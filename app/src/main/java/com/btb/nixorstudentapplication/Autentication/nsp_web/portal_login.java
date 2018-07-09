package com.btb.nixorstudentapplication.Autentication.nsp_web;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class portal_login extends Activity implements View.OnClickListener {
//XML
EditText email_editText;
EditText password_editText;
TextView password_textView;
TextView email_textView;
Button login_button;
common_util common_util;
public static String result="";
public static StudentDetails Student_Profile=null;

String TAG = "portal_login";
private String email;
private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_login);
        common_util= new common_util();

        email_editText=findViewById(R.id.email_editText);
        password_editText=findViewById(R.id.password_editText);
        email_textView=findViewById(R.id.email_textView);
        password_textView=findViewById(R.id.password_textView);
        login_button=findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button: initiatelogin();
                break;
        }
    }

    private void initiatelogin() {
        if(!email_editText.getText().toString().equals("")&&!password_editText.getText().toString().equals("")){
Log.i("ABC",email_editText.getText().toString()+"123");
          email=  email_editText.getText().toString();
          email += getString(R.string.domain_textView);
            Log.i(TAG,email);
          password=  password_editText.getText().toString();
          String[] creds={email,password};
            portal_async login = new portal_async(this);
            login.execute(creds);

        }
        else{
            common_util.ToasterShort(this,"Please enter both email and passsword");
        }
    }


    public void connectUserEmail(FirebaseAuth mAuth, AuthCredential credential, final Context context){
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            // updateUI(user);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            common_util.ToasterShort(context,"Authentication failed.");

                        }

                        // ...
                    }
                });

    }


}
