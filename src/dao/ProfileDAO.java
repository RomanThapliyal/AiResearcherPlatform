package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Project;
import model.ProjectTeam;
import util.DBConnection;

public class ProfileDAO {

    public boolean addProject(Project p){
        String sql = "INSERT INTO projects (title, description, status, created_by) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1, p.getTitle());
                ps.setString(2, p.getDescription());
                ps.setString(3, p.getStatus());
                ps.setInt(4, p.getCreatedBy());

                int rows = ps.executeUpdate();
                return rows > 0;
        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Project getProjectById(int id) {
        String sql = "SELECT * FROM projects WHERE project_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new Project(
                        rs.getInt("project_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getInt("created_by")
                    );
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<Project> getAllProjects(){
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    Project p = new Project(
                        rs.getInt("project_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getInt("created_by")
                    );
                    projects.add(p);
                } 
        } catch (SQLException ex){
            ex.printStackTrace();
        }
            
        return projects;
    }

    public List<Project> getProjectsByUser(int userId){
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE created_by = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
                
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Project p = new Project(
                            rs.getInt("project_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getInt("created_by")
                        );
                        projects.add(p);
                    }
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return projects;
    }

    public boolean updateProject(Project p){
        String sql = "UPDATE projects SET title = ?, description = ?, status = ? WHERE project_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, p.getTitle());
                ps.setString(2, p.getDescription());
                ps.setString(3, p.getStatus());
                ps.setInt(4, p.getProjectID());
                
                int rows = ps.executeUpdate();
                return rows > 0;
                
        } catch (SQLException ex){ 
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteProject(int id){
        String sql = "DELETE FROM projects WHERE project_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                return rows > 0;
            } catch (SQLException ex){
                ex.printStackTrace();
                return false;
            }
    }

    /**
     * TRANSACTION MANAGEMENT DEMO
     * This method inserts a Project and multiple ProjectTeam members in a single atomic transaction.
     * If any part fails, the entire operation is rolled back.
     */
    public boolean addProjectWithTeam(Project p, List<ProjectTeam> teamMembers) {
        String projectSql = "INSERT INTO projects (title, description, status, created_by) VALUES (?, ?, ?, ?)";
        String teamSql = "INSERT INTO project_team (project_id, user_id) VALUES (?, ?)";
        
        Connection conn = null;
        PreparedStatement projectPs = null;
        PreparedStatement teamPs = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert the Project
            projectPs = conn.prepareStatement(projectSql, Statement.RETURN_GENERATED_KEYS);
            projectPs.setString(1, p.getTitle());
            projectPs.setString(2, p.getDescription());
            projectPs.setString(3, p.getStatus());
            projectPs.setInt(4, p.getCreatedBy());
            
            int affectedRows = projectPs.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating project failed, no rows affected.");
            }

            // Get the auto-generated project_id
            rs = projectPs.getGeneratedKeys();
            int generatedProjectId = 0;
            if (rs.next()) {
                generatedProjectId = rs.getInt(1);
            } else {
                throw new SQLException("Creating project failed, no ID obtained.");
            }

            // 2. Insert the Project Team Members
            teamPs = conn.prepareStatement(teamSql);
            for (ProjectTeam member : teamMembers) {
                teamPs.setInt(1, generatedProjectId);
                teamPs.setInt(2, member.getUserID());
                teamPs.addBatch(); // Add to batch for efficiency
            }
            teamPs.executeBatch();

            // 3. Commit the transaction
            conn.commit();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Rollback in case of error
            if (conn != null) {
                try {
                    System.out.println("Transaction is being rolled back...");
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            // Clean up resources and reset auto-commit
            try {
                if (rs != null) rs.close();
                if (projectPs != null) projectPs.close();
                if (teamPs != null) teamPs.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset to default
                    conn.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}