package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Collaboration;
import model.User;
import service.ResearcherService;

@WebServlet("/CollaborationServlet")
public class CollaborationServlet extends HttpServlet {

    private ResearcherService researcherService = new ResearcherService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String projectIdParam = request.getParameter("projectId");

        if (projectIdParam != null) {
            int projectId = Integer.parseInt(projectIdParam);
            researcherService.getCollaborationsForProject(projectId);
        }

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

        String action = request.getParameter("action");

        if ("request".equals(action)) {

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
            String status = request.getParameter("status");

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