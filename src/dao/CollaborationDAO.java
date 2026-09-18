package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Collaboration;
import util.DBConnection;

public class CollaborationDAO {

    public boolean addCollaboration(Collaboration c){
        String sql = "INSERT INTO collaborations (project_id, initiated_by, status) VALUES (?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, c.getProjectID());
                ps.setInt(2, c.getInitiatedBy());
                ps.setString(3, c.getStatus());

                int rows = ps.executeUpdate();
                return rows > 0;
        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Collaboration getCollaborationById(int id) {
        String sql = "SELECT * FROM collaborations WHERE collaboration_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new Collaboration(
                        rs.getInt("collaboration_id"),
                        rs.getInt("project_id"),
                        rs.getInt("initiated_by"),
                        rs.getString("status")
                    );
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<Collaboration> getAllCollaborations(){
        List<Collaboration> collaborations = new ArrayList<>();
        String sql = "SELECT * FROM collaborations";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    Collaboration c = new Collaboration(
                        rs.getInt("collaboration_id"),
                        rs.getInt("project_id"),
                        rs.getInt("initiated_by"),
                        rs.getString("status")
                    );
                    collaborations.add(c);
                } 
        } catch (SQLException ex){
            ex.printStackTrace();
        }
            
        return collaborations;
    }

    public List<Collaboration> getCollaborationsByProject(int projectId){
        List<Collaboration> collaborations = new ArrayList<>();
        String sql = "SELECT * FROM collaborations WHERE project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
                
                ps.setInt(1, projectId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Collaboration c = new Collaboration(
                            rs.getInt("collaboration_id"),
                            rs.getInt("project_id"),
                            rs.getInt("initiated_by"),
                            rs.getString("status")
                        );
                        collaborations.add(c);
                    }
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return collaborations;
    }

    public boolean updateStatus(int id, String status){
        String sql = "UPDATE collaborations SET status = ? WHERE collaboration_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, status);
                ps.setInt(2, id);
                
                int rows = ps.executeUpdate();
                return rows > 0;
                
        } catch (SQLException ex){ 
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteCollaboration(int id){
        String sql = "DELETE FROM collaborations WHERE collaboration_id = ?";

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
}