
package com.example.login.firebase_chat_app.UI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.example.login.firebase_chat_app.R;
import com.example.login.firebase_chat_app.Utils.POJO.MockUser;

import java.util.ArrayList;
import java.util.List;

public class ConversationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Linda");

        //Display the users
       // displayUsers();


        //draw the circular image for the test
        ImageView imageView = findViewById(R.id.user_image);
        drawCurcularImage(imageView,R.drawable.facebook);
    }


    private void drawCurcularImage(ImageView imageView, @DrawableRes int image)
    {
        //This draws the circular image by setting the image drawable to rounded bitmap drawable
        //do this if the drawable is NOT the vector drawable because it needs BITMAPFACTORY
        Resources res = getResources();
        if(res !=null) {
            Log.e("COnversationList","The ress is not null");
            Bitmap src = BitmapFactory.decodeResource(res, image);
            if(src !=null) {
                Log.e("COnversationList","The src is not null");

                RoundedBitmapDrawable dr =
                        RoundedBitmapDrawableFactory.create(res, src);
                if(dr !=null) {

                    Log.e("COnversationList","The dr is not null");
                    dr.setCircular(true);
                    imageView.setImageDrawable(dr);
                }
            }
        }
    }

    private void displayUsers(){

        //mockdata display
        mockUsersDisplay();

        //live users display
        liveUsersDisplay();


    }

    private void mockUsersDisplay(){
        //mock user data
        MockUser user = new MockUser();
        List<MockUser> myUsers=new ArrayList<>();
        myUsers=user.getMockUsers();
        for (int i=0;i<10;i++){

        }

    }

    private void liveUsersDisplay(){

    }
}
