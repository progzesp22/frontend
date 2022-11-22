package com.example.myapplication;
import androidx.lifecycle.ViewModel;


public class UserModel extends ViewModel {
    public enum UserType{
        PLAYER,
        GM,
    }

    public enum MasterMode{
        EDIT,
        ACCEPT,
        INVITE
    }


    private UserType userType = UserType.PLAYER;
    private MasterMode masterMode = MasterMode.EDIT;

    private String username = "default";


    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setMasterMode(MasterMode masterMode) {
        this.masterMode = masterMode;
    }

    public MasterMode getMasterMode() {
        return masterMode;
    }
}

