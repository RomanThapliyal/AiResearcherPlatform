<%@ page import="model.User, model.Profile" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect("login.html");
        return;
    }
    // NOTE: assumes ProfileServlet attaches the Profile object (if one exists) as request attribute "profile".
    // Confirm against your servlet's doGet() and rename below if different.
    Profile profile = (Profile) request.getAttribute("profile");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <div class="brand">AI R&D Platform</div>
        <div class="nav-links">
            <% if ("ADMIN".equals(sessionUser.getRole())) { %>
                <a href="UserManagementServlet">Users</a>
                <a href="ResourceManagementServlet">Resources</a>
                <a href="ProjectManagementServlet">Projects</a>
                <a href="UsageMonitoringServlet">Usage Logs</a>
            <% } else { %>
                <a href="DatasetManagementServlet">Datasets</a>
                <a href="ModelTrainingServlet">Models</a>
                <a href="ExperimentTrackingServlet">Experiments</a>
                <a href="CollaborationServlet">Collaborations</a>
            <% } %>
            <a href="ProfileServlet">Profile</a>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>

    <div class="container">
        <% if (request.getParameter("error") != null) { %>
            <div class="msg error">Something went wrong. Please try again.</div>
        <% } %>
        <% if (request.getParameter("saved") != null) { %>
            <div class="msg success">Profile saved successfully.</div>
        <% } %>

        <div class="card">
            <h2>Account</h2>
            <p><strong>Name:</strong> <%= sessionUser.getName() %></p>
            <p><strong>Email:</strong> <%= sessionUser.getEmail() %></p>
            <p><strong>Role:</strong> <span class="status-badge"><%= sessionUser.getRole() %></span></p>
        </div>

        <div class="card">
            <h2>My Profile</h2>
            <form action="ProfileServlet" method="post">
                <input type="hidden" name="action" value="save">
                <div class="form-group">
                    <label for="bio">Bio</label>
                    <textarea id="bio" name="bio"><%= profile != null ? profile.getBio() : "" %></textarea>
                </div>
                <div class="form-group">
                    <label for="contactInfo">Contact Info</label>
                    <input type="text" id="contactInfo" name="contactInfo"
                           value="<%= profile != null ? profile.getContactInfo() : "" %>">
                </div>
                <button type="submit">Save Profile</button>
            </form>
        </div>
    </div>
</body>
</html>
