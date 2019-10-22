package com.example.myapplication.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by com.example.forsale.models.Users on 11/1/2017.
 */

public class SquareImageView extends ImageView {


    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}