package com.example.myapplication.Activites;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

         Toolbar toolbar;
         final ProgressBar progressBar;
         final EditText userEmail;
         Button userPass;

        final FirebaseAuth firebaseAuth;

        toolbar = (Toolbar) findViewById(R.id.toolbarforgot);
        progressBar = (ProgressBar) findViewById(R.id.progressBarForgotPassword);
        userEmail = (EditText) findViewById(R.id.etUserEmail);
        userPass = (Button) findViewById(R.id.ChangePassword);

//toolbar.setTitle("Change Password");
       //toolbar.setTitle("Change password");


        firebaseAuth = FirebaseAuth.getInstance();


        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);

                        if(task.isSuccessful())
                        {

                            Toast.makeText(ForgotPasswordActivity.this, "Passoword sent to your email", Toast.LENGTH_LONG).show();
                            ForgotPasswordActivity.this.finish();

                        }else{
                            Toast.makeText(ForgotPasswordActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }




                    }
                });

            }
        });





    }
}
