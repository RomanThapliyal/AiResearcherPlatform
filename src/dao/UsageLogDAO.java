package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.UsageLog;
import util.DBConnection;

public class UsageLogDAO {

    public boolean addLog(UsageLog log){
        String sql = "INSERT INTO usage_logs (resource_id, user_id, usage_detail) VALUES (?, ?, ?)";

        try(Connection conn =DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, log.getResourceID());
                ps.setInt(2, log.getUserID());
                ps.setString(3, log.getUsageDetail());

                int rows = ps.executeUpdate();
                return rows > 0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public List<UsageLog> getAllLogs(){
        List<UsageLog> usageLog = new ArrayList<>();
        String sql = "SELECT * FROM usage_logs";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    UsageLog log = new UsageLog(
                        rs.getInt("log_id"),
                        rs.getInt("resource_id"),
                        rs.getInt("user_id"),
                        rs.getString("usage_detail"),
                        rs.getString("logged_at")
                    );
                    usageLog.add(log);
                }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return usageLog;
    }

    public List<UsageLog> getLogsByResourceID(int resourceId){
        List<UsageLog> usageLog = new ArrayList<>();
        String sql = "SELECT * FROM usage_logs WHERE resource_id = ?";
        
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1,resourceId);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    UsageLog log = new UsageLog(
                        rs.getInt("log_id"),
                        rs.getInt("resource_id"),
                        rs.getInt("user_id"),
                        rs.getString("usage_detail"),
                        rs.getString("logged_at")
                    );
                    usageLog.add(log);
                }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return usageLog;
    }

    public List<UsageLog> getLogsByUser(int userId){
        List<UsageLog> usageLog = new ArrayList<>();
        String sql = "SELECT * FROM usage_logs WHERE user_id = ?";
        
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1,userId);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    UsageLog log = new UsageLog(
                        rs.getInt("log_id"),
                        rs.getInt("resource_id"),
                        rs.getInt("user_id"),
                        rs.getString("usage_detail"),
                        rs.getString("logged_at")
                    );
                    usageLog.add(log);
                }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return usageLog;
    }
    
}
