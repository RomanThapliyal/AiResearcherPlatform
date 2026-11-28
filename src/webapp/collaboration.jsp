
<%@ page import="java.util.List, java.util.Map, java.util.HashMap, model.User, model.Project, model.Collaboration" %>
<%!
    private static String statusLabel(String s) {
        if ("ACTIVE".equals(s)) return "Accepted";
        if ("CLOSED".equals(s)) return "Rejected";
        return "Pending";
    }
%>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect("login.html");
        return;
    }

    boolean isAdmin = "ADMIN".equals(sessionUser.getRole());

    List<Project> projects = (List<Project>) request.getAttribute("projects");
    List<Collaboration> collaborations = (List<Collaboration>) request.getAttribute("collaborations");
    List<Project> myProjects = (List<Project>) request.getAttribute("myProjects");
    Map<Integer, List<String>> teamNames = (Map<Integer, List<String>>) request.getAttribute("teamNames");

    Map<Integer, String> titles = new HashMap<>();
    if (projects != null) {
        for (Project p : projects) {
            titles.put(p.getProjectID(), p.getTitle());
        }
    }

    Map<Integer, String> myRequests = new HashMap<>();
    if (collaborations != null) {
        for (Collaboration c : collaborations) {
            if (c.getInitiatedBy() == sessionUser.getUserID()) {
                myRequests.put(c.getProjectID(), c.getStatus());
            }
        }
    }

    String error = request.getParameter("error");
    String errorMsg = null;
    if (error != null) {
        if ("forbidden".equals(error)) {
            errorMsg = "You are not allowed to do that.";
        } else {
            errorMsg = "Something went wrong. Please try again.";
        }
    }
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
        <div class="brand">AI R&D Platform - <%= isAdmin ? "Admin" : "Researcher" %></div>
        <div class="nav-links">
            <% if (isAdmin) { %>
                <a href="UserManagementServlet">Users</a>
                <a href="ResourceManagementServlet">Resources</a>
                <a href="ProjectManagementServlet">Projects</a>
                <a href="UsageMonitoringServlet">Usage Logs</a>
                <a href="CollaborationServlet">Collaborations</a>
            <% } else { %>
                <a href="DatasetManagementServlet">Datasets</a>
                <a href="ModelTrainingServlet">Models</a>
                <a href="ExperimentTrackingServlet">Experiments</a>
                <a href="CollaborationServlet">Collaborations</a>
                <a href="ProfileServlet">Profile</a>
            <% } %>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>

    <div class="container">
        <% if (errorMsg != null) { %>
            <div class="msg error"><%= errorMsg %></div>
        <% } %>

        <% if (request.getParameter("requested") != null || request.getParameter("updated") != null) { %>
            <div class="msg success">Action completed successfully.</div>
        <% } %>

        <% if (!isAdmin) { %>
        <div class="card">
            <h2>My Projects</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Team Members</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (myProjects != null && !myProjects.isEmpty()) {
                        for (Project p : myProjects) {
                            List<String> members = (teamNames != null) ? teamNames.get(p.getProjectID()) : null;
                %>
                    <tr>
                        <td><%= p.getProjectID() %></td>
                        <td><%= p.getTitle() %></td>
                        <td><%= p.getDescription() %></td>
                        <td><span class="status-badge"><%= p.getStatus() %></span></td>
                        <td><%= (members != null && !members.isEmpty()) ? String.join(", ", members) : "-" %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">You are not on any project yet. Request to join one below.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
        <% } %>

        <div class="card">
            <h2>Research Projects</h2>
            <input type="text" id="projectSearch" placeholder="Search projects by title or description..." style="margin-bottom:10px;">
            <table id="projectsTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Status</th>
                    <% if (!isAdmin) { %>
                        <th>Collaboration</th>
                    <% } %>
                </tr>
                </thead>
                <tbody>
                <%
                    if (projects != null && !projects.isEmpty()) {
                        for (Project p : projects) {
                            String mine = myRequests.get(p.getProjectID());
                %>
                    <tr>
                        <td><%= p.getProjectID() %></td>
                        <td><%= p.getTitle() %></td>
                        <td><%= p.getDescription() %></td>
                        <td><span class="status-badge"><%= p.getStatus() %></span></td>
                        <% if (!isAdmin) { %>
                        <td>
                            <% if (mine != null) { %>
                                <span class="status-badge">Requested: <%= statusLabel(mine) %></span>
                            <% } else { %>
                                <form action="CollaborationServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="request">
                                    <input type="hidden" name="projectId" value="<%= p.getProjectID() %>">
                                    <button type="submit">Request to Collaborate</button>
                                </form>
                            <% } %>
                        </td>
                        <% } %>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No projects have been created yet.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>Collaboration Requests</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Project</th>
                    <th>Requested By</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (collaborations != null && !collaborations.isEmpty()) {
                        for (Collaboration c : collaborations) {
                            String title = titles.get(c.getProjectID());
                            boolean own = c.getInitiatedBy() == sessionUser.getUserID();
                %>
                    <tr>
                        <td><%= c.getCollaborationID() %></td>
                        <td><%= title != null ? title : ("Project #" + c.getProjectID()) %></td>
                        <td><%= own ? "You" : ("User #" + c.getInitiatedBy()) %></td>
                        <td><span class="status-badge"><%= statusLabel(c.getStatus()) %></span></td>
                        <td>
                            <% if (isAdmin) { %>
                                <form action="CollaborationServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="updateStatus">
                                    <input type="hidden" name="collaborationId" value="<%= c.getCollaborationID() %>">
                                    <input type="hidden" name="projectId" value="<%= c.getProjectID() %>">
                                    <select name="status" onchange="this.form.submit()">
                                        <option value="PENDING" <%= "PENDING".equals(c.getStatus()) ? "selected" : "" %>>Pending</option>
                                        <option value="ACTIVE" <%= "ACTIVE".equals(c.getStatus()) ? "selected" : "" %>>Accepted</option>
                                        <option value="CLOSED" <%= "CLOSED".equals(c.getStatus()) ? "selected" : "" %>>Rejected</option>
                                    </select>
                                </form>
                            <% } else if (own && !"CLOSED".equals(c.getStatus())) { %>
                                <form action="CollaborationServlet" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="updateStatus">
                                    <input type="hidden" name="collaborationId" value="<%= c.getCollaborationID() %>">
                                    <input type="hidden" name="projectId" value="<%= c.getProjectID() %>">
                                    <input type="hidden" name="status" value="CLOSED">
                                    <button type="submit" class="danger" onclick="return confirm('Withdraw this request?');">Withdraw</button>
                                </form>
                            <% } else { %>
                                <span style="color:#888;">&mdash;</span>
                            <% } %>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No collaboration requests yet.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        document.getElementById("projectSearch").addEventListener("input", function () {
            var q = this.value.toLowerCase();
            document.querySelectorAll("#projectsTable tbody tr").forEach(function (r) {
                r.style.display = r.textContent.toLowerCase().indexOf(q) !== -1 ? "" : "none";
            });
        });
    </script>
</body>
</html>
