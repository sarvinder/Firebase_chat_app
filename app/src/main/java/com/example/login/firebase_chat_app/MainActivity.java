package com.example.login.firebase_chat_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.login.firebase_chat_app.UI.HomeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }


    @OnClick(R.id.launch)
    public void launchMessenger(View view){

        startActivity(new Intent(this, HomeActivity.class));

    }

}
