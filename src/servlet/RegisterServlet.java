package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import service.AuthService;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private AuthService authService = new AuthService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("register.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User user = new User(0, name, email, password, role);

        boolean registered = authService.register(user);

        if (registered) {
            response.sendRedirect("login.html?registered=true");
        } else {
            response.sendRedirect("register.html?error=failed");
        }
    }
}