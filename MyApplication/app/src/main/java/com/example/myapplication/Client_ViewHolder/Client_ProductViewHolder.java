package com.example.myapplication.Client_ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Interface.ItemClickListener;
import com.example.myapplication.R;

public class Client_ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {




    public TextView txtProductBrand, txtProductModel, txtProductPrice;
    public ImageView imageView;
    /*public View mView;

    public Client_ProductViewHolder (View itemView) {
        super(itemView);
        mView = itemView;
    }*/


    public ItemClickListener listener;


    public Client_ProductViewHolder(@NonNull View itemView) {
        super(itemView);





        imageView = (ImageView) itemView.findViewById(R.id.product_client_image);
        txtProductBrand = (TextView) itemView.findViewById(R.id.product_client_brand);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_client_price);
        txtProductModel=(TextView) itemView.findViewById(R.id.product_client_model);



    }


    public void setItemClickListener(ItemClickListener listener)
    {

    this.listener =  listener;
    }



    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);



    }
}

