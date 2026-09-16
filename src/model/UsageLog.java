package model;

public class UsageLog {
    private int logID;
    private int resourceID;
    private int userID;
    private String usageDetail;
    private String loggedAt;

    public UsageLog(int logID, int resourceID, int userID, String usageDetail, String loggedAt){
        this.logID=logID;
        this.resourceID=resourceID;
        this.userID=userID;
        this.usageDetail=usageDetail;
        this.loggedAt=loggedAt;
    }

    public int getLogID(){
        return logID;
    }
    public void setLogID(int logID){
        this.logID=logID;
    }

    public int getResourceID(){
        return resourceID;
    }
    public void setResourceID(int resourceID){
        this.resourceID=resourceID;
    }

    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID=userID;
    }

    public String getUsageDetail(){
        return usageDetail;
    }
    public void setUsageDetail(String usageDetail){
        this.usageDetail=usageDetail;
    }

    public String getLoggedAt(){
        return loggedAt;
    }
    public void setLoggedAt(String loggedAt){
        this.loggedAt=loggedAt;
    }
}
