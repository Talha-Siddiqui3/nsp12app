package com.btb.nixorstudentapplication.Application_Home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.Nsp_Portal.Adaptors.Nsp_Adaptor;
import com.btb.nixorstudentapplication.Nsp_Portal.Nsp_Portal_MainActivity;
import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.User.UserPhoto;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_screen extends Activity {
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

    //NSP

    public static String GUID = "";
    private Map<String, Object> map = new HashMap<>();
    private GridView gridView;
    private Nsp_Adaptor nsp_adaptor;
    private List<String> icons;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //XML
        photoStudent_circleView = findViewById(R.id.student_photo);
        idStudent_textView = findViewById(R.id.idStudent_textView);
        nameStudent_textView = findViewById(R.id.nameStudent_textView);



       // drawer = (DrawerLayout) findViewById(R.id.drawerLayout);



        db = FirebaseFirestore.getInstance();
        loadStudentDetails();
        common_util.checkActivation(home_screen.this, username);
        intializeNSP();
    }
    public void intializeNSP(){
        icons = new ArrayList<>();
        common_util = new common_util();
        GetGUIDLocally();
        GetNspIcons();
        TAG = "Nsp_Portal_MainActivity";
        gridView = findViewById(R.id.GridView_NspPortal);
        nsp_adaptor = new Nsp_Adaptor(icons, this);
        gridView.setAdapter(nsp_adaptor);
        GetExternalStoragePermission();
        MakePath();


    }
    public void MakePath() {
        File path = new File(Environment.getExternalStorageDirectory() + "/nixorapp/NspDocuments/");
        if (!path.exists())
            path.mkdirs();

    }
    public void GetGUIDLocally() {
        GUID = common_util.getUserDataLocally(this, "GUID");

    }
    private void GetExternalStoragePermission() {
        permission_util permission_util = new permission_util();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        permission_util.getPermissions(this, permissions);
    }
    public void GetNspIcons() {
        String username = common_util.getUserDataLocally(this, "username");
        DocumentReference dr = FirebaseFirestore.getInstance().collection("users").document(username).collection("icons").document("myicons");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())

                {
                    map = task.getResult().getData();
                    for (int i = 0; i < map.size(); i++) {

                        Log.i(TAG, map.get(Integer.toString(i)).toString());
                        icons.add(i, map.get(Integer.toString(i)).toString());
                    }

                    nsp_adaptor.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "cant get data");
                }
            }
        });
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


}
