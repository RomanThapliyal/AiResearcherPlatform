package servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

@WebServlet("/ProfilePictureServlet")
public class ProfilePictureServlet extends HttpServlet {

    /** Folder where profile pictures are stored: ~/ai_platform_uploads/profile_pictures */
    public static File pictureFolder() {
        return new File(System.getProperty("user.home"),
                "ai_platform_uploads" + File.separator + "profile_pictures");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId = user.getUserID();
        String param = request.getParameter("userId");
        if (param != null) {
            try {
                userId = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        final String prefix = "user_" + userId + ".";
        File[] matches = pictureFolder().listFiles((dir, name) -> name.startsWith(prefix));

        if (matches == null || matches.length == 0) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = matches[0];
        String lower = file.getName().toLowerCase();

        String type = "application/octet-stream";
        if (lower.endsWith(".png")) {
            type = "image/png";
        } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            type = "image/jpeg";
        } else if (lower.endsWith(".gif")) {
            type = "image/gif";
        } else if (lower.endsWith(".webp")) {
            type = "image/webp";
        }

        response.setContentType(type);
        response.setContentLengthLong(file.length());
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Cache-Control", "private, max-age=0, must-revalidate");

        Files.copy(file.toPath(), response.getOutputStream());
    }
}