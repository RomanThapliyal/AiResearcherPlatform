package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UsageLog;
import model.User;
import service.AdminService;

@WebServlet("/UsageMonitoringServlet")
public class UsageMonitoringServlet extends HttpServlet {

    private AdminService adminService = new AdminService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            response.sendRedirect("login.html");
            return;
        }

        List<UsageLog> usageLogs = adminService.getAllUsageLogs();
        Map<Integer, Integer> usageCountByResource = adminService.getUsageCountByResource();

        request.setAttribute("usageLogs", usageLogs);
        request.setAttribute("usageCountByResource", usageCountByResource);

        request.getRequestDispatcher("usage_monitoring.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("UsageMonitoringServlet");
    }
}