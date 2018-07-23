package com.btb.nixorstudentapplication.Sharks_on_cloud.ImageViewver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btb.nixorstudentapplication.GeneralLayout.OnSwipeTouchListener;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.ImageViewer_Viewpager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ImageViewer extends Activity {

    ImageViewer_Viewpager cViewPager;
    public static ArrayList<String> imageList;
    private static int currentPage = 0;
    private static Boolean setImageTo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        cViewPager = (ImageViewer_Viewpager) findViewById(R.id.cviewpager);
        cViewPager.setAdapter(new ImagePagerAdapter(imageList, ImageViewer.this));
        if (setImageTo && imageList.size() >= currentPage) {
            cViewPager.setCurrentItem(currentPage);
            Log.i("pupu123", "YES" + currentPage + imageList.size());

        } else {
            Log.i("pupu123", "NO" + currentPage + imageList.size());
        }
    }

    public ImageViewer() {
    }

    public ImageViewer(ArrayList<String> urls) {
        imageList = urls;
    }

    public void startActivity(Context c) {

        Intent intent = new Intent(c, ImageViewer.class);
        c.startActivity(intent);
    }


    public ImageViewer(ArrayList<String> urls, int current) {
        imageList = urls;
        currentPage = current;
        setImageTo = true;
    }


    class ImagePagerAdapter extends PagerAdapter {
        ArrayList<String> imageList;
        Context context;
        common_util common_util = new common_util();
        ProgressBar pb;

        ImagePagerAdapter(ArrayList<String> imageList, Context activity) {
            this.imageList = imageList;
            context = activity;
        }

        //Can you hear me? nope
        //i can hear you
        //speakers khule hein?
        @Override
        public int getCount() {
            return (null != imageList) ? imageList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.item_imageview, null);
            pb = rootView.findViewById(R.id.loading_imageviewver);
            PhotoView photoView = rootView.findViewById(R.id.photView);
            TextView upvote_textview = rootView.findViewById(R.id.upvote_textview);
            Glide.with(context)
                    .load(imageList.get(position)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    pb.setVisibility(View.INVISIBLE);
                    return false;
                }
            })
                    .into(photoView);

            upvote_textview.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onSwipeTop() {
                    common_util.ToasterShort(context, "Upvote given! :)");
                }
            });

            ((ViewPager) container).addView(rootView, 0);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}


