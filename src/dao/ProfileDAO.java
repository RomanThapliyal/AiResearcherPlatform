package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Profile;
import util.DBConnection;

public class ProfileDAO {

    public boolean addProfile(Profile p) {
        String sql = "INSERT INTO profiles (user_id, bio, contact_info) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getUserID());
            ps.setString(2, p.getBio());
            ps.setString(3, p.getContactInfo());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Profile getProfileByUserID(int userID) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Profile(
                    rs.getInt("profile_id"),
                    rs.getInt("user_id"),
                    rs.getString("bio"),
                    rs.getString("contact_info")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProfile(Profile p) {
        String sql = "UPDATE profiles SET bio = ?, contact_info = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getBio());
            ps.setString(2, p.getContactInfo());
            ps.setInt(3, p.getUserID());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}