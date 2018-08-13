package com.example.login.firebase_chat_app.Utils.POJO;

public class Message_Pojo {

    private String text;
    private String name;
    private String photoUrl;

    public Message_Pojo(){

    }
    public Message_Pojo(String text,String name,String photoUrl){
            this.name=name;
            this.text = text;
            this.photoUrl = photoUrl;

    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
