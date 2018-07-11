package com.btb.nixorstudentapplication.Application_Home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.Nsp_Portal.Adaptors.Nsp_Adaptor;
import com.btb.nixorstudentapplication.Past_papers.MainPPActivity;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.btb.nixorstudentapplication.User.UserPhoto;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class home_screen extends AppCompatActivity implements View.OnClickListener {
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
    ImageView movingshark;

    //NSP

    public static String GUID = "";
    private Map<String, Object> map = new HashMap<>();
    private GridView gridView;
    private Nsp_Adaptor nsp_adaptor;
    private List<String> icons;

    private DrawerLayout mDrawerLayout;
    private Button menu_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        //XML
        photoStudent_circleView = findViewById(R.id.student_photo);
        idStudent_textView = findViewById(R.id.idStudent_textView);
        nameStudent_textView = findViewById(R.id.nameStudent_textView);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        menu_button = findViewById(R.id.menu_button);
        movingshark = findViewById(R.id.movingshark);
        moveShark();
        animateShark();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        menu_button.setOnClickListener(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.pp:
                                startActivity(new Intent(home_screen.this, MainPPActivity.class));
                                break;
                            case R.id.soc:
                                startActivity(new Intent(home_screen.this, Soc_Main.class));
                                break;
                            case R.id.ta:
                                startActivity(new Intent(home_screen.this, Main_Activity_Ta_Tab.class));
                                break;
                        }
                        return true;
                    }
                });

        db = FirebaseFirestore.getInstance();
        loadStudentDetails();
        common_util.checkActivation(home_screen.this, username);
        intializeNSP();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_button:
                mDrawerLayout.openDrawer(GravityCompat.END);

        }
    }

    static Boolean directionback = false;
    static Boolean initialAnimation = true;

    public void moveShark() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels - 15;
        int animTime = 0;
        if (!initialAnimation) {
            animTime = 15000;
        }
        new CountDownTimer(animTime, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                initialAnimation = false;
                directionback = false;
                movingshark.animate().translationX(width).setDuration(15000);
                new CountDownTimer(15000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        directionback = true;
                        movingshark.animate().translationX(0).setDuration(15000);
                        moveShark();

                    }
                }.start();
            }
        }.start();


    }

    public void animateShark() {


        new CountDownTimer(100, 1000) {
            Boolean sharkset = true;

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (sharkset) {
                    if (directionback) {
                        movingshark.setImageResource(R.drawable.nixorsharkoriginal2back);
                    } else {
                        movingshark.setImageResource(R.drawable.nixorsharkoriginal2);
                    }
                    sharkset = true;
                    animateShark();
                } else {
                    if (directionback) {
                        movingshark.setImageResource(R.drawable.nixorsharkback);
                    } else {
                        movingshark.setImageResource(R.drawable.nixorsharkoriginal);
                    }
                    sharkset = false;
                    animateShark();
                }
            }
        }.start();


    }

    //Nsp Stuff. Do your commenting here
    public void intializeNSP() {
        icons = new ArrayList<>();
        common_util = new common_util();
        GetGUIDLocally();
        GetNspIcons();
        TAG = "oldActivity";
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
     /*   String username = common_util.getUserDataLocally(this, "username");
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
    */
        Log.i("ABCD", FirebaseInstanceId.getInstance().getToken());
        icons.add("Profile");
        icons.add("Gate Attendance");
        icons.add("Class Attendance");
        icons.add("Finance");
        icons.add("Schedule");
        icons.add("Student Marks");
        icons.add("CIE Grades");
        icons.add("TA Schedule");
        icons.add("TA Log");


    }


//In case we need GUID from FIrebase
    /*public void GetGUIDFireStore() {
        String userName = common_util.extractUsername(this, "talha.siddiqui@nixorcollege.edu.pk");
        DocumentReference dr = FirebaseFirestore.getInstance().collection("users").document(userName);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())

                {

                    GUID = task.getResult().get("student_guid").toString();
                } else {
                    Log.i(TAG, "Couldn't get GUID");

                }
            }
        });
    }
*/


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
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }







    }













}
