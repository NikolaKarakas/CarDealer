package com.example.myapplication.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {


    Button forgotPass;
    TextView close;
    TextView update;
    private EditText fullNameEditText;
    private CircleImageView profileImageView;
    private EditText addressEditText;
    private TextView profileChangeTextBtn;

    private Uri imageUri;
    private String myUrl= "";
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private StorageTask uploadTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        forgotPass = findViewById(R.id.profile_forgotPassword_Btn);
        close = (TextView) findViewById(R.id.close_settings);
        update = (TextView) findViewById(R.id.update_account_settings_btn);
        fullNameEditText = (EditText) findViewById(R.id.settings_profile_name);
        profileImageView  = (CircleImageView) findViewById(R.id.settings_profile_image);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");


        userInfoDisplay(profileImageView,fullNameEditText);



        close.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         finish();
                                     }
                                 });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( checker.equals("clicked"))
                {
                    userInfoSaved();

                }
                else{
                    updateOnlyUserInfo();

                }

            }
        });



                forgotPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SettingsActivity.this, ForgotPasswordActivity.class);
                        startActivity(intent);

                    }
                });

                profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checker = "clicked";

                        CropImage.activity(imageUri)
                                .setAspectRatio(1,1)
                                .start(SettingsActivity.this);
                    }
                });

    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEditText.getText().toString());
        //userMap.put("address", addressEditText.getText().toString());

        ref.child(Prevalent.currentOnlineUser.getUser_id()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, Home_UserActivity.class));
        Toast.makeText(SettingsActivity.this, "User Info Updated Succesfully ", Toast.LENGTH_LONG).show();
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);

        }
        else{
            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }

    }

    private void userInfoSaved() {

        if(TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is empty", Toast.LENGTH_LONG).show();
            
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait we are updating your account information");

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null)
        {
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getUser_id()+ ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful() )
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();

                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", fullNameEditText.getText().toString());
                      //  userMap.put("address", addressEditText.getText().toString());
                        userMap.put("image", myUrl);


                        ref.child(Prevalent.currentOnlineUser.getUser_id()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, Home_UserActivity.class));
                        Toast.makeText(SettingsActivity.this, "User Info Updated Succesfully ", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                      Toast.makeText(SettingsActivity.this, "Error, Try again later ", Toast.LENGTH_LONG).show();
                      //  finish();
                    }




                }
            });

        }
        else{
            progressDialog.dismiss();
            Toast.makeText(SettingsActivity.this, "Image is not Selected", Toast.LENGTH_LONG).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText) {

        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUser_id());


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                //        Toast.makeText(SettingsActivity.this, "Sdadas", Toast.LENGTH_LONG).show();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
