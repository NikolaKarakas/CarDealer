package com.example.myapplication.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.example.myapplication.util.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoginActivity extends AppCompatActivity {

    private EditText InputEmail, InputPassword;
    private Button LoginButton;
    private ProgressDialog progressDialog;

    private TextView AdminLink, NotAdminLink;


    private String parentDbName = "Users";
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        LoginButton = (Button) findViewById(R.id.login_btn);
        InputEmail = (EditText) findViewById(R.id.login_id);
        InputPassword = (EditText) findViewById(R.id.login_password);

        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);



        initImageLoader();

        progressDialog = new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });


        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Admins");






            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            }
        });




    }

    private void LoginUser() {

        String email = InputEmail.getText().toString().trim();
        String password = InputPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email address  .. ", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password .. ", Toast.LENGTH_SHORT).show();

        }

        else
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.setTitle("Login Account");
                        progressDialog.setMessage("Please Wait we are checking the cridentials");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        checkUserExitst();


                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Error Login, Try again later ..", Toast.LENGTH_SHORT).show();



                    }
                }
            });
        }



    }

    private void checkUserExitst()
    {
        final String user_id =mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild(user_id))
            {


                  // userData = dataSnapshot.child(parentDbName).child(user_id).getValue(Users.class);
             //   String name = dataSnapshot.child(parentDbName).child(user_id).child("name").getValue();

                if(parentDbName.equals("Admins"))
                {


                    Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);


                }
                else if(parentDbName.equals("Users"))
                {

                    progressDialog.dismiss();
                    Users userData = dataSnapshot.child(user_id).getValue(Users.class);
                    userData.setUser_id(user_id);
                   // Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
              //      if(userData.getEmail().equals("test@test.com"))
                //    {
                        Intent mainIntent = new Intent(LoginActivity.this, Home_UserActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                 //   }
                    Prevalent.currentOnlineUser = userData;

                    startActivity(mainIntent);

                    //String nameUser = userData.getName();
                    //Prevalent.UserName = nameUser;


                }


            }
            else {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "You need to setup your account", Toast.LENGTH_SHORT).show();



            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initImageLoader(){
        UniversalImageLoader imageLoader = new UniversalImageLoader(LoginActivity.this);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }
}
