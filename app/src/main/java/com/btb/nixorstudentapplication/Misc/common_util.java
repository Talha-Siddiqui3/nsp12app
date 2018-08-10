package com.btb.nixorstudentapplication.Misc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.btb.nixorstudentapplication.Autentication.nsp_web.StudentDetails;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class common_util {
    static ProgressDialog pd;
    String TAG = "common_util";
    String buttonClicked = "";
    public alterDialogListener alterDialogListener;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public boolean checkPlayServices(Activity context, String error_message) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                Log.i("Log", "This device is not supported.");
                context.finish();
            }
            return false;
        }
        return true;


    }

    public static void errorReporting(String TAG, String errorcode, Exception e) {
        encode_text(TAG + " " + errorcode);
        encode_text(TAG + " " + e.getMessage());

    }

    public static String encode_text(String text) {
        text = "------" + text + "------";
        System.out.println(text);
        Log.i("ERROR:", text);
        return text;
    }

    public void ToasterLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void ToasterShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void progressDialogShow(Context context, String message) {
        pd = new ProgressDialog(context);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.show();
    }

    public void progressDialogHide() {
        if (pd != null) {
            if (pd.isShowing()) pd.dismiss();
        }
    }

    //3332230503 -- Correct
    //03332230503
    //+923332230503
    public String formatNumbers(String numberss) {

        if (numberss.length() == "+923342230503".length()) {
            numberss = numberss.substring(3);
        } else if (numberss.length() == "03432230503".length()) {
            numberss = numberss.substring(1);
        } else if (numberss.length() == "+12332230503".length()) {
            numberss = numberss.substring(2);
        } else if (numberss.length() == "8833332230503".length()) {
            numberss = numberss.substring(3);

        }
        return numberss;
    }

    public String extractUsername(Context context, String email) {
        String domain = context.getString(R.string.domain_textView);
        return email.replaceAll(domain, "").replace(".", "-");
    }


    public void checkActivation(final Activity activity, final String username) {
        final FirebaseDatabase da = FirebaseDatabase.getInstance();
        da.getReference().child("activated").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Context context = activity;

                    AlertDialog builder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert).create();
                    } else {
                        builder = new AlertDialog.Builder(context).create();
                    }
                    String alertTitle = context.getString(R.string.notActivatedAlertTitle);
                    String alertMessage = context.getString(R.string.notActivatedAlertMessage);
                    builder.setTitle(alertTitle);
                    builder.setMessage(alertMessage);

                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    });
                    if (dataSnapshot.child(username).getValue() != null) {
                        Log.i("Account", "activated");

                        if (builder != null) {
                            if (builder.isShowing()) {
                                builder.dismiss();
                            }
                        }

                    } else {
                        if (builder != null) {
                            builder.setCancelable(false);
                            builder.setOnKeyListener(new Dialog.OnKeyListener() {

                                @Override
                                public boolean onKey(DialogInterface arg0, int keyCode,
                                                     KeyEvent event) {
                                    // TODO Auto-generated method stub
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                                    }
                                    return true;
                                }
                            });
                            builder.show();
                            if (builder.isShowing()) {


                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void saveUserDataLocally(Context context, StudentDetails studentDetails) {
        String sharedPrefName = context.getString(R.string.SharedPref);
        String username = extractUsername(context, studentDetails.getStudent_email());
        SharedPreferences pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", studentDetails.getStudent_email()); // Storing Email
        editor.putString("username", username); // Storing Username
        editor.putString("name", studentDetails.getStudent_name()); // Storing Name
        editor.putString("student_id", studentDetails.getStudent_id()); // Storing studentID
        editor.putString("nsp_photo", studentDetails.getStudent_profileUrl()); // Storing NspPhoto
        editor.putString("GUID", studentDetails.getStudent_guid());// String Students's GUID
        editor.putString("year", studentDetails.getStudent_year());//Storing Student's Graduating Year
        //converting arraylist to set to store in sharedpref
        Set<String> studentSubjectSet = new LinkedHashSet<>();
        studentSubjectSet.addAll(studentDetails.getStudentSubjects());
        editor.putStringSet("student_subjects", studentSubjectSet);
        Set<String> studentClassSet = new LinkedHashSet<>();
        studentClassSet.addAll(studentDetails.getStudentClasses());
        Log.i("sfhkasjihfia",studentDetails.getStudentClasses().toString());
        Log.i("sfhkasjihfia",studentDetails.getStudentSubjects().toString());
        Log.i("sfhkasjihfia",studentClassSet.toString());
        Log.i("sfhkasjihfia",studentSubjectSet.toString());
        editor.putStringSet("student_classes", studentClassSet);
        editor.apply();
    }

    public void saveUserDataLocally(Context context, String key, String data) {
        String sharedPrefName = context.getString(R.string.SharedPref);
        SharedPreferences pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, data); // Storing data
        editor.apply();

    }

    public String getUserDataLocally(Context context, String data) {
        String result = "";
        String sharedPrefName = context.getString(R.string.SharedPref);
        SharedPreferences pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        Log.i(TAG, data);
        result = pref.getString(data, null);

        return result;
    }

    public Set<String> getUserDataLocallyHashSet(Context context, String data) {
        Set result;
        String sharedPrefName = context.getString(R.string.SharedPref);
        SharedPreferences pref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        Log.i(TAG, data);
        result = pref.getStringSet(data, null);

        return result;
    }


    public StudentDetails hashMapStudentDetails(HashMap map) {
        StudentDetails student = new StudentDetails();
        student.setStudent_email(map.get("studentEmail").toString());
        student.setStudent_house(map.get("studentHouse").toString());//TODO: FIX THIS
        student.setStudent_name(map.get("studentName").toString());
        student.setStudent_year(map.get("studentYear").toString());
        student.setStudent_id(map.get("studentID").toString());
        //NSP PHOTO NOT FIREBASE FOR FIREBASE USE "photourl"
        student.setStudent_profileUrl(map.get("profile_url").toString());
        student.setStudent_guid(map.get("GUID").toString());
        student.setStudentSubjects((ArrayList<String>) map.get("student_subjects"));
        student.setStudentClasses((ArrayList<String>) map.get("student_classes"));
        return student;
    }

    public AlertDialog showAlertDialogue(Activity activity, String title, String message) {

        Context context = activity;

        AlertDialog builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert).create();
        } else {
            builder = new AlertDialog.Builder(context).create();
        }

        builder.setTitle(title);
        builder.setMessage(message);

        return builder;


    }

   /* public String getYear(Activity context) {
        boolean AS = false;
        boolean A2 = false;
        String year = null;
        Set<String> set = getUserDataLocallyHashSet(context, "student_classes");
        List<String> studentClasses = new ArrayList<String>(set);
        for (int i = 0; i < studentClasses.size(); i++) {
            if (studentClasses.get(i).substring(3, 4).equals("1")) {
                AS = true;
            }
            if (studentClasses.get(i).substring(3, 4).equals("2")) {
                A2 = true;
            }
        }
        if (AS && A2) {
            year = "both";
        } else if (AS) {
            year = "AS";
        } else {
            year = "A2";
        }
        return year;
    }*/


    public void showAlertDialogue(String pos, String neutral, String neg, Context context, String title, String message) {
        buttonClicked = "";

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
       if(!pos.equals("null")) {
           alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, pos,
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           buttonClicked = "pos";
                           alterDialogListener.getResult(buttonClicked);
                       }
                   });
       }
        if(!neutral.equals("null")) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, neutral,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            buttonClicked = "neutral";
                            alterDialogListener.getResult(buttonClicked);
                        }
                    });
        }
        if(!neg.equals("null")) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, neg,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            buttonClicked = "neg";
                            alterDialogListener.getResult(buttonClicked);

                        }
                    });
        }
        alertDialog.show();

    }


    public interface alterDialogListener {
        void getResult(String buttonClicked);

    }

}




