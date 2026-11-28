<%@ page import="java.util.List, model.User" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    List<User> users = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Management</title>
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
            <a href="CollaborationServlet">Collaborations</a>
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
            <h2>All Users</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (users != null) {
                        for (User u : users) {
                %>
                    <tr>
                        <td><%= u.getUserID() %></td>
                        <td><%= u.getName() %></td>
                        <td><%= u.getEmail() %></td>
                        <td><span class="status-badge"><%= u.getRole() %></span></td>
                        <td>
                            <form action="UserManagementServlet" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="userId" value="<%= u.getUserID() %>">
                                <button type="submit" class="danger" onclick="return confirm('Delete this user?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No users found.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>Add New User</h2>
            <form action="UserManagementServlet" method="post">
                <input type="hidden" name="action" value="create">
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="role">Role</label>
                    <select id="role" name="role" required>
                        <option value="ADMIN">Admin</option>
                        <option value="RESEARCHER">Researcher</option>
                    </select>
                </div>
                <button type="submit">Add User</button>
            </form>
        </div>
    </div>
</body>
</html>
