package service;

import java.util.List;

import dao.CollaborationDAO;
import dao.DatasetDAO;
import dao.ExperimentDAO;
import dao.MLModelDAO;
import model.Collaboration;
import model.Dataset;
import model.Experiment;
import model.MLModel;
import thread.ModelTrainingTask;

public class ResearcherService {

    private DatasetDAO datasetDAO = new DatasetDAO();
    private MLModelDAO mlModelDAO = new MLModelDAO();
    private ExperimentDAO experimentDAO = new ExperimentDAO();
    private CollaborationDAO collaborationDAO = new CollaborationDAO();

    public boolean uploadDataset(Dataset dataset) {

        if (dataset == null) {
            return false;
        }

        String name = dataset.getName();
        String filePath = dataset.getFilePath();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        return datasetDAO.addDataset(dataset);
    }

    public List<Dataset> getMyDatasets(int researcherId) {

        if (researcherId <= 0) {
            return null;
        }

        return datasetDAO.getDatasetsByResearcher(researcherId);
    }

    public boolean updateDataset(Dataset dataset) {

        if (dataset == null) {
            return false;
        }

        String name = dataset.getName();
        String filePath = dataset.getFilePath();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        return datasetDAO.updateDataset(dataset);
    }

    public boolean deleteDataset(int datasetId) {

        if (datasetId <= 0) {
            return false;
        }

        return datasetDAO.deleteDataset(datasetId);
    }

    public boolean startTraining(MLModel model) {

        if (model == null) {
            return false;
        }

        int datasetId = model.getDatasetID();

        if (datasetId <= 0) {
            return false;
        }

        Dataset dataset = datasetDAO.getDatasetByID(datasetId);

        if (dataset == null) {
            return false;
        }

        boolean added = mlModelDAO.addModel(model);

        if (!added) {
            return false;
        }

        new Thread(new ModelTrainingTask(model.getModelID())).start();

        return true;
    }

    public List<MLModel> getMyModels(int researcherId) {

        if (researcherId <= 0) {
            return null;
        }

        return mlModelDAO.getModelsByResearcher(researcherId);
    }

    public boolean logExperiment(Experiment experiment) {

        if (experiment == null) {
            return false;
        }

        int modelId = experiment.getModelID();

        if (modelId <= 0) {
            return false;
        }

        return experimentDAO.addExperiment(experiment);
    }

    public List<Experiment> getExperimentsForModel(int modelId) {

        if (modelId <= 0) {
            return null;
        }

        return experimentDAO.getExperimentsByModel(modelId);
    }

    public boolean requestCollaboration(Collaboration collaboration) {

        if (collaboration == null) {
            return false;
        }

        int projectId = collaboration.getProjectID();

        if (projectId <= 0) {
            return false;
        }

        if (collaboration.getStatus() == null
                || collaboration.getStatus().trim().isEmpty()) {
            collaboration.setStatus("PENDING");
        }

        return collaborationDAO.addCollaboration(collaboration);
    }

    public List<Collaboration> getCollaborationsForProject(int projectId) {

        if (projectId <= 0) {
            return null;
        }

        return collaborationDAO.getCollaborationsByProject(projectId);
    }

    public boolean updateCollaborationStatus(int collaborationId, String status) {

        if (collaborationId <= 0) {
            return false;
        }

        if (status == null || (!status.equals("PENDING")
                && !status.equals("ACTIVE")
                && !status.equals("CLOSED"))) {
            return false;
        }

        return collaborationDAO.updateStatus(collaborationId, status);
    }
}