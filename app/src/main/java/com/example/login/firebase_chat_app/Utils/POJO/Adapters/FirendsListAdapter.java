package com.example.login.firebase_chat_app.Utils.POJO.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.firebase_chat_app.R;
import com.example.login.firebase_chat_app.Utils.POJO.RetroUser;

import java.util.List;

public class FirendsListAdapter  extends RecyclerView.Adapter<FirendsListAdapter.MyViewHolder>{


    Context context;
    List<RetroUser> users;
    public FirendsListAdapter(Context context,List<RetroUser> users){
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutIdForListItem = R.layout.friends_list_row_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    /**
     * make a custom view holde
     */
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView  userName;
        TextView  userMessage;

        public MyViewHolder(View itemView) {
            super(itemView);
            //initialize
            imageView   = itemView.findViewById(R.id.item_user_image);
            userName    = itemView.findViewById(R.id.item_user_name);
            userMessage = itemView.findViewById(R.id.item_user_message);
        }

        public void onBind(int index){
            //bind the image
            drawCurcularImage(imageView,R.drawable.facebook);
            userName.setText(users.get(index).getUserName());
            userMessage.setText(Integer.toString(users.get(index).getId()));
        }

        private void drawCurcularImage(ImageView imageView, @DrawableRes int image)
        {
            //This draws the circular image by setting the image drawable to rounded bitmap drawable
            //do this if the drawable is NOT the vector drawable because it needs BITMAPFACTORY
            Resources res = context.getResources();
            if(res !=null) {
                Bitmap src = BitmapFactory.decodeResource(res, image);
                if(src !=null) {
                    RoundedBitmapDrawable dr =
                            RoundedBitmapDrawableFactory.create(res, src);
                    if(dr !=null) {
                        dr.setCircular(true);
                        imageView.setImageDrawable(dr);
                    }
                }
            }
        }
    }
}
