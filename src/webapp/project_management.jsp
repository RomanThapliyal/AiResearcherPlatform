
<%@ page import="java.util.List, model.User, model.Project" %>
<%!
    private static String js(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("<", "\\u003c")
                .replace("\n", " ")
                .replace("\r", " ");
    }
%>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null || !"ADMIN".equals(sessionUser.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    List<Project> projects = (List<Project>) request.getAttribute("projects");
    List<User> users = (List<User>) request.getAttribute("users");

    out.println("<!-- users=" + (users == null ? "null" : String.valueOf(users.size())) + " -->");

    String error = request.getParameter("error");
    String errorMsg = null;
    if (error != null) {
        if ("invaliduser".equals(error)) {
            errorMsg = "One or more selected team members do not exist. Pick users from the suggestion list.";
        } else if ("create".equals(error)) {
            errorMsg = "Could not create the project. Check that the title, description and status are valid.";
        } else if ("update".equals(error)) {
            errorMsg = "Could not update the project.";
        } else if ("delete".equals(error)) {
            errorMsg = "Could not delete the project.";
        } else {
            errorMsg = "Something went wrong. Please try again.";
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Project Management</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .picker { position: relative; }
        .chips { display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 6px; }
        .chip {
            background: #eef1f7; border: 1px solid #ccd3e0; border-radius: 14px;
            padding: 3px 6px 3px 10px; font-size: 13px; display: inline-flex; align-items: center; gap: 6px;
        }
        .chip button {
            background: none; color: #555; border: none; padding: 0 4px;
            font-size: 15px; line-height: 1; cursor: pointer;
        }
        .chip button:hover { color: #d9534f; background: none; }
        .suggestions {
            position: absolute; left: 0; right: 0; z-index: 10; background: #fff;
            border: 1px solid #ccd3e0; border-radius: 5px; margin-top: 2px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.12); max-height: 240px; overflow-y: auto;
        }
        .suggestion { padding: 8px 12px; cursor: pointer; font-size: 14px; }
        .suggestion:hover, .suggestion.active { background: #eef3ff; }
        .suggestion .sub { color: #666; font-size: 12px; }
        .suggestion-empty { padding: 8px 12px; font-size: 13px; color: #888; }
    </style>
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
        <% if (errorMsg != null) { %>
            <div class="msg error"><%= errorMsg %></div>
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
                    if (projects != null && !projects.isEmpty()) {
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
            <form action="ProjectManagementServlet" method="post" id="projectForm">
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
                    <label for="memberSearch">Team Members</label>
                    <div class="picker">
                        <div id="chips" class="chips"></div>
                        <input type="text" id="memberSearch" placeholder="Search by name, email or user ID..." autocomplete="off">
                        <div id="suggestions" class="suggestions" style="display:none;"></div>
                    </div>
                    <div id="hiddenIds"></div>
                    <small style="color:#666;">Start typing, then click a user to add them to the team.</small>
                </div>
                <button type="submit">Create Project</button>
            </form>
        </div>
    </div>

    <script>
        var USERS = [
        <%
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);
        %>
            {id: <%= u.getUserID() %>, name: "<%= js(u.getName()) %>", email: "<%= js(u.getEmail()) %>", role: "<%= js(u.getRole()) %>"}<%= (i < users.size() - 1) ? "," : "" %>
        <%
                }
            }
        %>
        ];

        var selected = {};
        var searchBox = document.getElementById("memberSearch");
        var box = document.getElementById("suggestions");
        var chips = document.getElementById("chips");
        var hidden = document.getElementById("hiddenIds");
        var activeIndex = -1;
        var visible = [];

        function addMember(user) {
            if (selected[user.id]) return;

            var input = document.createElement("input");
            input.type = "hidden";
            input.name = "teamUserIds";
            input.value = user.id;
            hidden.appendChild(input);

            var chip = document.createElement("span");
            chip.className = "chip";
            var label = document.createElement("span");
            label.textContent = user.name + " (#" + user.id + ")";
            var remove = document.createElement("button");
            remove.type = "button";
            remove.textContent = "\u00d7";
            remove.title = "Remove";
            remove.onclick = function () { removeMember(user.id); };
            chip.appendChild(label);
            chip.appendChild(remove);
            chips.appendChild(chip);

            selected[user.id] = { chip: chip, input: input };
            searchBox.value = "";
            hideSuggestions();
        }

        function removeMember(id) {
            var entry = selected[id];
            if (!entry) return;
            chips.removeChild(entry.chip);
            hidden.removeChild(entry.input);
            delete selected[id];
        }

        function hideSuggestions() {
            box.style.display = "none";
            box.innerHTML = "";
            activeIndex = -1;
            visible = [];
        }

        function showSuggestions() {
            var q = searchBox.value.trim().toLowerCase();
            box.innerHTML = "";
            visible = [];
            activeIndex = -1;
            if (q === "") { box.style.display = "none"; return; }

            for (var i = 0; i < USERS.length && visible.length < 8; i++) {
                var u = USERS[i];
                if (selected[u.id]) continue;
                var hit = String(u.id) === q ||
                          u.name.toLowerCase().indexOf(q) !== -1 ||
                          u.email.toLowerCase().indexOf(q) !== -1;
                if (hit) visible.push(u);
            }

            if (visible.length === 0) {
                var none = document.createElement("div");
                none.className = "suggestion-empty";
                none.textContent = "No matching users";
                box.appendChild(none);
            } else {
                visible.forEach(function (u) {
                    var row = document.createElement("div");
                    row.className = "suggestion";
                    var main = document.createElement("div");
                    main.textContent = u.name + " (#" + u.id + ")";
                    var sub = document.createElement("div");
                    sub.className = "sub";
                    sub.textContent = u.email + " \u2022 " + u.role;
                    row.appendChild(main);
                    row.appendChild(sub);
                    row.addEventListener("mousedown", function (e) {
                        e.preventDefault();
                        addMember(u);
                    });
                    box.appendChild(row);
                });
            }
            box.style.display = "block";
        }

        function highlight() {
            var rows = box.querySelectorAll(".suggestion");
            for (var i = 0; i < rows.length; i++) {
                rows[i].classList.toggle("active", i === activeIndex);
            }
        }

        searchBox.addEventListener("input", showSuggestions);
        searchBox.addEventListener("focus", showSuggestions);
        searchBox.addEventListener("blur", function () { setTimeout(hideSuggestions, 120); });
        searchBox.addEventListener("keydown", function (e) {
            if (e.key === "ArrowDown") {
                if (visible.length) { activeIndex = (activeIndex + 1) % visible.length; highlight(); }
                e.preventDefault();
            } else if (e.key === "ArrowUp") {
                if (visible.length) { activeIndex = (activeIndex - 1 + visible.length) % visible.length; highlight(); }
                e.preventDefault();
            } else if (e.key === "Enter") {
                e.preventDefault();
                if (visible.length) { addMember(visible[activeIndex >= 0 ? activeIndex : 0]); }
            } else if (e.key === "Escape") {
                hideSuggestions();
            }
        });
    </script>
</body>
</html>
