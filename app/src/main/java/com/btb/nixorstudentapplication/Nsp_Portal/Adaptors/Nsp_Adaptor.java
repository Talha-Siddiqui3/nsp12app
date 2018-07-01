package com.btb.nixorstudentapplication.Nsp_Portal.Adaptors;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Nsp_Portal.Nsp_ASyncTask;
import com.btb.nixorstudentapplication.Nsp_Portal.Nsp_Portal_MainActivity;
import com.btb.nixorstudentapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.i;

public class Nsp_Adaptor extends BaseAdapter implements View.OnClickListener {
    List<String> iconList = new ArrayList<>();
    private TextView txt;
    private ImageView img;
    private common_util cu=new common_util();
    private Activity context;


public Nsp_Adaptor(List<String>iconList,Activity context) {
    this.iconList = iconList;
    this.context = context;

}
    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case "Profile":
                GetNspData("Profile");
                break;
            case "Gate Attendance":
                GetNspData("Gate%20Attendance");
                break;
            case "Class Attendance":
                GetNspData("Attendance");
                break;
            case "Finance":
                GetNspData("Finance");
                break;
            case "Schedule":
                GetNspData("Schedule");
                break;
            case "Student Marks":
                GetNspData("Student%20Marks");
                break;
            case "CIE Grades":
                GetNspData("Grades");
                break;
            case "TA Schedule":
               cu.ToasterLong(context, "NOT AVAILABLE YET");
                break;
            case "TA Log":
                GetNspData("TA%20Log");
                break;
            default:
               cu.ToasterLong(context, "ERROR");
                break;

        }
    }

    @Override
    public int getCount() {
        return iconList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.nsp_layout_for_grid_view, parent, false);
        }
        txt = convertView.findViewById(R.id.icon_text_nsp_portal);
        img = convertView.findViewById(R.id.iconimage_nsp_portal);
        txt.setOnClickListener(this);
        img.setOnClickListener(this);
        txt.setText(iconList.get(position));
        img.setTag(iconList.get (position));
        txt.setTag(iconList.get (position));

        switch (iconList.get(position)) {
            case "Profile":
                img.setImageResource(R.drawable.newprofile);
                break;
            case "Gate Attendance":
                img.setImageResource(R.drawable.gateattendance);
                break;
            case "Class Attendance":
                img.setImageResource(R.drawable.class_attendance);
                break;
            case "Finance":
                img.setImageResource(R.drawable.finance);
                break;
            case "Schedule":
                img.setImageResource(R.drawable.schedule_image);
                break;
            case "Student Marks":
                img.setImageResource(R.drawable.studentmarks);
                break;
            case "CIE Grades":
                img.setImageResource(R.drawable.cie_grades);
                break;
            case "TA Schedule":
                img.setImageResource(R.drawable.tascheduleicon);
                break;
            case "TA Log":
                img.setImageResource(R.drawable.talogicon);
                break;
            default:
                img.setVisibility(View.INVISIBLE);
                break;


        }


        return convertView;
    }

    public void GetNspData(String data){
      cu.progressDialogShow(context, "Please Wait");
        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/NspDocuments/" + data+".pdf");
        Nsp_ASyncTask aSyncTask = new Nsp_ASyncTask(file1, context, Nsp_Portal_MainActivity.GUID,data);
        aSyncTask.execute();
    }




}
