package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import dao.ProfileDAO;
import model.Profile;
import model.User;

@WebServlet("/ProfileServlet")
@MultipartConfig(
        fileSizeThreshold = 512 * 1024,
        maxFileSize = 2L * 1024 * 1024,
        maxRequestSize = 3L * 1024 * 1024
)
public class ProfileServlet extends HttpServlet {

    private static final Set<String> IMAGE_EXTENSIONS =
            new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "webp"));

    private ProfileDAO profileDAO = new ProfileDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        Profile profile = profileDAO.getProfileByUserID(user.getUserID());

        request.setAttribute("profile", profile);

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String bio = request.getParameter("bio");
        String contactInfo = request.getParameter("contactInfo");

        // 1. Profile picture (optional)
        try {
            Part picture = request.getPart("picture");
            if (picture != null && picture.getSize() > 0) {
                if (!savePicture(user.getUserID(), picture)) {
                    response.sendRedirect("ProfileServlet?error=picture");
                    return;
                }
            }
        } catch (IllegalStateException e) {
            // picture is larger than the allowed size
            response.sendRedirect("ProfileServlet?error=toolarge");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            response.sendRedirect("ProfileServlet?error=picture");
            return;
        }

        // 2. Bio and contact info
        Profile existing = profileDAO.getProfileByUserID(user.getUserID());
        Profile profile = new Profile(0, user.getUserID(), bio, contactInfo);

        boolean saved;
        if (existing == null) {
            saved = profileDAO.addProfile(profile);
        } else {
            saved = profileDAO.updateProfile(profile);
        }

        if (saved) {
            response.sendRedirect("ProfileServlet?updated=true");
        } else {
            response.sendRedirect("ProfileServlet?error=failed");
        }
    }

    /** Saves the picture as user_<id>.<ext>, replacing any earlier picture. Returns false if the type is not allowed. */
    private boolean savePicture(int userId, Part part) throws IOException {

        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.trim().isEmpty()) {
            return false;
        }

        String name = Paths.get(submitted).getFileName().toString().toLowerCase();
        int dot = name.lastIndexOf('.');
        String extension = (dot >= 0) ? name.substring(dot + 1) : "";

        if (!IMAGE_EXTENSIONS.contains(extension)) {
            return false;
        }

        File folder = ProfilePictureServlet.pictureFolder();
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create folder: " + folder);
        }

        // remove the user's previous picture, whatever its extension
        final String prefix = "user_" + userId + ".";
        File[] old = folder.listFiles((dir, n) -> n.startsWith(prefix));
        if (old != null) {
            for (File f : old) {
                f.delete();
            }
        }

        File target = new File(folder, prefix + extension);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target.toPath());
        }
        return true;
    }
}