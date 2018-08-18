package com.example.login.firebase_chat_app.Utils.POJO;

import android.support.annotation.DrawableRes;

import com.example.login.firebase_chat_app.R;

import java.util.List;

public class MockUser {

    private String userName;

    @DrawableRes
    private int userImage;

    private List<MockUser> users;

    public MockUser(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public  List<MockUser> getMockUsers(){

        //prepaire the 10 mock users
        for (int i =0;i<10;i++){
           users.add(getUser());
        }
        return users;
    }

    private MockUser getUser (){

        MockUser user = new MockUser();
        user.setUserName("Linda");
        user.setUserImage(R.drawable.male_user);

        return user;
    }

}
