package com.btb.nixorstudentapplication.Autentication;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Autentication.User.accountTypeGetSet;
import com.btb.nixorstudentapplication.Autentication.nsp_web.StudentDetails;
import com.btb.nixorstudentapplication.Autentication.nsp_web.portal_login;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.Misc.sms_broadcast.SmsListener;
import com.btb.nixorstudentapplication.Misc.sms_broadcast.SmsReceiver;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class login_screen extends Activity implements View.OnClickListener {
    String TAG = "login_screen";
    FirebaseUser FbUser;
    StudentDetails studentDetails;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    permission_util permission_util;
    common_util common_util = new common_util();
    private FirebaseAuth FbAuth;
    private Boolean beingverified = false;
    private String phoneNumber;
    private String code;
    private PhoneAuthCredential credential;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //XML res
    EditText code_entry;
    EditText phone_entry;
    Button send_code;
    Button verify_code;
    RelativeLayout splash_screen;
    TextView code_timer;
    //Permission
    private static String[] PERMISSIONS = {
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.READ_SMS,};
    //fcm upload
    private ArrayList<String> ParentFirebaseTokens;
    private ArrayList<String> StudentFirebaseTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        splash_screen = findViewById(R.id.splash_screen);
        splash_screen.setVisibility(View.VISIBLE);
        //Intialize var
        permission_util = new permission_util();
            //XMl
        code_entry = (EditText) findViewById(R.id.code_entry);
        phone_entry =(EditText) findViewById(R.id.phone_entry);
        send_code = (Button) findViewById(R.id.send_code);
        verify_code =(Button) findViewById(R.id.verify_code);

        code_timer = (TextView) findViewById(R.id.code_timer);

        send_code.setOnClickListener(this);
        verify_code.setOnClickListener(this);

        initialize();

    }
    public void initialize() {

        FbAuth = FirebaseAuth.getInstance();
        permission_util.getPermissions(login_screen.this, PERMISSIONS);
        ui_Updates();
        verification_callback();


    }
    public void ui_Updates(){
    //Enable verify button on phone number entered
        phone_entry.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if(charSequence.length()>4){
    send_code.setEnabled(true);
    }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()>4){
            send_code.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length()>4){
            send_code.setEnabled(true);
        }
    }
});
    }
    public  void enableSms(){
        ComponentName receiver = new ComponentName(login_screen.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.i("sms","Enabled broadcst receiver");

    }

    private void sendCode(){
        if(phone_entry.getText()!=null){
            String loadinText = getString(R.string.phoneAuthSendCodeLoadingText);
            common_util.progressDialogShow(login_screen.this,loadinText);
            phoneNumber = phone_entry.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);
            beingverified=true;
        }       // OnVerificationStateChangedCallbacks
    }
    private void updateUICodeSent(){

code_entry.setVisibility(View.VISIBLE);
verify_code.setVisibility(View.VISIBLE);
send_code.setEnabled(false);

        codeSentTimer();
    }
    private void codeSentTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                code_timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                send_code.setEnabled(true);
            }
        }.start();
    }
    public void checkforSms(){
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("TAG",messageText);
                String str=messageText;
                String numberOnly= str.replaceAll("[^0-9]", "");
                code_entry.setText(numberOnly);
                verifyCode();
                common_util.ToasterShort(login_screen.this,"Code: "+messageText);




            }
        });

    }


    private void invalidCode(){

    }
    private void verifyCode(){
        if(code_entry.getText()!=null){
            code=code_entry.getText().toString();
            String loadinText = getString(R.string.phoneAuthVerifyCodeLoadingText);
            common_util.progressDialogShow(login_screen.this,loadinText);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }

    }


    private void signInComplete(){
        //String loginSuccessfulText = getString(R.string.phoneAuthSuccess);
       // common_util.ToasterShort(login_screen.this,loginSuccessfulText);
        if(FbUser==null)FbUser=FirebaseAuth.getInstance().getCurrentUser();
        checkUserHistory(FbUser.getUid());
    }
    private void checkUserHistory(final String uid){
        String userPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
       userPhoneNumber= common_util.formatNumbers(userPhoneNumber);
       Log.i(TAG,"Checking if account exists");
        //DatabaseReference node_users_uid = FirebaseDatabase.getInstance().getReference().child("account_link");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("account_link").document(userPhoneNumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists()){
                    accountTypeGetSet accountTypeGetSet = new accountTypeGetSet();
                    HashMap account =(HashMap) documentSnapshot.getData();
                    accountTypeGetSet.setMode(account.get("mode").toString());
                    accountTypeGetSet.setUsername(account.get("username").toString());
                    Log.i(TAG,"User account exists");
                    checkAccountType(accountTypeGetSet.getMode(), accountTypeGetSet.getUsername());
                }else{

                    Log.i(TAG,"User account does not exist");
                    common_util.progressDialogHide();
                    startActivity(new Intent(login_screen.this,portal_login.class));

                    finish();
                }

            }
        });

    }
    private void checkAccountType(final String mode, final String username){
    //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                 HashMap map = (HashMap) document.getData();
                studentDetails= common_util.hashMapStudentDetails(map);
                common_util.saveUserDataLocally(login_screen.this,studentDetails);
                common_util.saveUserDataLocally(login_screen.this,"Mode",mode);
                if(common_util.getUserDataLocally(login_screen.this,"initial")==null) {
                    uploadFcmToken(mode, username);
                }
             else {
                    common_util.saveUserDataLocally(login_screen.this,"initial","NO");
                    common_util.progressDialogHide();
                    startActivity(new Intent(login_screen.this, home_screen.class));
                    finish();

                }
                }
        });

    }

    private void uploadFcmToken(final String mode,final String username) {
        Log.i(TAG,mode);
        if(mode.equals("student")){
      uploadFirebaseTokenStudent(username);
    }
    else if((mode.equals("parent"))){
        uploadFirebaseTokenParent(username);
    }
    }


    private void displayError(Exception e, String errorcode, String message){

        common_util.ToasterShort(login_screen.this,"Code: "+message+" Error code: "+errorcode);

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.send_code: sendCode();
                break;
            case R.id.verify_code:verifyCode();
                break;
        }
    }


    private void verification_callback(){
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            beingverified=false;
            updateInstanceState();
            Log.d(TAG, "onVerificationCompleted:" + credential);
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            common_util.progressDialogHide();
            Log.w(TAG, "onVerificationFailed", e);
            beingverified=false;
            String errorcode="999";
            Log.i(TAG,e.toString()+"123");
            updateInstanceState();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
               errorcode="0001";
                common_util.errorReporting(TAG,errorcode,e);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                errorcode="0002";
                common_util.errorReporting(TAG,errorcode,e); }
            displayError(e,errorcode,getString(R.string.unabletosendcode));
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            common_util.progressDialogHide();
            updateUICodeSent();
            enableSms();
            checkforSms();
            Log.d(TAG, "onCodeSent:" + verificationId);
            mVerificationId = verificationId;
            mResendToken = token;

        }
    };
}
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            signInComplete();
                             FbUser = task.getResult().getUser();
                        } else {
                            common_util.progressDialogHide();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                String errorcode="0003";
                                common_util.errorReporting(TAG,errorcode,task.getException());
                                invalidCode();
                            }
                        }
                    }
                });
    }


    public void updateInstanceState(){
        Bundle onstate = new Bundle();
        onstate.putBoolean("beingverified",beingverified);
        onSaveInstanceState(onstate);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("beingverified",beingverified);
        if(beingverified)outState.putString("phonenumber",phoneNumber);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
       beingverified= savedInstanceState.getBoolean("beingverified");
       if(beingverified)
       phoneNumber= savedInstanceState.getString("phonenumber");
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FbUser = FbAuth.getCurrentUser();
        if(FbUser!=null){
            Log.i(TAG,"User Logged in");
            signInComplete();
        }else{
            splash_screen.setVisibility(View.GONE);
            if(beingverified){
                sendCode();

                code_entry.setVisibility(View.VISIBLE);
                verify_code.setVisibility(View.VISIBLE);
            }
            FbUser=null;
        }
    }
    @Override
    protected void onDestroy() {
        ComponentName receiver = new ComponentName(login_screen.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        ComponentName receiver = new ComponentName(login_screen.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        super.onPause();
    }
    @Override
    protected void onResume() {
        ComponentName receiver = new ComponentName(login_screen.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        super.onResume();
    }




    private void uploadFirebaseTokenParent(String username) {
       final String tokenForMessageing = FirebaseInstanceId.getInstance().getToken();
        ParentFirebaseTokens = new ArrayList<>();
        final DocumentReference dr = FirebaseFirestore.getInstance().collection("/users").document(username);
        final Map<String, Object> uploadData = new HashMap<>();

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.get("parentFirebaseTokens")==null) {
                    ParentFirebaseTokens.add(tokenForMessageing);
                    uploadData.put("parentFirebaseTokens", ParentFirebaseTokens);
                    dr.set(uploadData, SetOptions.merge());
                    Log.i(TAG, "uploaded data inside if(parent)");
                } else {
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ParentFirebaseTokens = (ArrayList<String>) task.getResult().get("parentFirebaseTokens");
                            ParentFirebaseTokens.add(tokenForMessageing);
                            uploadData.put("parentFirebaseTokens", ParentFirebaseTokens);
                            UploadToken(uploadData,dr);
                            Log.i(TAG, "uploaded data inside else(parent)");
                        }
                    });

                }
            }

        });

    }

    private void uploadFirebaseTokenStudent(String username) {

       final  String tokenForMessageing = FirebaseInstanceId.getInstance().getToken();
        StudentFirebaseTokens = new ArrayList<>();
        final DocumentReference dr = FirebaseFirestore.getInstance().collection("/users").document(username);
        final Map<String, Object> uploadData = new HashMap<>();

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.get("studentFirebaseTokens")==null) {
                    StudentFirebaseTokens.add(tokenForMessageing);
                    uploadData.put("studentFirebaseTokens", StudentFirebaseTokens);
                    dr.set(uploadData, SetOptions.merge());
                    Log.i(TAG, "uploaded data inside if(student)");
                } else {
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            StudentFirebaseTokens = (ArrayList<String>) task.getResult().get("studentFirebaseTokens");
                            StudentFirebaseTokens.add(tokenForMessageing);
                            uploadData.put("studentFirebaseTokens", StudentFirebaseTokens);
                            UploadToken(uploadData,dr);
                            Log.i(TAG, "uploaded data inside else(student)");
                        }
                    });

                }
            }

        });

    }
    public void UploadToken(Map uploadData,DocumentReference dr){
        dr.set(uploadData, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG,"TOKEN UPLOADED");
                common_util.progressDialogHide();
                startActivity(new Intent(login_screen.this, home_screen.class));
                finish();
            }
        });
    }





}
