<%@ page import="java.util.List, model.User, model.Dataset" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"RESEARCHER".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    // NOTE: assumes DatasetManagementServlet attaches the list as request attribute "datasets".
    // Confirm against your servlet's doGet() and rename below if different.
    List<Dataset> datasets = (List<Dataset>) request.getAttribute("datasets");
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
        <% if (request.getParameter("error") != null) { %>
            <div class="msg error">Something went wrong. Please try again.</div>
        <% } %>
        <% if (request.getParameter("created") != null || request.getParameter("updated") != null || request.getParameter("deleted") != null) { %>
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
                    <th>File Path</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (datasets != null) {
                        for (Dataset d : datasets) {
                %>
                    <tr>
                        <td><%= d.getDatasetID() %></td>
                        <td><%= d.getName() %></td>
                        <td><%= d.getDescription() %></td>
                        <td><%= d.getFilePath() %></td>
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
            <form action="DatasetManagementServlet" method="post">
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
                    <label for="filePath">File Path</label>
                    <input type="text" id="filePath" name="filePath" placeholder="e.g. /datasets/mydata.csv" required>
                    <small style="color:#666;">Plain text path for now - actual file upload (multipart) is a later enhancement, not required by the framework.</small>
                </div>
                <button type="submit">Upload Dataset</button>
            </form>
        </div>
    </div>
</body>
</html>
