package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import util.DBConnection;

public class UserDAO {

    public boolean addUser(User u){
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setString(1, u.getName());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setString(4, u.getRole());

                int rows = ps.executeUpdate();
                return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByID(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email){
        String sql = "SELECT * FROM users WHERE email = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setString(1, email);
                ResultSet rs=ps.executeQuery();

                if(rs.next()){
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                } 
            }catch (SQLException e){
                    e.printStackTrace();
             }
               
             return null;   
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String  sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    User u = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                    users.add(u);
                } 
        } catch (SQLException e){
            e.printStackTrace();
        }
            
        return users;
    }

    public boolean updateUser(User u){
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE user_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1, u.getName());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setString(4,u.getRole());
                ps.setInt(5, u.getUserID());

                int rows = ps.executeUpdate();
                return rows > 0;
                
        } catch (SQLException e){ 
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id){
        String sql = "DELETE FROM users WHERE user_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);){

                ps.setInt(1,id);
                int rows=ps.executeUpdate();
                return rows > 0;
            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
    }
}
