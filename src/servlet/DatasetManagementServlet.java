package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.Dataset;
import model.MLModel;
import model.User;
import service.ResearcherService;

@WebServlet("/DatasetManagementServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 20L * 1024 * 1024,
        maxRequestSize = 25L * 1024 * 1024
)
public class DatasetManagementServlet extends HttpServlet {

    private static final Set<String> ALLOWED_EXTENSIONS =
            new HashSet<>(Arrays.asList("csv", "tsv", "json", "txt", "xlsx", "xls", "zip"));

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

            String filePath;
            try {
                filePath = saveUploadedFile(request.getPart("file"));
            } catch (IllegalStateException e) {
                response.sendRedirect("DatasetManagementServlet?error=toolarge");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                response.sendRedirect("DatasetManagementServlet?error=upload");
                return;
            }

            if (filePath == null) {
                response.sendRedirect("DatasetManagementServlet?error=file");
                return;
            }

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

            List<MLModel> myModels = researcherService.getMyModels(user.getUserID());

            if (myModels != null) {
                for (MLModel m : myModels) {
                    if (m.getDatasetID() == datasetId) {
                        response.sendRedirect("DatasetManagementServlet?error=inuse");
                        return;
                    }
                }
            }

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

    private String saveUploadedFile(Part part) throws IOException {

        if (part == null || part.getSize() == 0) {
            return null;
        }

        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.trim().isEmpty()) {
            return null;
        }

        String original = Paths.get(submitted).getFileName().toString();
        String safe = original.replaceAll("[^A-Za-z0-9._-]", "_");

        int dot = safe.lastIndexOf('.');
        String extension = (dot >= 0) ? safe.substring(dot + 1).toLowerCase() : "";
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return null;
        }

        File folder = new File(System.getProperty("user.home"), "ai_platform_uploads");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create upload folder: " + folder);
        }

        File target = new File(folder, System.currentTimeMillis() + "_" + safe);

        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target.toPath());
        }

        return target.getAbsolutePath();
    }
}
