package model;

public class Profile {
    private int profileID;
    private int userID;
    private String bio;
    private String contactInfo;

    public Profile(int profileID, int userID, String bio, String contactInfo){
        this.profileID=profileID;
        this.userID=userID;
        this.bio=bio;
        this.contactInfo=contactInfo;
    }

    public int getProfileID(){
        return profileID;
    }
    public void setProfileID(int profileID){
        this.profileID=profileID;
    }

    public int getUserID(){
        return userID;
    }
    public void setUserID(int userID){
        this.userID=userID;
    }

    public String getBio(){
        return bio;
    }
    public void setBio(String bio){
        this.bio=bio;
    }

    public String getContactInfo(){
        return contactInfo;
    }
    public void setContactInfo(String contactInfo){
        this.contactInfo=contactInfo;
    }
}
