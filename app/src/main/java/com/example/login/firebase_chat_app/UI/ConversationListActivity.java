
package com.example.login.firebase_chat_app.UI;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.login.firebase_chat_app.R;
import com.example.login.firebase_chat_app.Utils.POJO.Adapters.FirendsListAdapter;
import com.example.login.firebase_chat_app.Utils.POJO.ApiEndPoint.Users;
import com.example.login.firebase_chat_app.Utils.POJO.MockUser;
import com.example.login.firebase_chat_app.Utils.POJO.RetroUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConversationListActivity extends AppCompatActivity {

    private static String USERNAME = "Linda";
    private static final String BASEURL = "https://jsonplaceholder.typicode.com/";
    private LottieAnimationView animationView;

    @BindView(R.id.friends_list_viewer)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    FirendsListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(USERNAME);
        animationView = findViewById(R.id.loader);
        //Display the users
        displayUsers();


    }

    private void displayUsers(){
        //get the data from jons server
        getJsonDataAndUpdateUI();

    }

    private void getJsonDataAndUpdateUI() {
        //lets get the data
        startLoader();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Users service = retrofit.create(Users.class);
        //making the call async to the api
        Call<List<RetroUser>> repos = service.listUsers();
        repos.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {
                //Toast.makeText(ConversationListActivity.this, "got the data", Toast.LENGTH_LONG).show();
                List<RetroUser> users = response.body();
                loadList(users);
                hideLoader();
            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {
                Toast.makeText(ConversationListActivity.this, "did not get the data : "+t, Toast.LENGTH_LONG).show();
                hideLoader();
            }
        });

    }

    private void loadList(List<RetroUser>users) {
        //initialize recycler view
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter=new FirendsListAdapter(this,users);
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.VISIBLE);


    }

    private void hideLoader() {
        //hide the loader
        if(animationView.isAnimating()){
            //stop animation
            animationView.cancelAnimation();
        }
        animationView.setVisibility(View.GONE);

    }

    private void startLoader() {
        //show the loader
        animationView.playAnimation();

    }


    private void liveUsersDisplay(){

    }

}
