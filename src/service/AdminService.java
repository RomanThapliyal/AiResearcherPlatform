package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.ProjectDAO;
import dao.ResourceDAO;
import dao.UsageLogDAO;
import dao.UserDAO;
import model.Project;
import model.Resource;
import model.UsageLog;
import model.User;

public class AdminService {

    private UserDAO userDAO = new UserDAO();
    private ResourceDAO resourceDAO = new ResourceDAO();
    private ProjectDAO projectDAO = new ProjectDAO();
    private UsageLogDAO usageLogDAO = new UsageLogDAO();

    public boolean createUser(User user) {

        if (user == null) {
            return false;
        }

        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        if (role == null || role.trim().isEmpty()) {
            return false;
        }

        if (!role.equals("ADMIN") && !role.equals("RESEARCHER")) {
            return false;
        }

        return userDAO.addUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User user) {

        if (user == null) {
            return false;
        }

        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        if (role == null || role.trim().isEmpty()) {
            return false;
        }

        if (!role.equals("ADMIN") && !role.equals("RESEARCHER")) {
            return false;
        }

        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {

        if (userId <= 0) {
            return false;
        }

        return userDAO.deleteUser(userId);
    }

    public boolean createResource(Resource resource) {

        if (resource == null) {
            return false;
        }

        String name = resource.getName();
        String type = resource.getType();
        String status = resource.getStatus();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (type == null || type.trim().isEmpty()) {
            return false;
        }

        if (status == null || (!status.equals("AVAILABLE")
                && !status.equals("IN_USE")
                && !status.equals("MAINTENANCE"))) {
            return false;
        }

        return resourceDAO.addResource(resource);
    }

    public List<Resource> getAllResources() {
        return resourceDAO.getAllResources();
    }

    public boolean updateResource(Resource resource) {

        if (resource == null) {
            return false;
        }

        String name = resource.getName();
        String type = resource.getType();
        String status = resource.getStatus();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (type == null || type.trim().isEmpty()) {
            return false;
        }

        if (status == null || (!status.equals("AVAILABLE")
                && !status.equals("IN_USE")
                && !status.equals("MAINTENANCE"))) {
            return false;
        }

        return resourceDAO.updateResource(resource);
    }

    public boolean deleteResource(int resourceId) {

        if (resourceId <= 0) {
            return false;
        }

        return resourceDAO.deleteResource(resourceId);
    }

    public boolean createProject(Project project, List<Integer> teamUserIds) {

        if (project == null) {
            return false;
        }

        String title = project.getTitle();
        String description = project.getDescription();
        String status = project.getStatus();

        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        if (description == null || description.trim().isEmpty()) {
            return false;
        }

        if (status == null || (!status.equals("ACTIVE")
                && !status.equals("COMPLETED")
                && !status.equals("ARCHIVED"))) {
            return false;
        }

        if (teamUserIds == null || teamUserIds.isEmpty()) {
            return false;
        }

        return projectDAO.addProjectWithTeam(project, teamUserIds);
    }

    public List<Project> getAllProjects() {
        return projectDAO.getAllProjects();
    }

    public boolean updateProject(Project project) {

        if (project == null) {
            return false;
        }

        String title = project.getTitle();
        String description = project.getDescription();
        String status = project.getStatus();

        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        if (description == null || description.trim().isEmpty()) {
            return false;
        }

        if (status == null || (!status.equals("ACTIVE")
                && !status.equals("COMPLETED")
                && !status.equals("ARCHIVED"))) {
            return false;
        }

        return projectDAO.updateProject(project);
    }

    public boolean deleteProject(int projectId) {

        if (projectId <= 0) {
            return false;
        }

        return projectDAO.deleteProject(projectId);
    }

    public List<UsageLog> getAllUsageLogs() {
        return usageLogDAO.getAllLogs();
    }

    public Map<Integer, Integer> getUsageCountByResource() {

        Map<Integer, Integer> usageCount = new HashMap<>();

        List<UsageLog> logs = usageLogDAO.getAllLogs();

        for (UsageLog log : logs) {

            int resourceId = log.getResourceID();

            if (usageCount.containsKey(resourceId)) {
                int currentCount = usageCount.get(resourceId);
                usageCount.put(resourceId, currentCount + 1);
            } else {
                usageCount.put(resourceId, 1);
            }
        }

        return usageCount;
    }
}