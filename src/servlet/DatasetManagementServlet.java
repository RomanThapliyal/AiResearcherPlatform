package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Dataset;
import model.User;
import service.ResearcherService;

@WebServlet("/DatasetManagementServlet")
public class DatasetManagementServlet extends HttpServlet {

    private ResearcherService researcherService = new ResearcherService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        List<Dataset> datasets = researcherService.getMyDatasets(user.getUserID());
        request.setAttribute("datasets", datasets);

        request.getRequestDispatcher("dataset_management.jsp")
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

        if ("upload".equals(action)) {

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String filePath = request.getParameter("filePath");

            Dataset dataset = new Dataset(
                    0,
                    name,
                    description,
                    filePath,
                    user.getUserID()
            );

            boolean uploaded = researcherService.uploadDataset(dataset);

            if (uploaded) {
                response.sendRedirect("DatasetManagementServlet?uploaded=true");
            } else {
                response.sendRedirect("DatasetManagementServlet?error=upload");
            }

        } else if ("update".equals(action)) {

            int datasetId = Integer.parseInt(request.getParameter("datasetId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String filePath = request.getParameter("filePath");

            Dataset dataset = new Dataset(
                    datasetId,
                    name,
                    description,
                    filePath,
                    user.getUserID()
            );

            boolean updated = researcherService.updateDataset(dataset);

            if (updated) {
                response.sendRedirect("DatasetManagementServlet?updated=true");
            } else {
                response.sendRedirect("DatasetManagementServlet?error=update");
            }

        } else if ("delete".equals(action)) {

            int datasetId = Integer.parseInt(request.getParameter("datasetId"));

            boolean deleted = researcherService.deleteDataset(datasetId);

            if (deleted) {
                response.sendRedirect("DatasetManagementServlet?deleted=true");
            } else {
                response.sendRedirect("DatasetManagementServlet?error=delete");
            }

        } else {
            response.sendRedirect("DatasetManagementServlet?error=invalid");
        }
    }
}