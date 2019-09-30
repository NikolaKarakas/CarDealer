package com.example.myapplication.Activites;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CarSingleActivity extends AppCompatActivity {


    private String mCar_key  = null;
    private DatabaseReference mDatabase;

    private ImageView mCarSingleImage;
    private EditText mCarSingleBrand;
    private EditText mCarSingleDescription;
    private EditText mCarSinglePrice;

    private Button mSingleRemoveBtn;
    private Button mSingleUpdateBtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_single);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cars");

        mCar_key = getIntent().getExtras().getString("car_id");

        mCarSingleBrand = (EditText) findViewById(R.id.singleCarBrand);
        mCarSingleDescription = (EditText) findViewById(R.id.singleCarDescription);
        mCarSingleImage = (ImageView) findViewById(R.id.singleCarImage);
        mSingleRemoveBtn = (Button) findViewById(R.id.removeCarbutton);
        mCarSinglePrice = (EditText) findViewById(R.id.singleCarPrice);
        mSingleUpdateBtn =(Button) findViewById(R.id.updateCar);

        //Toast.makeText(CarSingleActivity.this, car_key, Toast.LENGTH_SHORT).show();
        mDatabase.child(mCar_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String car_name = (String) dataSnapshot.child("brand").getValue();
                String car_desription = (String) dataSnapshot.child("description").getValue();
                String car_price = (String) dataSnapshot.child("price").getValue();
                String car_image = (String) dataSnapshot.child("image").getValue();


                mCarSingleBrand.setText(car_name);
                mCarSingleDescription.setText(car_desription);
                mCarSinglePrice.setText(car_price);
                Picasso.get().load(car_image).into(mCarSingleImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mCar_key).removeValue();
                Intent homeIntent = new Intent(CarSingleActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });



        mSingleUpdateBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {



                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child(mCar_key).child("brand").setValue(mCarSingleBrand.getText().toString());

                        dataSnapshot.getRef().child(mCar_key).child("description").setValue(mCarSingleDescription.getText().toString());

                        dataSnapshot.getRef().child(mCar_key).child("price").setValue(mCarSinglePrice.getText().toString());


                        Toast.makeText(CarSingleActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        CarSingleActivity.this.finish();

                        Intent homeIntent = new Intent(CarSingleActivity.this, HomeActivity.class);
                        startActivity(homeIntent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });



    }
}
