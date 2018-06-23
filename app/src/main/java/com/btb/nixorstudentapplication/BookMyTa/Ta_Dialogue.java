package com.btb.nixorstudentapplication.BookMyTa;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Ta_Dialogue extends Dialog {
    CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");

    String time;
    String day;
    String times[] = new String[5];
    String days[] = new String[5];
    TextView MondayTime;
    TextView TuesdayTime;
    TextView WednesdayTime;
    TextView ThursdayTime;
    TextView FridayTIme;
    TextView TaName;
    TextView TaID;
    Button BookTa;
    CircleImageView photoStudent_circleView;
    Map<String, Object> map = new HashMap<>();
    common_util cu = new common_util();
    TA_Object ta_object;
    Context context;
    @ServerTimestamp
    Date timestamp;


    public Ta_Dialogue(@NonNull final Context context) {
        super(context);
        this.context = context;
        setTitle("Title");
        setContentView(R.layout.ta_dialogue_layout);
        TaName = findViewById(R.id.TA_NAME_DIALOGUE);
        TaID=findViewById((R.id.TA_ID_DIALOGUE));
        photoStudent_circleView=findViewById(R.id.photoStudent_circleView_FOR_DIALOGUE);
        Glide.with(context).load(cu.getUserDataLocally(context,"nsp_photo")).into(photoStudent_circleView);
     

        BookTa = findViewById(R.id.Submit_Button_Ta_Dialogue);
        BookTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                cu.ToasterShort(context, "Request sent");
                Ta_Dialogue.super.hide();

            }
        });


    }

    private void sendRequest() {
        map.put("Status", "Pending");
        map.put("TaName", ta_object.getTaName());
        map.put("StudentName", cu.getUserDataLocally(context, "name"));
        map.put("latestUpdateTimestamp", FieldValue.serverTimestamp());
        cr.add(map);
    }

    public void UpdateLayout() {
        MondayTime = findViewById(R.id.Monday_Time);
        TuesdayTime = findViewById(R.id.Tuesday_Time);
        WednesdayTime = findViewById(R.id.Wednesday_Time);
        ThursdayTime = findViewById(R.id.Thursday_Time);
        FridayTIme = findViewById(R.id.Friday_Time);


        if (!days[0].equals("Null")) {
            MondayTime.setText(times[0]);
        }

        if (!days[1].equals("Null")) {
            TuesdayTime.setText(times[1]);
        }
        if (!days[2].equals("Null")) {
            WednesdayTime.setText(times[2]);
        }
        if (!days[3].equals("Null")) {
            ThursdayTime.setText(times[3]);
        }
        if (!days[4].equals("Null")) {
            FridayTIme.setText(times[4]);
        }


    }

    public void ExtractTime(TA_Object ta_object) {
        time = ta_object.getTimings();
        day = ta_object.getDays();
        times = time.split(",");
        days = day.split(",");
        TaName.setText(ta_object.getTaName());
        TaID.setText(ta_object.getTaID());
        this.ta_object = ta_object;

    }
}