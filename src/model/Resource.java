package model;

public class Resource {
    private int resourceID;
    private String name;
    private String type;
    private String status;
    private int createdBy;

    public Resource(int resourceID, String name, String type, String status, int createdBy){
        this.resourceID=resourceID;
        this.name=name;
        this.type=type;
        this.status=status;
        this.createdBy=createdBy;
    }

    public int getResourceID(){
        return resourceID;
    }
    public void setResourceID(int resourceID){
        this.resourceID=resourceID;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
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
