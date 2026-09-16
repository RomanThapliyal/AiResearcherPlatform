package model;

public class MLModels {
    private int modelID;
    private String name;
    private int datasetID;
    private String parameters;
    private String trainingStatus;
    private int progressPercentage;
    private int researcherID;

    public MLModels(int modelID, String name, int datasetID, String parameters, String trainingStatus, int progressPercentage, int researcherID){
        this.modelID=modelID;
        this.name=name;
        this.datasetID=datasetID;
        this.parameters=parameters;
        this.trainingStatus=trainingStatus;
        this.progressPercentage=progressPercentage;
        this.researcherID=researcherID;
    }

    public int getModelID(){
        return modelID;
    }
    public void setModelID(int modelID){
        this.modelID=modelID;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public int getDatasetID(){
        return datasetID;
    }
    public void setDatasetID(int datasetID){
        this.datasetID=datasetID;
    }

    public String getParameters(){
        return parameters;
    }
    public void setParameters(String parameters){
        this.parameters=parameters;
    }

    public String getTrainingStatus(){
        return trainingStatus;
    }
    public void setTrainingStatus(String trainingStatus){
        this.trainingStatus=trainingStatus;
    }

    public int getProgressPercentage(){
        return progressPercentage;
    }
    public void setProgressPercentage(int progressPercentage){
        this.progressPercentage=progressPercentage;
    }

    public int getReasearcherID(){
        return researcherID;
    }
    public void setReasearcherID(int researcherID){
        this.researcherID=researcherID;
    }
}
