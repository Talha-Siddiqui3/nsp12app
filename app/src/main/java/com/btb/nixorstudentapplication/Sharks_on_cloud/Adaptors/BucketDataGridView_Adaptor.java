package com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.ImageViewver.ImageViewer;
import com.btb.nixorstudentapplication.Sharks_on_cloud.MyBucket;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Misc.checkedListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BucketDataGridView_Adaptor extends BaseAdapter {
    private ArrayList<BucketDataObject> bucketDataObjects;
    private ArrayList<String> photoUrlsForImageViewver;
    private Activity context;
    TextView contentName;
    ImageView contentImage;
    RelativeLayout relativeLayout;
    ProgressBar loading;
    private ArrayList<Integer> validPosList = new ArrayList<>();
    private String currentCollectionReferenece;
    private String yearSelected;
    private String subjectName;
    private String bucketType;
    private String username;
    private boolean isLongClicked = false;
    private SparseBooleanArray checkedItems;
    private SparseBooleanArray loadedItems;
    private String TAG = "BucketDataGridView_Adaptor";
    public checkedListener checkedListener;

    public BucketDataGridView_Adaptor(ArrayList<BucketDataObject> bucketDataObjects, ArrayList<String> photoUrlsForImageViewver, Activity context,
                                      String currentCollectionReferenece, String yearSelected, String subjectName, String bucketType, String username) {
        this.bucketDataObjects = bucketDataObjects;
        this.photoUrlsForImageViewver = photoUrlsForImageViewver;
        this.context = context;
        this.currentCollectionReferenece = currentCollectionReferenece;
        this.yearSelected = yearSelected;
        this.subjectName = subjectName;
        this.bucketType = bucketType;
        this.username = username;
        checkedItems = new SparseBooleanArray(bucketDataObjects.size());
        loadedItems=new SparseBooleanArray(bucketDataObjects.size());
    }


    @Override
    public int getCount() {
        return bucketDataObjects.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.item_bucketdata, parent, false);
        }

        contentName = convertView.findViewById(R.id.content_name_soc);
        contentImage = convertView.findViewById(R.id.content_image_soc);
        loading = convertView.findViewById(R.id.loading_bucketData1);
        relativeLayout = convertView.findViewById(R.id.BucketDataLayout);
        contentName.setText(bucketDataObjects.get(position).getName());
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLongClicked){
                    Log.i("123", String.valueOf(checkedItems.get(position,false)));
                    if(checkedItems.get(position,false)==true) {
                        checkedItems.append(position,false);
                    }
                    else{
                        checkedItems.append(position,true);
                    }
                    notifyDataSetChanged();
                }

                else if (photoUrlsForImageViewver.get(position) != null) {
                    Intent intent = new Intent(context, ImageViewer.class);
                    Bundle b = new Bundle();
                    b.putStringArrayList("photoUrlsForImageViewver", photoUrlsForImageViewver);
                    b.putIntegerArrayList("validPosList", validPosList);//pasing alist of only not null values avoiding folder null values
                    intent.putExtra("bucketDataObjects", bucketDataObjects);
                    intent.putExtra("clickPos", validPosList.indexOf(position));
                    intent.putExtras(b);
                    context.startActivity(intent);


                    //ImageViewer imageViewer = new ImageViewer(photoUrlsForImageViewver, position,bucketDataObjects);
                    //imageViewer.startActivity(context);
                } else {
                    Intent i = new Intent(context, MyBucket.class);
                    i.putExtra("subject", subjectName);
                    i.putExtra("year", yearSelected);
                    i.putExtra("type", bucketType);
                    i.putExtra("username", username);
                    i.putExtra("collectionReference", currentCollectionReferenece + "/" + bucketDataObjects.get(position).getName() + "/Buckets");
                    context.startActivity(i);

                }
            }
        });
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                checkedListener.onChecked(true);
                isLongClicked = true;
                checkedItems.append(position,true);
                notifyDataSetChanged();
                return false;
            }
        });

        handleSelectItems(position);
        handleLoadedItems(position);

        if (bucketDataObjects.get(position).isFolder()) {
            Picasso.get()
                    .load(R.drawable.folder_icon_large)
                    .error(R.drawable.ic_error_outline)
                    .into(contentImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if(loadedItems.get(position,false)==false) {
                                loading.setVisibility(View.GONE);
                                loadedItems.append(position, true);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Exception e) {


                        }
                    });
        } else {
            if (photoUrlsForImageViewver.get(position) != null && !validPosList.contains(position)) {
                validPosList.add(position);
            }
            Picasso.get()
                    .load(bucketDataObjects.get(position).getPhotoUrlThumbnail())
                    .error(R.drawable.ic_error_outline)
                    .into(contentImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if(loadedItems.get(position,false)==false) {
                                loading.setVisibility(View.GONE);
                                loadedItems.append(position, true);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if(loadedItems.get(position,false)==false) {
                                loading.setVisibility(View.GONE);
                                loadedItems.append(position, true);
                                notifyDataSetChanged();
                            }
                            Log.i(TAG, e.toString());
                        }
                    });
        }

        return convertView;
    }

    private void handleSelectItems(int position) {
        if (isLongClicked) {
            if(checkedItems.get(position,false)==true) {
                relativeLayout.setBackgroundResource(R.color.colorPrimary);
            }
            else{
                relativeLayout.setBackgroundResource(0);
            }
        }
        else{
            relativeLayout.setBackgroundResource(0);
        }
    }

    private void handleLoadedItems(int position){
        if(loadedItems.get(position,false)==true){
            loading.setVisibility(View.GONE);
        }
        else{
            loading.setVisibility(View.VISIBLE);
        }
    }

    public boolean getCheckedItem(int position) {
        return checkedItems.get(position);
    }
public void resetCheckedItems(){
        isLongClicked=false;
        checkedItems.clear();
        notifyDataSetChanged();
}

}
//TODO: ADD LOADING