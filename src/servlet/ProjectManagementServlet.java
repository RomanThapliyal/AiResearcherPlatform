package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Project;
import model.User;
import service.AdminService;

@WebServlet("/ProjectManagementServlet")
public class ProjectManagementServlet extends HttpServlet {

    private AdminService adminService = new AdminService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            response.sendRedirect("login.html");
            return;
        }

        List<Project> projects = adminService.getAllProjects();
        request.setAttribute("projects", projects);

        List<User> users = adminService.getAllUsers();
        request.setAttribute("users", users);

        request.getRequestDispatcher("project_management.jsp")
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

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String status = request.getParameter("status");
            String[] teamIds = request.getParameterValues("teamUserIds");

            List<User> allUsers = adminService.getAllUsers();
            Set<Integer> validIds = new HashSet<>();
            if (allUsers != null) {
                for (User u : allUsers) {
                    validIds.add(u.getUserID());
                }
            }

            List<Integer> teamUserIds = new ArrayList<>();

            if (teamIds != null) {
                for (String teamId : teamIds) {
                    try {
                        int id = Integer.parseInt(teamId.trim());
                        if (!validIds.contains(id)) {
                            response.sendRedirect("ProjectManagementServlet?error=invaliduser");
                            return;
                        }
                        if (!teamUserIds.contains(id)) {
                            teamUserIds.add(id);
                        }
                    } catch (NumberFormatException e) {
                        response.sendRedirect("ProjectManagementServlet?error=invaliduser");
                        return;
                    }
                }
            }

            Project project = new Project(
                    0,
                    title,
                    description,
                    status,
                    user.getUserID()
            );

            boolean created = adminService.createProject(project, teamUserIds);

            if (created) {
                response.sendRedirect("ProjectManagementServlet?created=true");
            } else {
                response.sendRedirect("ProjectManagementServlet?error=create");
            }

        } else if ("update".equals(action)) {

            int projectId = Integer.parseInt(request.getParameter("projectId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String status = request.getParameter("status");

            Project project = new Project(
                    projectId,
                    title,
                    description,
                    status,
                    user.getUserID()
            );

            boolean updated = adminService.updateProject(project);

            if (updated) {
                response.sendRedirect("ProjectManagementServlet?updated=true");
            } else {
                response.sendRedirect("ProjectManagementServlet?error=update");
            }

        } else if ("delete".equals(action)) {

            int projectId = Integer.parseInt(request.getParameter("projectId"));

            boolean deleted = adminService.deleteProject(projectId);

            if (deleted) {
                response.sendRedirect("ProjectManagementServlet?deleted=true");
            } else {
                response.sendRedirect("ProjectManagementServlet?error=delete");
            }

        } else {
            response.sendRedirect("ProjectManagementServlet?error=invalid");
        }
    }
}