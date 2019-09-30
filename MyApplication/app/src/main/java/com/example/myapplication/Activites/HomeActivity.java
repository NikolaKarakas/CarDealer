package com.example.myapplication.Activites;
import com.example.myapplication.Car;
import com.example.myapplication.HitsList;
import com.example.myapplication.HitsObject;
import com.example.myapplication.R;
import com.example.myapplication.Client_ViewHolder.ViewHolder;
import com.example.myapplication.util.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    private static final int NUM_GRID_COLUMNS = 3;
    private static final int GRID_ITEM_MARGIN = 5;

    private RecyclerView mVehicleList;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Car, ViewHolder> mVehicleAdapter;
    private ImageView mFilters;
    private EditText mSearchText;
    private String mElasticSearchPassword;
    private String mPrefBrand;
    private String mPrefModel;
    private String mPrefYear;
    private ArrayList<Car> mCars;
    private static final String TAG = "com.example.myapplication.HomeAcitivity";
    private static final String BASE_URL = "http://34.66.217.199/elasticsearch/cars/car/";
    private CarListAdapter  mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        mVehicleList = (RecyclerView)findViewById(R.id.car_list1);
        mVehicleList.setHasFixedSize(true);
        mVehicleList.setLayoutManager(new LinearLayoutManager(this));
        mFilters = (ImageView) findViewById(R.id.ic_search);
        mSearchText = (EditText) findViewById(R.id.input_search);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cars");
        getElasticSearchPassword();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Car>()
                .setQuery(mDatabase, Car.class).build();


        mVehicleAdapter = new FirebaseRecyclerAdapter<Car, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {




                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.vehicle_row, viewGroup, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Car model) {

                final String car_key = getRef(position).getKey();
                holder.setBrand(model.getBrand());
                holder.setDesc(model.getDescription());
                holder.setImage(getApplicationContext(),   model.getImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleCarIntent = new Intent(HomeActivity.this, CarSingleActivity.class);
                        singleCarIntent.putExtra("car_id", car_key);
                        startActivity(singleCarIntent);

                    }
                });






            }
        };

        mVehicleList.setAdapter(mVehicleAdapter);


            init();

    }




    private  void  init ()
    {

        mFilters.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to filters activity.");
                Intent intent = new Intent(getApplicationContext(), FiltersActivity.class);
                startActivity(intent);
            }
        });




       mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @SuppressLint("LongLogTag")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        ||actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getKeyCode() == KeyEvent.KEYCODE_ENTER){

                    mCars = new ArrayList<Car>();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    getFilters();
                    ElasticSearchAPI searchAPI = retrofit.create(ElasticSearchAPI.class);

                    HashMap<String, String> headerMap = new HashMap<String, String>();

                    headerMap.put("Authorization", Credentials.basic("user", mElasticSearchPassword));

                    String searchString = "";

                    if(!mSearchText.equals("")){
                        searchString = searchString + mSearchText.getText().toString() + "*";
                    }
                    if(mPrefBrand!=null && !mPrefBrand.equals("")){
                        searchString = searchString + " brand:" + mPrefBrand;
                    }
                    if(mPrefModel!=null &&!mPrefModel.equals("")){
                        searchString = searchString + " model:" + mPrefModel;
                    }
                    if(mPrefYear!=null && !mPrefYear.equals("")){
                        searchString = searchString + " year:" + mPrefYear;

                    }
                    Log.d(TAG, "onResponse:vSearchString   ppppp:" + searchString);


                    Call<HitsObject> call = searchAPI.search(headerMap, "AND", searchString);

                    call.enqueue(new Callback<HitsObject>() {
                        @Override
                        public void onResponse(Call<HitsObject> call, Response<HitsObject> response) {

                            HitsList hitsList = new HitsList();
                            String jsonResponse = "";
                            try{
                                Log.d(TAG, "onResponse: server response: " + response.toString());

                                if(response.isSuccessful()){
                                    hitsList = response.body().getHits();


                                }else{
                                    jsonResponse = response.errorBody().string();
                                }

                                Log.d(TAG, "onResponse: hits: " + hitsList);

                                for(int i = 0; i < hitsList.getCarIndex().size(); i++){
                                    Log.d(TAG, "onResponse: data: " + hitsList.getCarIndex().get(i).getCar().toString());
                                    mCars.add(hitsList.getCarIndex().get(i).getCar());

                                }

                                Log.d(TAG, "onResponse: size: " + mCars.size());
                                //setup the list of posts
                                setupPostsList();

                            }catch (NullPointerException e){
                                Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage() );
                            }
                            catch (IndexOutOfBoundsException e){
                                Log.e(TAG, "onResponse: IndexOutOfBoundsException: " + e.getMessage() );
                            }
                            catch (IOException e){
                                Log.e(TAG, "onResponse: IOException: " + e.getMessage() );
                            }
                        }

                        @Override
                        public void onFailure(Call<HitsObject> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t.getMessage() );
                            Toast.makeText(getApplicationContext(), "search failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


                return false;
            }
        });
    }


    private void setupPostsList(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), NUM_GRID_COLUMNS);
        mVehicleList.setLayoutManager(gridLayoutManager);
        mAdapter = new CarListAdapter(getApplicationContext(), mCars);
        mVehicleList.setAdapter(mAdapter);
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

    @SuppressLint("LongLogTag")
    private void getElasticSearchPassword(){
        Log.d(TAG, "getElasticSearchPassword: retrieving elasticsearch password.");

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.node_elasticsearch))
                .orderByValue();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                mElasticSearchPassword = singleSnapshot.getValue().toString();
                Log.d(TAG, "getElasticSearchPassword: retrieving elasticsearch password "+ mElasticSearchPassword);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add)
        {
            startActivity(new Intent(HomeActivity.this, VehicleActivity.class) );
        }

        if(item.getItemId() == R.id.logoutbutton)
        {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class) );
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("LongLogTag")
    private void getFilters(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPrefBrand = preferences.getString(getString(R.string.preferences_brand), "");
        mPrefModel = preferences.getString(getString(R.string.preferences_model), "");
        mPrefYear = preferences.getString(getString(R.string.preferences_year), "");

        Log.d(TAG, "getFilters: got filters: \nBrand: " + mPrefBrand + "\nModel: " + mPrefModel
                + "\nYear: " + mPrefYear);
    }
}
