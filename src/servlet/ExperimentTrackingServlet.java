package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Experiment;
import model.User;
import service.ResearcherService;

@WebServlet("/ExperimentTrackingServlet")
public class ExperimentTrackingServlet extends HttpServlet {

    private ResearcherService researcherService = new ResearcherService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String modelIdParam = request.getParameter("modelId");

        if (modelIdParam != null) {
            int modelId = Integer.parseInt(modelIdParam);
            List<Experiment> experiments = researcherService.getExperimentsForModel(modelId);
            request.setAttribute("experiments", experiments);
        }

        request.getRequestDispatcher("experiment_tracking.jsp")
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

        if ("log".equals(action)) {

            int modelId = Integer.parseInt(request.getParameter("modelId"));
            String parameters = request.getParameter("parameters");
            String results = request.getParameter("results");
            String performanceMetric = request.getParameter("performanceMetric");

            Experiment experiment = new Experiment(
                    0,
                    modelId,
                    parameters,
                    results,
                    performanceMetric,
                    user.getUserID()
            );

            boolean logged = researcherService.logExperiment(experiment);

            if (logged) {
                response.sendRedirect("ExperimentTrackingServlet?logged=true");
            } else {
                response.sendRedirect("ExperimentTrackingServlet?error=log");
            }

        } else if ("view".equals(action)) {

            int modelId = Integer.parseInt(request.getParameter("modelId"));

            response.sendRedirect("ExperimentTrackingServlet?modelId=" + modelId);

        } else {
            response.sendRedirect("ExperimentTrackingServlet?error=invalid");
        }
    }
}