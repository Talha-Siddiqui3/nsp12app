package com.btb.nixorstudentapplication.Application_Home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.User.UserPhoto;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_screen extends Activity {
//XML
CircleImageView photoStudent_circleView;
TextView nameStudent_textView;
TextView idStudent_textView;

String TAG ="home_screen";
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
        photoStudent_circleView = findViewById(R.id.photoStudent_circleView);
        idStudent_textView = findViewById(R.id.idStudent_textView);
        nameStudent_textView = findViewById(R.id.nameStudent_textView);
         db = FirebaseFirestore.getInstance();
        loadStudentDetails();
        common_util.checkActivation(home_screen.this,username);
    }


    public void loadStudentDetails(){
        //Get Username
    username = getUsername();
//Get Student Name
getStudentName(home_screen.this);
getStudentID(home_screen.this);
    //Get Student photo
    UserPhoto photoClass = new UserPhoto();
    photoClass.getPhoto(username,home_screen.this);



}
    public void getStudentName(Context context){
        Studentname = common_util.getUserDataLocally(context,"name");
        nameStudent_textView.setText(Studentname);
}
    public void getStudentID(Context context){
        StudentID = common_util.getUserDataLocally(context,"student_id");
        idStudent_textView.setText(StudentID);
    }
    public String getUsername(){
       return common_util.getUserDataLocally(home_screen.this,"username");
    }
    public void setDisplay(String photoUrl, Context context){
        photoURL=photoUrl;
            Glide.with(context).load(photoUrl).into(photoStudent_circleView);

    }
}
