package com.example.myapplication.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;


import android.widget.Button;

import com.example.myapplication.Model.Cars;

import com.example.myapplication.Client_ViewHolder.Client_ProductViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.util.CarListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class WatchList extends AppCompatActivity {

    private DatabaseReference CarsRef;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button back;
    String name;

    private FirebaseRecyclerAdapter<Cars, Client_ProductViewHolder> mVehicleAdapter;

    private CarListAdapter mAdapter;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        back=(Button) findViewById(R.id.button3);



        recyclerView = findViewById(R.id.car_list1);
        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CarsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
        .child("watch_list");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchList.this, Home_UserActivity.class);
                startActivity(intent);
            }
        });









        // final String user_id =mAuth.getCurrentUser().getUid();



        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Cars>()
                .setQuery(CarsRef, Cars.class).build();






        mVehicleAdapter = new FirebaseRecyclerAdapter<Cars, Client_ProductViewHolder>(options) {

            @NonNull
            @Override
            public Client_ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {



                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.car_item_layout, viewGroup, false);

                return new Client_ProductViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull Client_ProductViewHolder holder, int position, @NonNull Cars model) {
                // holder.txtProductPrice.setText(model.getPrice());
                holder.txtProductPrice.setText("Price = "+ model.getPrice()+ "$");
                //holder.setImage(getApplicationContext(),   model.getImage());
                holder.txtProductBrand.setText(model.getBrand());
                holder.txtProductModel.setText(model.getModel());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                final String car_key = getRef(position).getKey();
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleCarIntent = new Intent(WatchList.this, CarDetail1.class);
                        singleCarIntent.putExtra("car_id", car_key);
                        startActivity(singleCarIntent);
                    }
                });

            }

        };


        recyclerView.setAdapter(mVehicleAdapter);




    }

    @Override
    protected void onStart() {
        super.onStart();


        mVehicleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVehicleAdapter.stopListening();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


}
