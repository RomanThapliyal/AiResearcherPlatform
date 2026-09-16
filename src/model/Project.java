package model;

public class Project {
    private int projectID;
    private String title;
    private String description;
    private String status;
    private int createdBy;

    public Project(int projectID,String title, String description, String status, int createdBy){
        this.projectID=projectID;
        this.title=title;
        this.description=description;
        this.status=status;
        this.createdBy=createdBy;
    }

    public int getProjectID(){
        return projectID;
    }
    public void setProjectID(int projectID){
        this.projectID=projectID;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }

    public int getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(int createdBy){
        this.createdBy=createdBy;
    }
}
