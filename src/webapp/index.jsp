<%@ page import="model.User" %>
<%
    // Entry point: send the user to the right place based on session state.
    User sessionUser = (User) session.getAttribute("user");

    if (sessionUser == null) {
        response.sendRedirect("login.html");
        return;
    }

    if ("ADMIN".equals(sessionUser.getRole())) {
        response.sendRedirect("UserManagementServlet");
    } else {
        response.sendRedirect("ModelTrainingServlet");
    }
%>
