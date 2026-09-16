package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Project;
import util.DBConnection;


public class ProjectDAO {
    
    public boolean addProject(Project p){
        String sql = "INSERT INTO projects (title, description, status, created_by) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1,p.getTitle());
                ps.setString(2,p.getDescription());
                ps.setString(3,p.getStatus());
                ps.setInt(4,p.getCreatedBy());

                int rows = ps.executeUpdate();
                return rows > 0;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Project getProjectByID(int id){
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
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Project> getAllProjects(){
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Project p= new Project(
                        rs.getInt("project_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getInt("created_by")
                    );
                    projects.add(p);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            return projects;
    }

    public boolean updateProject(Project p){
        String sql = "UPDATE projects SET title = ?, description = ?, status = ? WHERE project_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1,p.getTitle());
                ps.setString(2,p.getDescription());
                ps.setString(3,p.getStatus());
                ps.setInt(4,p.getProjectID());

                int rows = ps.executeUpdate();
                return rows > 0;

        } catch(SQLException e){
            e.printStackTrace();
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

        } catch(SQLException e){
            e.printStackTrace();;
            return false;
        }
    }

    public boolean addProjectWithTeam(Project p, List<Integer> teamUserIdList){
        String insertProject = "INSERT INTO projects (title, description, status, created_by) VALUES (?, ?, ?, ?)";
        String insertTeam = "INSERT INTO project_team (project_id, user_id) VALUES (?, ?)";

        Connection conn= null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int projectID;
            try (PreparedStatement psProject = conn.prepareStatement(insertProject, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psProject.setString(1, p.getTitle());
            psProject.setString(2, p.getDescription());
            psProject.setString(3, p.getStatus());
            psProject.setInt(4, p.getCreatedBy());

            int rows = psProject.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return false;
            }

            try (ResultSet rs = psProject.getGeneratedKeys()) {
                if (rs.next()) {
                    projectID = rs.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }
        }
        try (PreparedStatement psTeam = conn.prepareStatement(insertTeam)) {
            for (Integer userId : teamUserIdList) {
                psTeam.setInt(1, projectID);
                psTeam.setInt(2, userId);
                psTeam.addBatch();
            }
            psTeam.executeBatch();
        }
        
        conn.commit();
        return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // restore default
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Integer> getTeamForProject(int projectId) {
        List<Integer> teamUserIdList = new ArrayList<>();
        String sql = "SELECT user_id FROM project_team WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, projectId);
    
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    teamUserIdList.add(rs.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teamUserIdList;
    }
}
