package com.btb.nixorstudentapplication.Sharks_on_cloud.ImageViewver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btb.nixorstudentapplication.GeneralLayout.OnSwipeTouchListener;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.ImageViewer_Viewpager;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Buckets;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Subjects_homescreen;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ImageViewer extends Activity {

    ImageViewer_Viewpager cViewPager;
    public ArrayList<String> imageList;
    private static int currentPage = 0;
    private static Boolean setImageTo = false;
    private ArrayList<BucketDataObject> bucketDataObjects;
private ArrayList<Integer> validPosList;
private int clickPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        imageList = getIntent().getStringArrayListExtra("photoUrlsForImageViewver");
        bucketDataObjects = (ArrayList<BucketDataObject>) getIntent().getSerializableExtra("bucketDataObjects");
        validPosList=getIntent().getIntegerArrayListExtra("validPosList");
        clickPos=getIntent().getIntExtra("clickPos",0);
        cViewPager = (ImageViewer_Viewpager) findViewById(R.id.cviewpager);
        cViewPager.setAdapter(new ImagePagerAdapter(imageList, ImageViewer.this));
        cViewPager.setCurrentItem(clickPos);
        // if (setImageTo && imageList.size() >= currentPage) {
        //   cViewPager.setCurrentItem(currentPage);

        //}
    }

    public ImageViewer() {
    }

  /*  public ImageViewer(ArrayList<String> urls) {
        imageList = urls;
    }*/

    /*public void startActivity(Context c) {

        Intent intent = new Intent(c, ImageViewer.class);
        c.startActivity(intent);
    }*/


   /* public ImageViewer(ArrayList<String> urls, int current,ArrayList<BucketDataObject> bucketDataObjects) {
        this.bucketDataObjects=bucketDataObjects;
        imageList = urls;
        currentPage = current;
        setImageTo = true;
    }*/


    class ImagePagerAdapter extends PagerAdapter {
        common_util cu = new common_util();
        ArrayList<String> imageList;
        Context context;
        common_util common_util = new common_util();
        ProgressBar pb;
        TextView subjectName;
        TextView Date;
        TextView bucketName;
        String username;
        Button shareButton;

        ImagePagerAdapter(ArrayList<String> imageList, Context activity) {
            this.imageList = imageList;
            context = activity;
            username = cu.getUserDataLocally(context, "name");

        }

        //Can you hear me? nope
        //i can hear you
        //speakers khule hein?
        @Override
        public int getCount() {
            /*return (null != imageList) ? imageList.size() : 0;*/
            return validPosList.size();
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
            subjectName = rootView.findViewById(R.id.subjectName_ImageViewer);
            bucketName = rootView.findViewById(R.id.name_ImageViewer);
            Date = rootView.findViewById(R.id.date_imageViewer);
            subjectName.setText(Buckets.subjectName + "'s notes");
            bucketName.setText(username + "'s bucket");
            Date.setText(bucketDataObjects.get(validPosList.get(position)).getDateTimeStamp().toDate().toString());
            shareButton=rootView.findViewById(R.id.share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse());  //optional//use this when you want to send an image
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "send"));*/
                }
            });

            final PhotoView photoView = rootView.findViewById(R.id.photView);
            TextView upvote_textview = rootView.findViewById(R.id.upvote_textview);
            Glide.with(context)
                    .load(imageList.get(validPosList.get(position)))
                    .into(photoView);
                     /*.into(new SimpleTarget<GlideDrawable>() {
                         @Override
                         public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
photoView.setImageDrawable(resource);
Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,resource);  //optional//use this when you want to send an image
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "send"));*/

                     /*    }
                     });*/
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


