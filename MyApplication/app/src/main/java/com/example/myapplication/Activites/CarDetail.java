package com.example.myapplication.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.myapplication.Car;
import com.example.myapplication.R;
import com.example.myapplication.util.CarListAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarDetail extends AppCompatActivity {


    private String mCar_key  = null;
    private String mUser_key  = null;

    private DatabaseReference mDatabase;

    private ImageView mCarSingleImage;
    private TextView mBrand;
    private TextView mModel;
    private TextView mYear;
    private TextView mMileage;
    private TextView mFuel;
    private TextView mDescription;
    private TextView mPrice;
    private Car car1 = new Car();
    private DatabaseReference mDatabase2;
    private  Uri mimgeUri = null;
    private String car_image;



    ArrayList<Car> cars= new ArrayList<Car>();

    private  String TAG = "MMAMA";
    Button imgClick;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cars");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users");






        mCar_key = getIntent().getExtras().getString("car_id");
        mUser_key = getIntent().getExtras().getString("user_id");


        mBrand = (TextView) findViewById(R.id.tBrand);
        mModel = (TextView) findViewById(R.id.tModel);
        mYear = (TextView) findViewById(R.id.tYear);
        mMileage = (TextView) findViewById(R.id.tMileage);
        mFuel = (TextView) findViewById(R.id.tFuel);
        mDescription = (TextView) findViewById(R.id.tDescription);
        mPrice = (TextView) findViewById(R.id.tPrice);
        mCarSingleImage = (ImageView) findViewById(R.id.singleCarImage);
        imgClick = (Button)findViewById(R.id.rWatchList);




        //Toast.makeText(CarSingleActivity.this, car_key, Toast.LENGTH_SHORT).show();
        mDatabase.child(mCar_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String sBrand = (String) dataSnapshot.child("brand").getValue();
                final String sModel = (String) dataSnapshot.child("model").getValue();
                final String sYear = (String) dataSnapshot.child("year").getValue();
                final String sMileage = (String) dataSnapshot.child("mileage").getValue();
                final String sFuel = (String) dataSnapshot.child("fuel").getValue();
                final String sDescription = (String) dataSnapshot.child("description").getValue();
                final String sPrice = (String) dataSnapshot.child("price").getValue();
                final String spostID = (String) dataSnapshot.child("post_id").getValue();

                final String sadminId = (String) dataSnapshot.child("adminId").getValue();


                final String carId = (String) dataSnapshot.child("post_id").getValue();

                String uid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();


                car_image = (String) dataSnapshot.child("image").getValue();
                Log.d( TAG,"Brand "  + sBrand);

                mBrand.setText("Brand: "+sBrand);
                mModel.setText("Model: "+sModel);
                mYear.setText("Year: "+sYear);
                mMileage.setText("Mileage: "+sMileage);
                mFuel.setText("Fuel: "+sFuel);
                mDescription.setText("Description: "+sDescription);
                mPrice.setText("Price: "+sPrice+"$");

                Picasso.get().load(car_image).into(mCarSingleImage);

                imgClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                         car1 = new Car();
                        car1.setBrand(sBrand);
                        car1.setModel(sModel);
                        car1.setYear(sYear);
                        car1.setMileage(sMileage);
                        car1.setFuel(sFuel);
                        car1.setDescription(sDescription);
                        car1.setPrice(sPrice);
                        car1.setImage(car_image);
                        car1.setAdminId("1");
                        car1.setPost_id(spostID);
                        Log.d( TAG,"Brand "  + car1.getBrand()+ "Year "+car1.getYear());


                        Car.watchcars.add(car1);
                        Log.d( TAG,"DETAL Brand "  + Car.watchcars.get(0));
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        reference.child("Users")
                                .child(uid)
                                .child("watch_list")
                                .child(carId)
                                .setValue(car1);
                        Toast.makeText(getApplicationContext(), "Car Added To WatchList", Toast.LENGTH_SHORT).show();








                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    }






