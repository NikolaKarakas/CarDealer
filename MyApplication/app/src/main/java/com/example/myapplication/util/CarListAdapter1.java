package com.example.myapplication.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.Car;

import java.util.ArrayList;

public class CarListAdapter1 extends RecyclerView.Adapter<CarListAdapter1.ViewHolder>{


    private static final String TAG = "CarListAdapter1";
    private static final int NUM_GRID_COLUMNS = 2;


    private ArrayList<Car> mCars;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{

        SquareImageView mPostImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mPostImage = (SquareImageView) itemView.findViewById(R.id.post_image);

            int gridWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            int imageWidth = gridWidth/NUM_GRID_COLUMNS;
            mPostImage.setMaxHeight(imageWidth);
            mPostImage.setMaxWidth(imageWidth);
        }
    }


    public CarListAdapter1(Context context, ArrayList<Car> mCars) {
        this.mCars = mCars;
        mContext=context;
    }

    @Override
    public CarListAdapter1.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_grid_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarListAdapter1.ViewHolder viewHolder, int i) {
        UniversalImageLoader.setImage(mCars.get(i).getImage(), viewHolder.mPostImage);

        viewHolder.mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: selected a post");
                //TODO

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }
}

