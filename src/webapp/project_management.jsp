<%@ page import="java.util.List, model.User, model.Project" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    List<Project> projects = (List<Project>) request.getAttribute("projects");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Project Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <div class="brand">AI R&D Platform - Admin</div>
        <div class="nav-links">
            <a href="UserManagementServlet">Users</a>
            <a href="ResourceManagementServlet">Resources</a>
            <a href="ProjectManagementServlet">Projects</a>
            <a href="UsageMonitoringServlet">Usage Logs</a>
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
            <h2>All Projects</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (projects != null) {
                        for (Project p : projects) {
                %>
                    <tr>
                        <td><%= p.getProjectID() %></td>
                        <td><%= p.getTitle() %></td>
                        <td><%= p.getDescription() %></td>
                        <td><span class="status-badge"><%= p.getStatus() %></span></td>
                        <td>
                            <form action="ProjectManagementServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="projectId" value="<%= p.getProjectID() %>">
                                <button type="submit" class="danger" onclick="return confirm('Delete this project?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No projects found.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>Create New Project</h2>
            <form action="ProjectManagementServlet" method="post">
                <input type="hidden" name="action" value="create">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" name="title" required>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" required></textarea>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <input type="text" id="status" name="status" placeholder="e.g. ACTIVE" required>
                </div>
                <div class="form-group">
                    <label for="teamUserIds">Team Member User IDs</label>
                    <input type="text" id="teamUserIds" name="teamUserIds" placeholder="e.g. 2, 3, 5 (comma separated)">
                    <small style="color:#666;">Enter the numeric user IDs of team members, separated by commas.</small>
                </div>
                <button type="submit">Create Project</button>
            </form>
        </div>
    </div>

    <script>
        // Split the comma-separated teamUserIds field into repeated hidden inputs
        // before submit, since the servlet expects request.getParameterValues("teamUserIds").
        document.querySelector('form[action="ProjectManagementServlet"]').addEventListener('submit', function (e) {
            var field = document.getElementById('teamUserIds');
            var raw = field.value.trim();
            field.name = ""; // remove the raw field from submission
            if (raw.length > 0) {
                var ids = raw.split(',');
                for (var i = 0; i < ids.length; i++) {
                    var id = ids[i].trim();
                    if (id.length > 0) {
                        var hidden = document.createElement('input');
                        hidden.type = 'hidden';
                        hidden.name = 'teamUserIds';
                        hidden.value = id;
                        e.target.appendChild(hidden);
                    }
                }
            }
        });
    </script>
</body>
</html>
