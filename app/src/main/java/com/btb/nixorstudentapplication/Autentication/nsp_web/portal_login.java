package com.btb.nixorstudentapplication.Autentication.nsp_web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Autentication.User.AccountType;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;


public class portal_login extends Activity implements View.OnClickListener {
    //XML
    EditText email_editText;
    EditText password_editText;
    TextView password_textView;
    TextView email_textView;
    Button login_button;
    common_util common_util;
    ProgressBar pb;
    public static String result = "";
    public static StudentDetails Student_Profile = null;

    String TAG = "portal_login";
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_login);
        common_util = new common_util();

        email_editText = findViewById(R.id.email_editText);
        password_editText = findViewById(R.id.password_editText);
        email_textView = findViewById(R.id.email_textView);
        password_textView = findViewById(R.id.password_textView);
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        pb = findViewById(R.id.progressBar_portal_login);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                initiatelogin();
                if (email_editText.isSelected()) {
                    HideKeyboard(email_editText);
                }
else{
                    HideKeyboard(password_editText);
                }
                break;
        }
    }

    private void initiatelogin() {
        if (!email_editText.getText().toString().equals("") && !password_editText.getText().toString().equals("")) {
            showLoading();
            email = email_editText.getText().toString();
            email += getString(R.string.domain_textView);
            Log.i(TAG, email);
            password = password_editText.getText().toString();
            String username = common_util.extractUsername(this, email);
            cloudFuncExtractData(email, password, username);
        } else {
            common_util.ToasterShort(this, "Please enter both email and passsword");
        }
    }

    public void cloudFuncExtractData(String email, String passoword, String username) {
        // Create the arguments to the callable function.
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        Map<String, Object> sendData = new HashMap<>();

        sendData.put("username", username);
        sendData.put("password", passoword);
        sendData.put("email", email);
        Log.i(TAG, "method executed");

        mFunctions
                .getHttpsCallable("NSP_EXTRACTION_FUNCTION")
                .call(sendData)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.i(TAG, "Success");
                        Log.i(TAG, httpsCallableResult.getData().toString());
                        Map<String, Object> returnedData = new HashMap<>();
                        returnedData = (Map<String, Object>) httpsCallableResult.getData();
                        if (returnedData.containsKey("Error") || returnedData.get("Error") != null) {
                            common_util.showAlertDialogue(portal_login.this, "Error", returnedData.get("Error").toString()).show();
                            hideLoading();
                        } else {
                            saveStudentDetails(returnedData);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "FAIlED");
                        common_util.showAlertDialogue(portal_login.this,
                                "Error", "Cant Connect to Server, Please make sure you are connected to Internet.").show();
                        hideLoading();
                    }
                });


    }


    public void saveStudentDetails(Map<String, Object> studentDataHashmap) {
        generateStudentDetailsObject(studentDataHashmap);
        StudentDetails instantiatedStudentObj = generateStudentDetailsObject(studentDataHashmap);
        common_util = new common_util();
        hideLoading();
        common_util.ToasterShort(this, "User Account Created");
        common_util.saveUserDataLocally(this, instantiatedStudentObj);
        this.startActivity(new Intent(this, AccountType.class));
        ((Activity) this).finish();


    }

    public StudentDetails generateStudentDetailsObject(Map<String, Object> studentDataHashmap) {
        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setStudent_name(studentDataHashmap.get("studentName").toString());
        studentDetails.setStudent_id(studentDataHashmap.get("studentID").toString());
        studentDetails.setStudent_year(studentDataHashmap.get("studentYear").toString());
        studentDetails.setStudent_house(studentDataHashmap.get("studentHouse").toString());
        studentDetails.setStudent_email(studentDataHashmap.get("studentEmail").toString());
        studentDetails.setStudent_profileUrl(studentDataHashmap.get("profile_url").toString());
        studentDetails.setStudent_guid(studentDataHashmap.get("GUID").toString());
        return studentDetails;
    }


    public void showLoading() {
        pb.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        pb.setVisibility(View.INVISIBLE);
    }


    public void HideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}