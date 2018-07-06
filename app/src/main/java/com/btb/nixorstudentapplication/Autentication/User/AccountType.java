package com.btb.nixorstudentapplication.Autentication.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountType extends AppCompatActivity implements View.OnClickListener {
    private Button studentmode_button;
    private Button parentmode_button;

    private common_util common_util;
    private String username;
    private String phoneNumber;
    private String TAG = "AccountType";
    private String tokenForMessageing;
    private List<String> StudentFirebaseTokens;
    private List<String> ParentFirebaseTokens;
    private DocumentReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);
        common_util = new common_util();
        studentmode_button = (Button) findViewById(R.id.studentmode_button);
        parentmode_button = (Button) findViewById(R.id.parentmode_button);
        parentmode_button.setOnClickListener(this);
        studentmode_button.setOnClickListener(this);
    }

    public void activateStudent() {
        setAccountLink("student");
    }

    public void activateParent() {
        setAccountLink("parent");
    }

    public void setAccountLink(final String mode) {
        username = common_util.getUserDataLocally(AccountType.this, "username");
        phoneNumber = common_util.formatNumbers(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        accountTypeGetSet account = new accountTypeGetSet(username, mode);
        //  FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("account_link").document(phoneNumber).set(account).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                common_util.saveUserDataLocally(AccountType.this, "Mode", mode);
                startActivity(new Intent(AccountType.this, home_screen.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.studentmode_button:
                uploadFirebaseTokenStudent();
                activateStudent();
                break;
            case R.id.parentmode_button:
                uploadFirebaseTokenParent();
                activateParent();
                break;
        }
    }

    private void uploadFirebaseTokenParent() {
        tokenForMessageing = FirebaseInstanceId.getInstance().getToken();
        ParentFirebaseTokens = new ArrayList<>();
        dr = FirebaseFirestore.getInstance().collection("/users/talha-siddiqui/FirebaseTokens").document("ParentTokens");
        final Map<String, Object> uploadData = new HashMap<>();

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.get("parentFirebaseTokens")==null) {
                    ParentFirebaseTokens.add(tokenForMessageing);
                    uploadData.put("parentFirebaseTokens", ParentFirebaseTokens);
                    dr.set(uploadData,SetOptions.merge());
                    Log.i(TAG, "uploaded data inside if");
                } else {
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ParentFirebaseTokens = (ArrayList<String>) task.getResult().get("parentFirebaseTokens");
                            ParentFirebaseTokens.add(tokenForMessageing);
                            uploadData.put("parentFirebaseTokens", ParentFirebaseTokens);
                            UploadToken(uploadData);
                            Log.i(TAG, "uploaded data inside else");
                        }
                    });

                }
            }

        });

    }

    private void uploadFirebaseTokenStudent() {

        tokenForMessageing = FirebaseInstanceId.getInstance().getToken();
        StudentFirebaseTokens = new ArrayList<>();
        dr = FirebaseFirestore.getInstance().collection("/users").document("talha-siddiqui");
        final Map<String, Object> uploadData = new HashMap<>();

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.get("studentFirebaseTokens")==null) {
                    StudentFirebaseTokens.add(tokenForMessageing);
                    uploadData.put("studentFirebaseTokens", StudentFirebaseTokens);
                    dr.set(uploadData, SetOptions.merge());
                    Log.i(TAG, "uploaded data inside if");
                } else {
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            StudentFirebaseTokens = (ArrayList<String>) task.getResult().get("studentFirebaseTokens");
                            StudentFirebaseTokens.add(tokenForMessageing);
                            uploadData.put("studentFirebaseTokens", StudentFirebaseTokens);
                            UploadToken(uploadData);
                            Log.i(TAG, "uploaded data inside else");
                        }
                    });

                }
            }

        });

    }
    public void UploadToken(Map uploadData){
        dr.set(uploadData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG,"TOKEN UPLOADED");
            }
        });
    }



}