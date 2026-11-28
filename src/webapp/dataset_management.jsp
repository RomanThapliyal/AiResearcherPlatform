<%@ page import="java.util.List, java.io.File, model.User, model.Dataset" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"RESEARCHER".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    List<Dataset> datasets = (List<Dataset>) request.getAttribute("datasets");

    String error = request.getParameter("error");
    String errorMsg = null;
    if (error != null) {
        if ("file".equals(error)) {
            errorMsg = "Please choose a dataset file (csv, tsv, json, txt, xlsx, xls or zip).";
        } else if ("toolarge".equals(error)) {
            errorMsg = "That file is too large. The limit is 20 MB.";
        } else if ("upload".equals(error)) {
            errorMsg = "The upload failed. Check the name and description and try again.";
        } else if ("inuse".equals(error)) {
            errorMsg = "This dataset was used to train a model, so it can't be deleted.";
        } else if ("delete".equals(error)) {
            errorMsg = "The dataset could not be deleted.";
        } else if ("update".equals(error)) {
            errorMsg = "The dataset could not be updated.";
        } else {
            errorMsg = "Something went wrong. Please try again.";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dataset Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <div class="brand">AI R&D Platform - Researcher</div>
        <div class="nav-links">
            <a href="DatasetManagementServlet">Datasets</a>
            <a href="ModelTrainingServlet">Models</a>
            <a href="ExperimentTrackingServlet">Experiments</a>
            <a href="CollaborationServlet">Collaborations</a>
            <a href="ProfileServlet">Profile</a>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>

    <div class="container">
        <% if (errorMsg != null) { %>
            <div class="msg error"><%= errorMsg %></div>
        <% } %>
        <% if (request.getParameter("uploaded") != null || request.getParameter("updated") != null || request.getParameter("deleted") != null) { %>
            <div class="msg success">Action completed successfully.</div>
        <% } %>

        <div class="card">
            <h2>My Datasets</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>File</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (datasets != null && !datasets.isEmpty()) {
                        for (Dataset d : datasets) {
                            String fileName = d.getFilePath() == null ? "" :
                                    new File(d.getFilePath()).getName().replaceFirst("^\\d+_", "");
                %>
                    <tr>
                        <td><%= d.getDatasetID() %></td>
                        <td><%= d.getName() %></td>
                        <td><%= d.getDescription() %></td>
                        <td><%= fileName %></td>
                        <td>
                            <form action="DatasetManagementServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="datasetId" value="<%= d.getDatasetID() %>">
                                <button type="submit" class="danger" onclick="return confirm('Delete this dataset?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No datasets found.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>Upload New Dataset</h2>
            <form action="DatasetManagementServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="upload">
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" required></textarea>
                </div>
                <div class="form-group">
                    <label for="file">Dataset File</label>
                    <input type="file" id="file" name="file" accept=".csv,.tsv,.json,.txt,.xlsx,.xls,.zip" required>
                    <small style="color:#666;">Allowed: csv, tsv, json, txt, xlsx, xls, zip. Maximum 20 MB.</small>
                </div>
                <button type="submit">Upload Dataset</button>
            </form>
        </div>
    </div>
</body>
</html>