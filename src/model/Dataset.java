package model;

public class Dataset {
    private int datasetID;
    private String name;
    private String description;
    private String filePath;
    private int uploadedBy;

    public Dataset(int datasetID, String name, String description, String filePath, int uploadedBy){
        this.datasetID=datasetID;
        this.name=name;
        this.description=description;
        this.filePath=filePath;
        this.uploadedBy=uploadedBy;
    }

    public int getDatasetID(){
        return datasetID;
    }
    public void setDatasetID(int datasetID){
        this.datasetID=datasetID;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public String getFilePath(){
        return filePath;
    }
    public void setFilePath(String filePath){
        this.filePath=filePath;
    }

    public int getUploadedBy(){
        return uploadedBy;
    }
    public void setUploadedBy(int uploadedBy){
        this.uploadedBy=uploadedBy;
    }
}
