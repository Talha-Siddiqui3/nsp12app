package com.btb.nixorstudentapplication.Autentication.phone_verification;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Autentication.User.accountTypeGetSet;
import com.btb.nixorstudentapplication.Autentication.nsp_web.StudentDetails;
import com.btb.nixorstudentapplication.Autentication.nsp_web.portal_login;
import com.btb.nixorstudentapplication.Misc.KeyboardView;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.Misc.sms_broadcast.SmsListener;
import com.btb.nixorstudentapplication.Misc.sms_broadcast.SmsReceiver;
import com.btb.nixorstudentapplication.R;
import com.chaos.view.PinView;
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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class login_screen extends Activity {
    String TAG = "login_screen";
    FirebaseUser FbUser;
    StudentDetails studentDetails;
    static Boolean mShowingBack=false;
    permission_util permission_util;
    common_util common_util = new common_util();
    private FirebaseAuth FbAuth;
    protected static Boolean beingverified = false;
    protected static String phoneNumber;
    protected String code;
    protected PhoneAuthCredential credential;
    protected String mVerificationId;
    protected PhoneAuthProvider.ForceResendingToken mResendToken;


    //XML res
static TextView code_timer;
  static  KeyboardView keyboardView;
static PinView code_entry;
static Button send_code;
    //Permission
    private static String[] PERMISSIONS = {
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.READ_SMS,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new phoneNumberCard())
                    .commit();
        }

        //Intialize var
        permission_util = new permission_util();
            //XMl


        initialize();
    }
    public void initialize() {

        FbAuth = FirebaseAuth.getInstance();
        permission_util.getPermissions(login_screen.this, PERMISSIONS);



    }

    public  void enableSms(){
        ComponentName receiver = new ComponentName(login_screen.this, SmsReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.i("sms","Enabled broadcst receiver");

    }



    protected void flipCard() {
        Log.i("Flipping",mShowingBack.toString());
        if (mShowingBack) {
            mShowingBack = false;

            // Create and commit a new fragment transaction that adds the fragment for
            // the back of the card, uses custom animations, and is part of the fragment
            // manager's back stack.

            getFragmentManager()
                    .beginTransaction()

                    // Replace the default fragment animations with animator resources
                    // representing rotations when switching to the back of the card, as
                    // well as animator resources representing rotations when flipping
                    // back to the front (e.g. when the system Back button is pressed).
                    .setCustomAnimations(
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out)

                    // Replace any fragments currently in the container view with a
                    // fragment representing the next page (indicated by the
                    // just-incremented currentPage variable).
                    .replace(R.id.container, new phoneNumberCard())

                    // Add this transaction to the back stack, allowing users to press
                    // Back to get to the front of the card.
                    .addToBackStack(null)
                    // Commit the transaction.
                    .commit();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        getFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.container, new codeEntryCard())

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.
                .addToBackStack(null)
                // Commit the transaction.
                .commit();
    }

    protected void verifyCode(){

        if(code_entry.getText()!=null){
            code=code_entry.getText().toString();
            String loadinText = getString(R.string.phoneAuthVerifyCodeLoadingText);
            common_util.progressDialogShow(this,loadinText);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }

    }


    protected void updateUICodeSent(){
        flipCard();

        codeSentTimer();
    }
    private void codeSentTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                code_timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
                //send_code.setEnabled(false);
            }

            public void onFinish() {
              //  send_code.setEnabled(true);

                //Send code button enable
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


    private void invalidCode()
    {
common_util.ToasterShort(login_screen.this,getString(R.string.invalid_verif_code));
    }


    private void signInComplete(){
        String loginSuccessfulText = getString(R.string.phoneAuthSuccess);
        common_util.ToasterShort(login_screen.this,loginSuccessfulText);
        if(FbUser==null)FbUser=FirebaseAuth.getInstance().getCurrentUser();
        checkUserHistory(FbUser.getUid());
    }
    private void checkUserHistory(final String uid){
        String userPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
       userPhoneNumber= common_util.formatNumbers(userPhoneNumber);
        DatabaseReference node_users_uid = FirebaseDatabase.getInstance().getReference().child("account_link");
        node_users_uid.child(userPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    accountTypeGetSet accountTypeGetSet = new accountTypeGetSet();
                        HashMap account =(HashMap) dataSnapshot.getValue();
                        accountTypeGetSet.setMode(account.get("mode").toString());
                        accountTypeGetSet.setUsername(account.get("username").toString());
                  Log.i(TAG,"User account exists");
                  checkAccountType(accountTypeGetSet.getMode(), accountTypeGetSet.getUsername());
                }else{
                    Log.i(TAG,"User account does not exist");
                    startActivity(new Intent(login_screen.this,portal_login.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    private void checkAccountType(final String mode, final String username){
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference().child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        HashMap map = (HashMap) dataSnapshot.getValue();
       studentDetails= common_util.hashMapStudentDetails(map);
        common_util.saveUserDataLocally(login_screen.this,studentDetails);
        common_util.saveUserDataLocally(login_screen.this,"Mode",mode);
        startActivity(new Intent(login_screen.this, home_screen.class));
        finish();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    });

    }


    protected void displayError(Exception e, String errorcode, String message){

        common_util.ToasterShort(login_screen.this,"Code: "+message+" Error code: "+errorcode);

    }





    protected void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            common_util.progressDialogHide();
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


    protected void updateInstanceState(){
        Bundle onstate = new Bundle();
        onstate.putBoolean("beingverified",beingverified);
        onSaveInstanceState(onstate);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("beingverified",beingverified);
     if(phoneNumber!=null){
        if(beingverified)outState.putString("phonenumber",phoneNumber);}
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
            if(beingverified){
              // flipCard();
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


}
