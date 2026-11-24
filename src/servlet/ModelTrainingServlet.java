package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import model.Dataset;

import model.MLModel;
import model.User;
import service.ResearcherService;

@WebServlet("/ModelTrainingServlet")
public class ModelTrainingServlet extends HttpServlet {

    private ResearcherService researcherService = new ResearcherService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        List<MLModel> models = researcherService.getMyModels(user.getUserID());
        List<Dataset> datasets = researcherService.getMyDatasets(user.getUserID());
        request.setAttribute("models", models);
        request.setAttribute("datasets", datasets);

        request.getRequestDispatcher("model_training.jsp")
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

        String name = request.getParameter("name");
        String datasetId = request.getParameter("datasetId");
        String parameters = request.getParameter("parameters");

        MLModel model = new MLModel(
                0,
                name,
                Integer.parseInt(datasetId),
                parameters,
                "PENDING",
                0,
                user.getUserID()
        );

        boolean started = researcherService.startTraining(model);

        if (started) {
            response.sendRedirect("ModelTrainingServlet?started=true");
        } else {
            response.sendRedirect("ModelTrainingServlet?error=failed");
        }
    }
}