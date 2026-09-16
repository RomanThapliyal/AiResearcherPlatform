package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Dataset;
import util.DBConnection;

public class DatasetDAO {

    public boolean addDataset(Dataset d){
        String sql = "INSERT INTO datasets (name, description, file_path, uploaded_by) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setString(1, d.getName());
                ps.setString(2, d.getDescription());
                ps.setString(3, d.getFilePath());
                ps.setInt(4, d.getUploadedBy());

                int rows = ps.executeUpdate();
                return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Dataset getDatasetByID(int id) {
        String sql = "SELECT * FROM datasets WHERE dataset_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new Dataset(
                        rs.getInt("dataset_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("file_path"),
                        rs.getInt("uploaded_by")
                    );
                }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Dataset> getAllDatasets(){
        List<Dataset> datasets = new ArrayList<>();
        String  sql = "SELECT * FROM datasets";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    Dataset d = new Dataset(
                        rs.getInt("dataset_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("file_path"),
                        rs.getInt("uploaded_by")
                    );
                    datasets.add(d);
                } 
        } catch (SQLException e){
            e.printStackTrace();
        }
            
        return datasets;
    }

    public List<Dataset> getDatasetsByResearcher(int uploadedBy){
        List<Dataset> datasets = new ArrayList<>();
        String  sql = "SELECT * FROM datasets where uploaded_by = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    Dataset d = new Dataset(
                        rs.getInt("dataset_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("file_path"),
                        rs.getInt("uploaded_by")
                    );
                    datasets.add(d);
                }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return datasets;
    }

    public boolean updateDataset(Dataset d){
        String sql = "UPDATE datasets SET name = ?, description = ?, file_path = ? WHERE dataset_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setString(1, d.getName());
                ps.setString(2, d.getDescription());
                ps.setString(3, d.getFilePath());
                ps.setInt(4, d.getDatasetID());

                int rows = ps.executeUpdate();
                return rows > 0;
                
        } catch (SQLException e){ 
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDataset(int id){
        String sql = "DELETE FROM datasets WHERE dataset_id = ?";

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