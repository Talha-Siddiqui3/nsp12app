package com.btb.nixorstudentapplication.GeneralLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_header extends RelativeLayout{
    View rootView;
   static CircleImageView displayphoto;
   static TextView displayname;
   static TextView displayid;
   static TextView nixorpoints;
   static TextView activityname;

    public activity_header(Context context) {
        super(context);
        init(context);
    }

    public activity_header(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        rootView = inflate(context, R.layout.activity_header, this);
        displayphoto = rootView.findViewById(R.id.student_photo);
        displayname = rootView.findViewById(R.id.displayname);
        displayid = rootView.findViewById(R.id.displayid);
        nixorpoints = rootView.findViewById(R.id.nixorpoints);
        activityname = rootView.findViewById(R.id.activity_name);

        common_util cm = new common_util();
        setName(cm.getUserDataLocally(context,"name"));
        setStudentID(cm.getUserDataLocally(context,"student_id"));
        if(cm.getUserDataLocally(context,"photourl")!=null){
          setDisplay(cm.getUserDataLocally(context,"photourl"),context);

        }

    }
    private void setDisplay(String photourl, Context context){
        Glide.with(context).load(photourl).into(displayphoto);
    }
    private void setName(String name){
        displayname.setText(name);
    }
    private void setStudentID(String id){
        displayid.setText(id);
    }
    public void setNixorPoints(String nixorPoints){
        nixorpoints.setText(nixorPoints);
    }
    public void setActivityname(String activityname1){
        activityname.setText(activityname1);
    }
}
