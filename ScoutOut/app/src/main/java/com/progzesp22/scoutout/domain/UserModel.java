package com.progzesp22.scoutout.domain;

import androidx.lifecycle.ViewModel;


public class UserModel extends ViewModel {
    public enum UserType {
        PLAYER,
        GM,
    }

    private UserType userType = UserType.PLAYER;

    private String username = "";


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }
}

