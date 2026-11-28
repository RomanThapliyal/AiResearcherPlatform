package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ProjectDAO;
import model.Collaboration;
import model.Project;
import model.User;
import service.AdminService;
import service.ResearcherService;

@WebServlet("/CollaborationServlet")
public class CollaborationServlet extends HttpServlet {

    private ResearcherService researcherService = new ResearcherService();
    private AdminService adminService = new AdminService();
    private ProjectDAO projectDAO = new ProjectDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        List<Project> projects = adminService.getAllProjects();
        List<Collaboration> collaborations = new ArrayList<>();

        if (projects != null) {
            for (Project p : projects) {
                List<Collaboration> forProject =
                        researcherService.getCollaborationsForProject(p.getProjectID());
                if (forProject != null) {
                    collaborations.addAll(forProject);
                }
            }
        }

        // user id -> name, used to show team members by name
        Map<Integer, String> userNames = new HashMap<>();
        List<User> allUsers = adminService.getAllUsers();
        if (allUsers != null) {
            for (User u : allUsers) {
                userNames.put(u.getUserID(), u.getName());
            }
        }

        // "My Projects": on the project team, or has an accepted (ACTIVE) collaboration
        List<Project> myProjects = new ArrayList<>();
        Map<Integer, List<String>> teamNames = new HashMap<>();

        if (projects != null) {
            for (Project p : projects) {
                int projectId = p.getProjectID();

                Set<Integer> memberIds = new LinkedHashSet<>(projectDAO.getTeamForProject(projectId));
                boolean mine = memberIds.contains(user.getUserID());

                for (Collaboration c : collaborations) {
                    if (c.getProjectID() == projectId && "ACTIVE".equals(c.getStatus())) {
                        memberIds.add(c.getInitiatedBy());
                        if (c.getInitiatedBy() == user.getUserID()) {
                            mine = true;
                        }
                    }
                }

                if (mine) {
                    myProjects.add(p);
                    List<String> names = new ArrayList<>();
                    for (Integer id : memberIds) {
                        String name = userNames.get(id);
                        names.add(name != null ? name : ("User #" + id));
                    }
                    teamNames.put(projectId, names);
                }
            }
        }

        request.setAttribute("projects", projects);
        request.setAttribute("collaborations", collaborations);
        request.setAttribute("myProjects", myProjects);
        request.setAttribute("teamNames", teamNames);

        request.getRequestDispatcher("collaboration.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        boolean isAdmin = "ADMIN".equals(user.getRole());
        String action = request.getParameter("action");

        if ("request".equals(action)) {

            if (isAdmin) {
                response.sendRedirect("CollaborationServlet?error=forbidden");
                return;
            }

            int projectId = Integer.parseInt(request.getParameter("projectId"));
            String status = request.getParameter("status");

            Collaboration collaboration = new Collaboration(
                    0,
                    projectId,
                    user.getUserID(),
                    status
            );

            boolean requested = researcherService.requestCollaboration(collaboration);

            if (requested) {
                response.sendRedirect("CollaborationServlet?requested=true");
            } else {
                response.sendRedirect("CollaborationServlet?error=request");
            }

        } else if ("updateStatus".equals(action)) {

            int collaborationId = Integer.parseInt(request.getParameter("collaborationId"));
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            String status = request.getParameter("status");

            boolean allowed = false;

            if (isAdmin) {
                allowed = true;
            } else if ("CLOSED".equals(status)) {
                // A researcher may only withdraw their own request.
                List<Collaboration> forProject = researcherService.getCollaborationsForProject(projectId);
                if (forProject != null) {
                    for (Collaboration c : forProject) {
                        if (c.getCollaborationID() == collaborationId
                                && c.getInitiatedBy() == user.getUserID()) {
                            allowed = true;
                            break;
                        }
                    }
                }
            }

            if (!allowed) {
                response.sendRedirect("CollaborationServlet?error=forbidden");
                return;
            }

            boolean updated = researcherService.updateCollaborationStatus(collaborationId, status);

            if (updated) {
                response.sendRedirect("CollaborationServlet?updated=true");
            } else {
                response.sendRedirect("CollaborationServlet?error=update");
            }

        } else {
            response.sendRedirect("CollaborationServlet?error=invalid");
        }
    }
}