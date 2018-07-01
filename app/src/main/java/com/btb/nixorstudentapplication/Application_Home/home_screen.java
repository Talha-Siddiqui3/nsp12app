package com.btb.nixorstudentapplication.Application_Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Nsp_Portal.Nsp_Portal_MainActivity;
import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.User.UserPhoto;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_screen extends Activity implements View.OnClickListener {
    //XML
    CircleImageView photoStudent_circleView;
    TextView nameStudent_textView;
    TextView idStudent_textView;
    Button pastPapersIntent;
    Button bookMyTaIntent;
    Button nspIntent;

    String TAG = "home_screen";
    common_util common_util = new common_util();
    public static String username;
    public static String photoURL;
    public static String Studentname;
    public static String StudentID;
    public static FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //XML
        photoStudent_circleView = findViewById(R.id.student_photo);
        idStudent_textView = findViewById(R.id.idStudent_textView);
        nameStudent_textView = findViewById(R.id.nameStudent_textView);
        pastPapersIntent = findViewById(R.id.pastPapersIntent);
        pastPapersIntent.setOnClickListener(this);
        bookMyTaIntent = findViewById(R.id.bookMyTaIntent);
        bookMyTaIntent.setOnClickListener(this);
        nspIntent = findViewById(R.id.nsp_portal1);
        nspIntent.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        loadStudentDetails();
        common_util.checkActivation(home_screen.this, username);
    }


    public void loadStudentDetails() {
        //Get Username
        username = getUsername();
        //Get Student Name
        getStudentName(home_screen.this);
        getStudentID(home_screen.this);
        //Get Student photo
        UserPhoto photoClass = new UserPhoto();
        photoClass.getPhoto(username, home_screen.this);


    }

    public void getStudentName(Context context) {
        Studentname = common_util.getUserDataLocally(context, "name");
        nameStudent_textView.setText(Studentname);
    }

    public void getStudentID(Context context) {
        StudentID = common_util.getUserDataLocally(context, "student_id");
        idStudent_textView.setText(StudentID);
    }

    public String getUsername() {


        return common_util.getUserDataLocally(home_screen.this, "username");
    }

    public void setDisplay(String photoUrl, Context context) {
        photoURL = photoUrl;
        Glide.with(context).load(photoUrl).into(photoStudent_circleView);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pastPapersIntent:
                startActivity(new Intent(home_screen.this, MainPPActivity.class));
                break;
            case R.id.bookMyTaIntent:

                startActivity(new Intent(this, Main_Activity_Ta_Tab.class));
                break;
            case R.id.nsp_portal1:

                startActivity(new Intent(this, Nsp_Portal_MainActivity.class));
                break;
        }
    }
}
