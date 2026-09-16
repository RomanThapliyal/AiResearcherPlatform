package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Resource;
import util.DBConnection;

public class ResourceDAO {
    
    public boolean addResource(Resource r){
        String sql = "INSERT INTO resources (name ,type ,status ,created_by) VALUES (?, ?, ?, ?)";
        try(Connection conn =DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

                ps.setString(1,r.getName());
                ps.setString(2,r.getType());
                ps.setString(3,r.getStatus());
                ps.setInt(4,r.getCreatedBy());

                int row = ps.executeUpdate();
                return row > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Resource getResourceByID(int id){
        String sql = "SELECT * FROM resources WHERE resource_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1,id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new Resource(
                        rs.getInt("resource_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("created_by")
                    );
                }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Resource> getAllResources(){
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Resource r = new Resource(
                        rs.getInt("resource_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("created_by")
                    );
                    resources.add(r);
                }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return resources;
    }

    public boolean updateResource(Resource r){
        String sql = "UPDATE resources SET name = ?, type = ?, status = ? WHERE resource_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1,r.getName());
                ps.setString(2,r.getType());
                ps.setString(3,r.getStatus());
                ps.setInt(4, r.getResourceID());

                int row = ps.executeUpdate();
                return row > 0;
                
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteResource(int id){
        String sql = "DELETE FROM resources WHERE resource_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1,id);
                int row = ps.executeUpdate();
                return row > 0;

        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
