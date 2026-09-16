package model;

public class Experiment {
    private int experimentID;
    private int modelID;
    private String parameters;
    private String result;
    private String performanceMetric;
    private int researcherID;

    public Experiment(int experimentID, int modelID, String parameters, String result, String performanceMetric, int researcherID){
        this.experimentID=experimentID;
        this.modelID=modelID;
        this.parameters=parameters;
        this.result=result;
        this.performanceMetric=performanceMetric;
        this.researcherID=researcherID;
    }

    public int getExperimentID(){
        return experimentID;
    }
    public void setExperimentID(int experimentID){
        this.experimentID=experimentID;
    }

    public int getModelID(){
        return modelID;
    }
    public void setModelID(int modelID){
        this.modelID=modelID;
    }

    public String getParameters(){
        return parameters;
    }
    public void setParameters(String parameters){
        this.parameters=parameters;
    }

    public String getResults(){
        return result;
    }
    public void setResult(String result){
        this.result=result;
    }

    public String getPerformanceMetric(){
        return performanceMetric;
    }
    public void setPerformanceMetric(String performanceMetric){
        this.performanceMetric=performanceMetric;
    }

    public int getResearcherID(){
        return researcherID;
    }
    public void setResearcherID(int researcherID){
        this.researcherID=researcherID;
    }

}
