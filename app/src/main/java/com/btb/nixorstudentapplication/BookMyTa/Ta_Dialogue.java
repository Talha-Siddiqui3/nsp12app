package com.btb.nixorstudentapplication.BookMyTa;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//Opens when student clicks on any TA for booking
public class Ta_Dialogue extends Dialog {
    private CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");

    private String time;
    private String day;
    private String times[] = new String[5];
    private String days[] = new String[5];
    private TextView MondayTime;
    private TextView TuesdayTime;
    private TextView WednesdayTime;
    private TextView ThursdayTime;
    private TextView FridayTIme;
    private TextView TaName;
    private TextView TaID;
    private TextView TaSubject;
    private Button BookTa;
    private CircleImageView photoStudent_circleView;
    private Map<String, Object> map = new HashMap<>();
    private common_util cu = new common_util();
    private TA_Object ta_object;
    private Context context;
    @ServerTimestamp
    Date timestamp;


    public Ta_Dialogue(@NonNull final Context context, TA_Object ta_object) {
        super(context);
        this.context = context;
        setContentView(R.layout.cell_content_layout);
        this.ta_object=ta_object;
        TaName = findViewById(R.id.TA_NAME_DIALOGUE);
        TaID = findViewById((R.id.TA_ID_DIALOGUE));
        TaSubject=findViewById(R.id.SubjectTA_Dialogue);
        photoStudent_circleView = findViewById(R.id.photoStudent_circleView_FOR_DIALOGUE);
        TaName.setText(ta_object.getTaName());
        TaID.setText(ta_object.getTaID());
TaSubject.setText(ta_object.getSubject());
        //loading TA Picture
        Glide.with(context).load(cu.getUserDataLocally(context, "nsp_photo")).into(photoStudent_circleView);


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

    //Creating new request to book TA from Dialogue
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


//OLD IMPLEMENTATION
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


    //IMLEMENT THIS
    public void ParseData() {

    }


    //OLD IMPLEMENTATION
    //Make new implementation
    public void ExtractTime() {
        time = ta_object.getTimings();
        day = ta_object.getDays();
        times = time.split(",");
        days = day.split(",");


    }

}