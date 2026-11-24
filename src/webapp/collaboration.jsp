<%@ page import="java.util.List, model.User, model.Collaboration" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect("login.html");
        return;
    }
    List<Collaboration> collaborations = (List<Collaboration>) request.getAttribute("collaborations");
    String projectIdParam = request.getParameter("projectId");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Collaborations</title>
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
        <% if (request.getParameter("requested") != null || request.getParameter("updated") != null) { %>
            <div class="msg success">Action completed successfully.</div>
        <% } %>

        <div class="card">
            <h2>Collaborations<% if (projectIdParam != null) { %> for Project #<%= projectIdParam %><% } %></h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Project ID</th>
                    <th>Initiated By (User ID)</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (collaborations != null) {
                        for (Collaboration c : collaborations) {
                %>
                    <tr>
                        <td><%= c.getCollaborationID() %></td>
                        <td><%= c.getProjectID() %></td>
                        <td><%= c.getInitiatedBy() %></td>
                        <td><span class="status-badge"><%= c.getStatus() %></span></td>
                        <td>
                            <form action="CollaborationServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="collaborationId" value="<%= c.getCollaborationID() %>">
                                <select name="status" onchange="this.form.submit()">
                                    <option value="PENDING" <%= "PENDING".equals(c.getStatus()) ? "selected" : "" %>>Pending</option>
                                    <option value="ACTIVE" <%= "ACTIVE".equals(c.getStatus()) ? "selected" : "" %>>Active</option>
                                    <option value="CLOSED" <%= "CLOSED".equals(c.getStatus()) ? "selected" : "" %>>Closed</option>
                                </select>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No collaborations found. Enter a project ID in the box below to view its collaborations.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>View Collaborations for a Project</h2>
            <form action="CollaborationServlet" method="get">
                <div class="form-group">
                    <label for="projectIdView">Project ID</label>
                    <input type="number" id="projectIdView" name="projectId" required>
                </div>
                <button type="submit">View</button>
            </form>
        </div>

        <div class="card">
            <h2>Request a New Collaboration</h2>
            <form action="CollaborationServlet" method="post">
                <input type="hidden" name="action" value="request">
                <div class="form-group">
                    <label for="projectId">Project ID</label>
                    <input type="number" id="projectId" name="projectId" required>
                </div>
                <div class="form-group">
                    <label for="status">Initial Status</label>
                    <input type="text" id="status" name="status" value="PENDING" required>
                </div>
                <button type="submit">Request Collaboration</button>
            </form>
        </div>
    </div>
</body>
</html>
