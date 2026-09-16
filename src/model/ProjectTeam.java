package model;

public class ProjectTeam {
    private int projectID;
    private int userID;

    public ProjectTeam(int projectID, int userID){
        this.projectID=projectID;
        this.userID=userID;
    }

    public int getProjectID(){
        return projectID;
    }
    public void setProjectID(int projectID){
        this.projectID=projectID;
    }

    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID=userID;
    }
}
