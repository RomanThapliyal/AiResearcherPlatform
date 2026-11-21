package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ProfileDAO;
import model.Profile;
import model.User;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

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

        Profile profile = new Profile(0, user.getUserID(), bio, contactInfo);

        boolean updated = profileDAO.updateProfile(profile);

        if (updated) {
            response.sendRedirect("ProfileServlet?updated=true");
        } else {
            response.sendRedirect("ProfileServlet?error=failed");
        }
    }
}