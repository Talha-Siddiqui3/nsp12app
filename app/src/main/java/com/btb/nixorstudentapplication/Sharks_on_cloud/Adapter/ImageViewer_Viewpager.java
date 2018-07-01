package com.btb.nixorstudentapplication.Sharks_on_cloud.Adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ImageViewer_Viewpager extends ViewPager {
    public ImageViewer_Viewpager(Context context) {
        super(context);
    }

    public ImageViewer_Viewpager(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}