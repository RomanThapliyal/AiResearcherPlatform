package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Resource;
import model.User;
import service.AdminService;

@WebServlet("/ResourceManagementServlet")
public class ResourceManagementServlet extends HttpServlet {

    private AdminService adminService = new AdminService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            response.sendRedirect("login.html");
            return;
        }

        List<Resource> resources = adminService.getAllResources();
        request.setAttribute("resources", resources);

        request.getRequestDispatcher("resource_management.jsp")
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
            String type = request.getParameter("type");
            String status = request.getParameter("status");

            Resource resource = new Resource(
                    0,
                    name,
                    type,
                    status,
                    user.getUserID()
            );

            boolean created = adminService.createResource(resource);

            if (created) {
                response.sendRedirect("ResourceManagementServlet?created=true");
            } else {
                response.sendRedirect("ResourceManagementServlet?error=create");
            }

        } else if ("update".equals(action)) {

            int resourceId = Integer.parseInt(request.getParameter("resourceId"));
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String status = request.getParameter("status");

            Resource resource = new Resource(
                    resourceId,
                    name,
                    type,
                    status,
                    user.getUserID()
            );

            boolean updated = adminService.updateResource(resource);

            if (updated) {
                response.sendRedirect("ResourceManagementServlet?updated=true");
            } else {
                response.sendRedirect("ResourceManagementServlet?error=update");
            }

        } else if ("delete".equals(action)) {

            int resourceId = Integer.parseInt(request.getParameter("resourceId"));

            boolean deleted = adminService.deleteResource(resourceId);

            if (deleted) {
                response.sendRedirect("ResourceManagementServlet?deleted=true");
            } else {
                response.sendRedirect("ResourceManagementServlet?error=delete");
            }

        } else {
            response.sendRedirect("ResourceManagementServlet?error=invalid");
        }
    }
}
