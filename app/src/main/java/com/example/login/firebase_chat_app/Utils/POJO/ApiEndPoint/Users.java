package com.example.login.firebase_chat_app.Utils.POJO.ApiEndPoint;

import com.example.login.firebase_chat_app.Utils.POJO.MockUser;
import com.example.login.firebase_chat_app.Utils.POJO.RetroUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * This is the api endpoint declaration of the
 * getting the users from the https://jsonplaceholder.typicode.com/users
 * which provide the json of some users as mock data
 * This uses retrofit2 library
 */

public interface Users {

    @GET("users")
    Call<List<RetroUser>> listUsers();

}
