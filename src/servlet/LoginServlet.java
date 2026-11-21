package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.AuthService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("login.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.login(email, password);

        if (user == null) {
            response.sendRedirect("login.html?error=invalid");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserID());
        session.setAttribute("role", user.getRole());

        if (user.getRole().equals("ADMIN")) {
            response.sendRedirect("admin_dashboard.jsp");
        } else {
            response.sendRedirect("researcher_dashboard.jsp");
        }
    }
}
```