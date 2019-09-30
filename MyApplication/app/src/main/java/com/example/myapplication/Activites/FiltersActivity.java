package com.example.myapplication.Activites;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.R;

public class FiltersActivity extends AppCompatActivity {

    private static final String TAG = "com.example.forsale.FiltersActivity1";

    //widgets
    private Button mSave;
    private EditText mBrand, mModel, mYear;
    private ImageView mBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filasdas);
        mSave = (Button) findViewById(R.id.btnSave);
        mBrand = (EditText) findViewById(R.id.input_brand);
        mModel = (EditText) findViewById(R.id.input_model);
        mYear = (EditText) findViewById(R.id.input_year);
        mBackArrow = (ImageView) findViewById(R.id.backArrow);


        init();

    }

    private void init(){

        getFilterPreferences();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: saving...");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FiltersActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                Log.d(TAG, "onClick: Brand: " + mBrand.getText().toString());
                editor.putString(getString(R.string.preferences_brand), mBrand.getText().toString());
                editor.commit();

                Log.d(TAG, "onClick: Model: " + mModel.getText().toString());
                editor.putString(getString(R.string.preferences_model), mModel.getText().toString());
                editor.commit();

                Log.d(TAG, "onClick: Year: " + mYear.getText().toString());
                editor.putString(getString(R.string.preferences_year), mYear.getText().toString());
                editor.commit();
            }
        });

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back.");
                finish();
            }
        });
    }

    private void getFilterPreferences(){
        Log.d(TAG, "getFilterPreferences: retrieving saved preferences.");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String year = preferences.getString(getString(R.string.preferences_year), "");
        String model = preferences.getString(getString(R.string.preferences_model), "");
        String brand = preferences.getString(getString(R.string.preferences_brand), "");

        mYear.setText(year);
        mModel.setText(model);
        mBrand.setText(brand);
    }
}



