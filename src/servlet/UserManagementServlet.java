package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.AdminService;

@WebServlet("/UserManagementServlet")
public class UserManagementServlet extends HttpServlet {

    private AdminService adminService = new AdminService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            response.sendRedirect("login.html");
            return;
        }

        List<User> users = adminService.getAllUsers();
        request.setAttribute("users", users);

        request.getRequestDispatcher("user_management.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            response.sendRedirect("login.html");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            User newUser = new User(0, name, email, password, role);

            boolean created = adminService.createUser(newUser);

            if (created) {
                response.sendRedirect("UserManagementServlet?created=true");
            } else {
                response.sendRedirect("UserManagementServlet?error=create");
            }

        } else if ("update".equals(action)) {

            int userId = Integer.parseInt(request.getParameter("userId"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            User updatedUser = new User(userId, name, email, password, role);

            boolean updated = adminService.updateUser(updatedUser);

            if (updated) {
                response.sendRedirect("UserManagementServlet?updated=true");
            } else {
                response.sendRedirect("UserManagementServlet?error=update");
            }

        } else if ("delete".equals(action)) {

            int userId = Integer.parseInt(request.getParameter("userId"));

            boolean deleted = adminService.deleteUser(userId);

            if (deleted) {
                response.sendRedirect("UserManagementServlet?deleted=true");
            } else {
                response.sendRedirect("UserManagementServlet?error=delete");
            }

        } else {
            response.sendRedirect("UserManagementServlet?error=invalid");
        }
    }
}