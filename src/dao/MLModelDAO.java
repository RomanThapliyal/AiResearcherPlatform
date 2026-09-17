package dao;

import  java.sql.Connection;
import  java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import  java.util.ArrayList;
import java.util.List;

import model.MLModel;
import util.DBConnection;

public class MLModelDAO {

    public boolean addModel(MLModel m){
        String sql = "INSERT INTO models (name, dataset_id, parameters, training_status, progress_percent, researcher_id) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setString(1, m.getName());
                ps.setInt(2, m.getDatasetID());
                ps.setString(3, m.getParameters());
                ps.setString(4, m.getTrainingStatus());
                ps.setInt(5, m.getProgressPercentage());
                ps.setInt(6, m.getResearcherID());

                int rows = ps.executeUpdate();
                return rows > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public MLModel getModelByID(int id) {
        String sql = "SELECT * FROM models WHERE model_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new MLModel(
                        rs.getInt("model_id"),
                        rs.getString("name"),
                        rs.getInt("dataset_id"),
                        rs.getString("parameters"),
                        rs.getString("training_status"),
                        rs.getInt("progress_percent"),
                        rs.getInt("researcher_id")
                    );
                }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<MLModel> getAllModels(){
        List<MLModel> models = new ArrayList<>();
        String  sql = "SELECT * FROM models";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    MLModel m = new MLModel(
                        rs.getInt("model_id"),
                        rs.getString("name"),
                        rs.getInt("dataset_id"),
                        rs.getString("parameters"),
                        rs.getString("training_status"),
                        rs.getInt("progress_percent"),
                        rs.getInt("researcher_id")
                    );
                    models.add(m);
                } 
        } catch (SQLException e){
            e.printStackTrace();
        }
            
        return models;
    }

    public List<MLModel> getModelsByResearcher(int researcherID){
        List<MLModel> models = new ArrayList<>();
        String  sql = "SELECT * FROM models where researcher_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    MLModel m = new MLModel(
                        rs.getInt("model_id"),
                        rs.getString("name"),
                        rs.getInt("dataset_id"),
                        rs.getString("parameters"),
                        rs.getString("training_status"),
                        rs.getInt("progress_percent"),
                        rs.getInt("researcher_id")
                    );
                    models.add(m);
                }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return models;
    }

    public boolean updateModel(MLModel m){
        String sql = "UPDATE models SET name = ?, dataset_id = ?, parameters = ? WHERE model_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, m.getName());
                ps.setInt(2, m.getDatasetID());
                ps.setString(3, m.getParameters());
                ps.setInt(4, m.getModelID());
                
                int rows = ps.executeUpdate();
                return rows > 0;
                
        } catch (SQLException e){ 
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProgress(int modelID, int progressPercentage, String trainingStatus){
        String sql = "UPDATE models SET progress_percent = ?, training_status = ? WHERE model_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, progressPercentage);
                ps.setString(2, trainingStatus);
                ps.setInt(3, modelID);

                int rows = ps.executeUpdate();
                return rows > 0;

        } catch (SQLException e){ 
            e.printStackTrace();
            return false;
        } 
    }

    public boolean deleteModel(int id){
        String sql = "DELETE FROM models WHERE model_id = ?";

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