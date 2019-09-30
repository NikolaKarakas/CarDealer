package com.example.myapplication.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Car;
import com.example.myapplication.R;
import com.example.myapplication.util.UniversalImageLoader;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.text.TextUtils.isEmpty;

public class VehicleActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private static final int GALLERY_REQUEST =1;

    private Button mSubmit;

    private ProgressDialog mProgress;

    private static final String TAG = VehicleActivity.class.getSimpleName();

   private EditText mBrand;
    private EditText mModel;
    private EditText mYear;
    private EditText mMileage;
    private EditText mFuel;
    private EditText mDescription;
    private EditText mPrice;

    private  Uri mimgeUri = null;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    String downladImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);


         mStorage = FirebaseStorage.getInstance().getReference();
         mDatabase = FirebaseDatabase.getInstance().getReference().child("Cars");

         mSelectImage =(ImageButton) findViewById(R.id.addImage);
         mBrand = findViewById(R.id.tBrand);
         mModel = findViewById(R.id.Model);
         mYear = findViewById(R.id.Year);
         mMileage = findViewById(R.id.Mileage);
         mFuel = findViewById(R.id.Fuel);
         mMileage = findViewById(R.id.Mileage);
         mDescription = findViewById(R.id.Description);
         mPrice = findViewById(R.id.Price);




         mSubmit = (Button) findViewById(R.id.addVehicle);

         mProgress = new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }


    private  void check()
    {
        Log.d(TAG, "onClick: attempting to post...");
        if(!isEmpty(mBrand.getText().toString())
                && !isEmpty(mModel.getText().toString())
                && !isEmpty(mYear.getText().toString())
                && !isEmpty(mMileage.getText().toString())
                && !isEmpty(mFuel.getText().toString())
                && !isEmpty(mPrice.getText().toString())){
                startAdding();
        }

    }
    private void startAdding() {



        if (mimgeUri != null) {
            mProgress.setMessage("Uploading to Database .. ");
            mProgress.show();
            final StorageReference filepath = mStorage.child("Vehicle_Images").child(mimgeUri.getLastPathSegment());

            final UploadTask uploadTask = filepath.putFile(mimgeUri);




            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(VehicleActivity.this, "Error: Uploading  ", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(VehicleActivity.this, "Image Uploaded: ", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            downladImageUrl = filepath.getDownloadUrl().toString();
                            mProgress.dismiss();
                            startActivity(new Intent(VehicleActivity.this, MainActivity.class));
                            return filepath.getDownloadUrl();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                       Toast.makeText(VehicleActivity.this, "Success" ,Toast.LENGTH_SHORT).show();

                            final String postId = FirebaseDatabase.getInstance().getReference().push().getKey();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                            Car car = new Car();
                            car.setBrand(mBrand.getText().toString());
                            car.setModel(mModel.getText().toString());
                            car.setYear(mYear.getText().toString());
                            car.setMileage(mMileage.getText().toString());
                            car.setFuel(mFuel.getText().toString());
                            car.setDescription(mDescription.getText().toString());
                            car.setPrice(mPrice.getText().toString());
                            car.setImage(task.getResult().toString());
                            car.setAdminId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            car.setPost_id(postId);

                            reference.child(getString(R.string.node_cars))
                                    .child(postId)
                                    .setValue(car);
                            resetFields();

                            Intent intent = new Intent(VehicleActivity.this, HomeActivity.class);
                            startActivity(intent);



                        }
                    });

                }
            });





        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
           mimgeUri = data.getData();
           mSelectImage.setImageURI(mimgeUri);
        }


    }

    private void resetFields(){
        UniversalImageLoader.setImage("", mSelectImage);
        mBrand.setText("");
        mModel.setText("");
        mMileage.setText("");
        mYear.setText("");
        mFuel.setText("");
        mPrice.setText("");
    }
}
