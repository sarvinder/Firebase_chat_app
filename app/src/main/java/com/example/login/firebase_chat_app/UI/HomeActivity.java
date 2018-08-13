package com.example.login.firebase_chat_app.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.firebase_chat_app.R;
import com.example.login.firebase_chat_app.Utils.POJO.Adapters.TestAdapter;
import com.example.login.firebase_chat_app.Utils.POJO.Message_Pojo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.Url;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    @BindView(R.id._dynamic)
    public ListView mMessageListView;

    public TestAdapter mMessageAdapter;

    @BindView(R.id.imagepicker)
    public ImageButton mPhotoPickerButton;

    @BindView(R.id.editText)
    public EditText mMessageEditText;

    @BindView(R.id.button)
    public Button mSendButton;

    //@BindView(R.id.sign_out)
    //public FloatingActionButton fab;
    @BindView(R.id.signout_card)
    public CardView signOutCard;

    private String mUsername = "anonomus";



    //lets add the firebase dependencies for the messenging service
    FirebaseDatabase database;
    DatabaseReference reference;
    ChildEventListener listener;

    //Firebase auth
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    private static final int RC_SIGN_IN=123;
    private static final int RC_PHOTO_PICKER = 456;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        // Initialize message ListView and its adapter
        List<Message_Pojo> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new TestAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        //firebase database to get the firebase reference to the messeging part the the DB
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("messages");

        //get the reference to the storage location to store the messages photos
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("chat_photos");

        auth = FirebaseAuth.getInstance();



        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send the message

                //lets first create the object
                Message_Pojo pojo = new Message_Pojo(mMessageEditText.getText().toString(),mUsername,null);
                reference.push().setValue(pojo);

                // Clear input box
                mMessageEditText.setText("");
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // already signed in
                    Toast.makeText(HomeActivity.this, "You are logged in .welcome to the chat app", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(firebaseAuth.getCurrentUser().getDisplayName());
                } else {
                    // not signed in
                    onSignOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.LoginTheme)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                            ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomeActivity.this, "you clicked the image button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"complete Action using ... "),RC_PHOTO_PICKER);
            }
        });
    }

    private void onSignOutCleanUp() {
        mUsername=ANONYMOUS;
        mMessageAdapter.clear();
        detachDataBaseReadListener();
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
        attachDatabaseReadListener();
    }


    private void attachDatabaseReadListener(){
        //listen to the firebase messages location for the messages
        if(listener == null) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    //in this the messages will be receaved
                    Message_Pojo got_message = dataSnapshot.getValue(Message_Pojo.class);
                    mMessageAdapter.add(got_message);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            //now lets attach the listener to the database reference
            reference.addChildEventListener(listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //IdpResponse response = IdpResponse.fromResultIntent(data);
        //Log.e("HomeActivity","the result code is  : "+resultCode);
        //Log.e("HomeAcitvity","the result code required is : "+RESULT_OK);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "User Signed In", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED){
                // Sign in failed
                Toast.makeText(this, "User Canceled The Sign In", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
                //got the photo
                //Toast.makeText(this, "got the photo", Toast.LENGTH_SHORT).show();
                Uri selectImageUri = data.getData();
                StorageReference photoRef = storageReference.child(selectImageUri.getLastPathSegment());
                photoRef.putFile(selectImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri imageUrl = taskSnapshot.getUploadSessionUri();
                        Message_Pojo pojo = new Message_Pojo(null,mUsername,imageUrl.toString());
                        reference.push().setValue(pojo);

                    }
                });
            }
        }

    }

    @OnClick(R.id.imagepicker)
    public void imagePicker(View view){
        //lets launch the image picker
        Toast.makeText(this, "lets start the image picker", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(intent,"complete Action using ... "),RC_PHOTO_PICKER);

    }

    private void detachDataBaseReadListener(){
        if(listener!=null) {
            reference.removeEventListener(listener);
            listener=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(authStateListener !=null){
            auth.removeAuthStateListener(authStateListener);
        }
        detachDataBaseReadListener();
        mMessageAdapter.clear();

    }


 /*   @OnClick(R.id.sign_out)
    public void signOut(View view){
        AuthUI.getInstance().signOut(this);
    }
*/
    @OnClick(R.id.settings)
    public void settingsClick(View view){
        //lets pop the signout card and call the signout method
        final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.chat_home);
        if(transitionsContainer !=null) {
            TransitionManager.beginDelayedTransition(transitionsContainer);
        }
        signOutCard.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.signout_card)
    public void signOutCardClick(View view){
        signOutCard.setVisibility(View.INVISIBLE);
        //signout();
    }

    public void signOut(){
        if(signOutCard.getVisibility() == View.VISIBLE) {
            AuthUI.getInstance().signOut(this);
            signOutCard.setVisibility(View.GONE);
        }
    }

}
