<%@ page import="model.User, model.Profile" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect("login.html");
        return;
    }
    Profile profile = (Profile) request.getAttribute("profile");

    String bioValue = "";
    String contactValue = "";
    if (profile != null) {
        if (profile.getBio() != null) {
            bioValue = profile.getBio();
        }
        if (profile.getContactInfo() != null) {
            contactValue = profile.getContactInfo();
        }
    }

    String userName = sessionUser.getName() == null ? "" : sessionUser.getName();
    String initial = userName.isEmpty() ? "?" : userName.substring(0, 1).toUpperCase();

    String error = request.getParameter("error");
    String errorMsg = null;
    if (error != null) {
        if ("picture".equals(error)) {
            errorMsg = "Could not save the picture. Use a jpg, png, gif or webp image.";
        } else if ("toolarge".equals(error)) {
            errorMsg = "That picture is too large. The limit is 2 MB.";
        } else {
            errorMsg = "Something went wrong. Please try again.";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .account-row { display: flex; align-items: center; gap: 20px; flex-wrap: wrap; }
        .avatar {
            width: 96px; height: 96px; border-radius: 50%; object-fit: cover;
            border: 2px solid #ccd3e0; background: #eef1f7; flex-shrink: 0;
        }
        .avatar.fallback {
            align-items: center; justify-content: center;
            font-size: 38px; font-weight: 600; color: #1f2a44;
        }
        .account-info p { margin-bottom: 4px; }
    </style>
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
                <a href="CollaborationServlet">Collaborations</a>
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
        <% if (errorMsg != null) { %>
            <div class="msg error"><%= errorMsg %></div>
        <% } %>
        <% if (request.getParameter("updated") != null) { %>
            <div class="msg success">Profile saved successfully.</div>
        <% } %>

        <div class="card">
            <h2>Account</h2>
            <div class="account-row">
                <img id="avatarImg" class="avatar"
                     src="ProfilePictureServlet?userId=<%= sessionUser.getUserID() %>&t=<%= System.currentTimeMillis() %>"
                     alt="Profile picture"
                     onerror="this.style.display='none'; document.getElementById('avatarFallback').style.display='flex';">
                <div id="avatarFallback" class="avatar fallback" style="display:none;"><%= initial %></div>

                <div class="account-info">
                    <p><strong>Name:</strong> <%= sessionUser.getName() %></p>
                    <p><strong>Email:</strong> <%= sessionUser.getEmail() %></p>
                    <p><strong>Role:</strong> <span class="status-badge"><%= sessionUser.getRole() %></span></p>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>My Profile</h2>
            <form action="ProfileServlet" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="picture">Profile Picture</label>
                    <input type="file" id="picture" name="picture" accept=".jpg,.jpeg,.png,.gif,.webp">
                    <small style="color:#666;">jpg, png, gif or webp, up to 2 MB. Leave empty to keep your current picture.</small>
                </div>
                <div class="form-group">
                    <label for="bio">Bio</label>
                    <textarea id="bio" name="bio"><%= bioValue %></textarea>
                </div>
                <div class="form-group">
                    <label for="contactInfo">Contact Info</label>
                    <input type="text" id="contactInfo" name="contactInfo" value="<%= contactValue %>">
                </div>
                <button type="submit">Save Profile</button>
            </form>
        </div>
    </div>
</body>
</html>