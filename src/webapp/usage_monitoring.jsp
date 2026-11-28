
<%@ page import="java.util.List, java.util.Map, model.User, model.UsageLog" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    List<UsageLog> usageLogs = (List<UsageLog>) request.getAttribute("usageLogs");
    Map<Integer, Integer> usageCounts = (Map<Integer, Integer>) request.getAttribute("usageCountByResource");

    StringBuilder labels = new StringBuilder();
    StringBuilder counts = new StringBuilder();
    if (usageCounts != null) {
        for (Map.Entry<Integer, Integer> e : usageCounts.entrySet()) {
            if (labels.length() > 0) {
                labels.append(",");
                counts.append(",");
            }
            labels.append("\"Resource #").append(e.getKey()).append("\"");
            counts.append(e.getValue());
        }
    }
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
            <a href="CollaborationServlet">Collaborations</a>
            <a href="LogoutServlet">Logout</a>
        </div>
    </div>

    <div class="container">
        <div class="card">
            <h2>Usage by Resource</h2>
            <% if (usageCounts != null && !usageCounts.isEmpty()) { %>
                <div style="max-width:700px;"><canvas id="usageChart"></canvas></div>
            <% } else { %>
                <p>No usage data to chart yet.</p>
            <% } %>
        </div>

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
                    <th>Usage Detail</th>
                    <th>Logged At</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (usageLogs != null && !usageLogs.isEmpty()) {
                        for (UsageLog log : usageLogs) {
                %>
                    <tr>
                        <td><%= log.getLogID() %></td>
                        <td><%= log.getResourceID() %></td>
                        <td><%= log.getUserID() %></td>
                        <td><%= log.getUsageDetail() %></td>
                        <td><%= log.getLoggedAt() %></td>
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

    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
    <script>
        (function () {
            var canvas = document.getElementById("usageChart");
            if (!canvas || typeof Chart === "undefined") return;
            new Chart(canvas, {
                type: "bar",
                data: {
                    labels: [<%= labels %>],
                    datasets: [{
                        label: "Times used",
                        data: [<%= counts %>],
                        backgroundColor: "#2d6cdf"
                    }]
                },
                options: {
                    plugins: { legend: { display: false } },
                    scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
                }
            });
        })();
    </script>
</body>
</html>
