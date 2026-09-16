package model;

public class Collaboration {
    private int collaborationID;
    private int projectID;
    private int initiatedBy;
    private String status;

    public Collaboration(int collaborationID, int projectID, int initiatedBy, String status){
        this.collaborationID=collaborationID;
        this.projectID=projectID;
        this.initiatedBy=initiatedBy;
        this.status=status;
    }

    public int getCollaborationID(){
        return collaborationID;
    }
    public void setCollaborationID(int collaborationID){
        this.collaborationID=collaborationID;
    }

    public int getProjectID(){
        return projectID;
    }
    public void setProjectID(int projectID){
        this.projectID=projectID;
    }

    public int getInitiatedBy(){
        return initiatedBy;
    }
    public void setInitiatedBy(int initiatedBy){
        this.initiatedBy=initiatedBy;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
