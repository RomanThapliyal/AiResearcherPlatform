<%@ page import="java.util.List, model.User, model.MLModel, model.Dataset" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"RESEARCHER".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    // NOTE: assumes ModelTrainingServlet attaches:
    //   "models"   -> List<MLModel> (this researcher's models)
    //   "datasets" -> List<Dataset> (to populate the dataset dropdown below)
    // Confirm against your servlet's doGet() and rename below if different.
    List<MLModel> models = (List<MLModel>) request.getAttribute("models");
    List<Dataset> datasets = (List<Dataset>) request.getAttribute("datasets");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Model Training</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<%
    boolean training = false;
    if (models != null) {
        for (MLModel m : models) {
            if ("PENDING".equals(m.getTrainingStatus()) || "TRAINING".equals(m.getTrainingStatus())) {
                training = true;
                break;
            }
        }
    }
%>
<% if (training) { %>
<script>
    setInterval(function () {
        var typing = document.getElementById("name").value !== ""
                  || document.getElementById("parameters").value !== "";
        if (!typing) {
            location.reload();
        }
    }, 5000);
</script>
<% } %>
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

        <div class="card">
            <h2>My Models</h2>
            <p style="color:#666; font-size:13px; margin-bottom:10px;">This page auto-refreshes every 5 seconds so you can watch training progress update.</p>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Dataset ID</th>
                    <th>Status</th>
                    <th>Progress</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (models != null) {
                        for (MLModel m : models) {
                %>
                    <tr>
                        <td><%= m.getModelID() %></td>
                        <td><%= m.getName() %></td>
                        <td><%= m.getDatasetID() %></td>
                        <td><span class="status-badge"><%= m.getTrainingStatus() %></span></td>
                        <td><%= m.getProgressPercentage() %>%</td>
                        <td>
                            <a class="btn" href="ExperimentTrackingServlet?action=view&modelId=<%= m.getModelID() %>">View Experiments</a>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="6">No models found.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>Train a New Model</h2>
            <form action="ModelTrainingServlet" method="post">
                <input type="hidden" name="action" value="train">
                <div class="form-group">
                    <label for="name">Model Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="datasetId">Dataset</label>
                    <select id="datasetId" name="datasetId" required>
                        <option value="">-- Select Dataset --</option>
                        <%
                            if (datasets != null) {
                                for (Dataset d : datasets) {
                        %>
                            <option value="<%= d.getDatasetID() %>"><%= d.getName() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="form-group">
                    <label for="parameters">Parameters</label>
                    <textarea id="parameters" name="parameters" placeholder="e.g. learning_rate=0.01, epochs=50" required></textarea>
                </div>
                <button type="submit">Start Training</button>
            </form>
        </div>
    </div>
</body>
</html>
