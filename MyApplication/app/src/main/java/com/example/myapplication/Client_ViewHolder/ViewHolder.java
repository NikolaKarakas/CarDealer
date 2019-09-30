package com.example.myapplication.Client_ViewHolder;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;


public  class ViewHolder extends RecyclerView.ViewHolder {

    public View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }


    public void setBrand(String brand)
    {
        TextView vehicle_name = (TextView) mView.findViewById(R.id.vehicle_brand);
        vehicle_name.setText(brand);
    }

    public void setDesc(String desc)
    {
        TextView vehicle_desc = (TextView) mView.findViewById(R.id.vehicle_description);
        vehicle_desc.setText(desc);
    }

    public void setImage(Context ctc , String image){
        ImageView vehicle_image = (ImageView) mView.findViewById(R.id.vehicle_image);
        Picasso.get().load(image).into(vehicle_image);
    }

}
