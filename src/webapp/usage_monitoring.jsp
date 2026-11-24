<%@ page import="java.util.List, java.util.Map, model.User, model.UsageLog" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    // NOTE: assumes UsageMonitoringServlet attaches these two attributes under these names,
    // and that UsageLog has getLogID(), getResourceID(), getUserID(), getAction(), getTimestamp().
    // Paste UsageLog.java and UsageMonitoringServlet.java if any of these don't match, so this can be corrected.
    List<UsageLog> usageLogs = (List<UsageLog>) request.getAttribute("usageLogs");
    Map<Integer, Integer> usageCounts = (Map<Integer, Integer>) request.getAttribute("usageCounts");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Usage Monitoring</title>
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
        <div class="card">
            <h2>Usage Count by Resource</h2>
            <table>
                <thead>
                <tr>
                    <th>Resource ID</th>
                    <th>Times Used</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (usageCounts != null && !usageCounts.isEmpty()) {
                        for (Map.Entry<Integer, Integer> entry : usageCounts.entrySet()) {
                %>
                    <tr>
                        <td><%= entry.getKey() %></td>
                        <td><%= entry.getValue() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="2">No usage data found.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h2>All Usage Logs</h2>
            <table>
                <thead>
                <tr>
                    <th>Log ID</th>
                    <th>Resource ID</th>
                    <th>User ID</th>
                    <th>Action</th>
                    <th>Timestamp</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (usageLogs != null) {
                        for (UsageLog log : usageLogs) {
                %>
                    <tr>
                        <td><%= log.getLogID() %></td>
                        <td><%= log.getResourceID() %></td>
                        <td><%= log.getUserID() %></td>
                        <td><%= log.getAction() %></td>
                        <td><%= log.getTimestamp() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5">No usage logs found.</td></tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
