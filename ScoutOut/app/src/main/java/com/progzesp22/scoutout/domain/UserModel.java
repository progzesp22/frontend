package com.progzesp22.scoutout.domain;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class UserModel extends ViewModel {
    public enum UserType {
        PLAYER,
        GM,
    }


    private MutableLiveData<UserType> userType = new MutableLiveData<>(UserType.PLAYER);

    private String username = "";


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserType(UserType userType) {
        this.userType.postValue(userType);
    }

    public MutableLiveData<UserType> getUserType() {
        return userType;
    }
}

