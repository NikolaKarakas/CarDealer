package com.example.myapplication.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


import android.widget.TextView;

import com.example.myapplication.Model.Cars;

import com.example.myapplication.Client_ViewHolder.Client_ProductViewHolder;
import com.example.myapplication.Prevalent.Prevalent;
import com.example.myapplication.R;
import com.example.myapplication.util.CarListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;










import de.hdodenhof.circleimageview.CircleImageView;

public class Home_UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference CarsRef;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String name;

    private FirebaseRecyclerAdapter<Cars, Client_ProductViewHolder> mVehicleAdapter;

    private CarListAdapter mAdapter;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__user);



        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        CarsRef = FirebaseDatabase.getInstance().getReference().child("cars");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        // final String user_id =mAuth.getCurrentUser().getUid();

        View headerViw = navigationView.getHeaderView(0);
        final TextView userNameTextView = headerViw.findViewById(R.id.user_profile_name);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        CircleImageView profileImageView = headerViw.findViewById(R.id.user_profile_image);
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(profileImageView);
      //  Toast.makeText(Home_UserActivity.this, Prevalent.currentOnlineUser.getImage(), Toast.LENGTH_LONG).show(); ;





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
                        Intent singleCarIntent = new Intent(Home_UserActivity.this, CarDetail.class);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_watchlist)
        {
            Intent intent = new Intent(Home_UserActivity.this, WatchList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();
        }

        else if (id == R.id.nav_logout) {

            Intent intent = new Intent(Home_UserActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();

        }
        else if (id == R.id.nav_settins)
        {
            Intent intent  = new Intent(Home_UserActivity.this, SettingsActivity.class);


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
