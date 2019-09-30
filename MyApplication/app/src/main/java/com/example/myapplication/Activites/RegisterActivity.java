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
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputEmail, InputPassword;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_name);
        InputEmail = (EditText) findViewById(R.id.register_email);
        InputPassword = (EditText) findViewById(R.id.register_password);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });


    }

    private void CreateAccount() {

        final String name = InputName.getText().toString().trim();
        final String email = InputEmail.getText().toString().trim();
        final String password = InputPassword.getText().toString().trim();



        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter your name .. ", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email address  .. ", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password .. ", Toast.LENGTH_SHORT).show();

        }
        else
        {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please wait, we are checking your details");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("email").setValue(email);
                    //    current_user_db.child("password").setValue(password);
                        progressDialog.dismiss();


                        Toast.makeText(RegisterActivity.this, "Congrats your account has been created ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);


                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Network Error, Try again later ..", Toast.LENGTH_SHORT).show();



                    }

                }
            });







        }


    }

    private void ValidateData(final String name, final String email, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(email).exists())) {

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("email", email);
                    userDataMap.put("password", password);
                    userDataMap.put("name", name);

                    RootRef.child("Users").child(email).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congrats your account has been created ", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();


                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error, Try again later ..", Toast.LENGTH_SHORT).show();



                                    }


                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "This" + email + " already exits", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please tru again using another email address", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
