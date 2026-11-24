<%@ page import="java.util.List, model.User, model.Experiment" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"RESEARCHER".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    List<Experiment> experiments = (List<Experiment>) request.getAttribute("experiments");
    String modelIdParam = request.getParameter("modelId");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Experiment Tracking</title>
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

        <% if (request.getParameter("logged") != null) { %>
            <div class="msg success">Experiment logged successfully.</div>
        <% } %>

        <div class="card">
            <h2>Experiments<% if (modelIdParam != null) { %> for Model #<%= modelIdParam %><% } %></h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Model ID</th>
                    <th>Parameters</th>
                    <th>Result</th>
                    <th>Performance Metric</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (experiments != null) {
                        for (Experiment ex : experiments) {
                %>
                    <tr>
                        <td><%= ex.getExperimentID() %></td>
                        <td><%= ex.getModelID() %></td>
                        <td><%= ex.getParameters() %></td>
                        <td><%= ex.getResults() %></td>
                        <td><%= ex.getPerformanceMetric() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No experiments found. Select a model from the Models page to view its experiments.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>Log a New Experiment</h2>
            <form action="ExperimentTrackingServlet" method="post">
                <input type="hidden" name="action" value="log">
                <div class="form-group">
                    <label for="modelId">Model ID</label>
                    <input type="number" id="modelId" name="modelId" value="<%= modelIdParam != null ? modelIdParam : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="parameters">Parameters</label>
                    <textarea id="parameters" name="parameters" required></textarea>
                </div>
                <div class="form-group">
                    <label for="result">Result</label>
                    <textarea id="result" name="results" required></textarea>
                </div>
                <div class="form-group">
                    <label for="performanceMetric">Performance Metric</label>
                    <input type="text" id="performanceMetric" name="performanceMetric" placeholder="e.g. accuracy=0.92" required>
                </div>
                <button type="submit">Log Experiment</button>
            </form>
        </div>
    </div>
</body>
</html>