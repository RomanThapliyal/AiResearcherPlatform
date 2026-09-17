package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Experiment;
import util.DBConnection;

public class ExperimentDAO {

    public boolean addExperiment(Experiment e){
        String sql = "INSERT INTO experiments (model_id, parameters, result, performance_metric, researcher_id) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, e.getModelID());
                ps.setString(2, e.getParameters());
                ps.setString(3, e.getResults()); 
                ps.setString(4, e.getPerformanceMetric());
                ps.setInt(5, e.getResearcherID());

                int rows = ps.executeUpdate();
                return rows > 0;
        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Experiment getExperimentById(int id) {
        String sql = "SELECT * FROM experiments WHERE experiment_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    return new Experiment(
                        rs.getInt("experiment_id"),
                        rs.getInt("model_id"),
                        rs.getString("parameters"),
                        rs.getString("result"), 
                        rs.getString("performance_metric"),
                        rs.getInt("researcher_id")
                    );
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<Experiment> getAllExperiments(){
        List<Experiment> experiments = new ArrayList<>();
        String sql = "SELECT * FROM experiments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                
                while (rs.next()) {
                    Experiment e = new Experiment(
                        rs.getInt("experiment_id"),
                        rs.getInt("model_id"),
                        rs.getString("parameters"),
                        rs.getString("result"),
                        rs.getString("performance_metric"),
                        rs.getInt("researcher_id")
                    );
                    experiments.add(e);
                } 
        } catch (SQLException ex){
            ex.printStackTrace();
        }
            
        return experiments;
    }

    public List<Experiment> getExperimentsByModel(int modelId){
        List<Experiment> experiments = new ArrayList<>();
        String sql = "SELECT * FROM experiments WHERE model_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
                
                ps.setInt(1, modelId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Experiment e = new Experiment(
                            rs.getInt("experiment_id"),
                            rs.getInt("model_id"),
                            rs.getString("parameters"),
                            rs.getString("result"),
                            rs.getString("performance_metric"),
                            rs.getInt("researcher_id")
                        );
                        experiments.add(e);
                    }
                }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return experiments;
    }

    public boolean updateExperiment(Experiment e){
        String sql = "UPDATE experiments SET model_id = ?, parameters = ?, result = ?, performance_metric = ?, researcher_id = ? WHERE experiment_id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, e.getModelID());
                ps.setString(2, e.getParameters());
                ps.setString(3, e.getResults()); 
                ps.setString(4, e.getPerformanceMetric());
                ps.setInt(5, e.getResearcherID());
                ps.setInt(6, e.getExperimentID());
                
                int rows = ps.executeUpdate();
                return rows > 0;
                
        } catch (SQLException ex){ 
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteExperiment(int id){
        String sql = "DELETE FROM experiments WHERE experiment_id = ?";

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