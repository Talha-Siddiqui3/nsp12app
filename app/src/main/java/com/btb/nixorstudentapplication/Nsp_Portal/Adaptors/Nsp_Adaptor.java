package com.btb.nixorstudentapplication.Nsp_Portal.Adaptors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Nsp_Portal.NspASyncTask;
import com.btb.nixorstudentapplication.Nsp_Portal.NspPortalPdf;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.util.Log.i;

public class Nsp_Adaptor extends BaseAdapter implements View.OnClickListener {
    List<String> iconList = new ArrayList<>();
    private TextView txt;
    private ImageView img;
    private common_util cu = new common_util();
    private Activity context;
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    private Exception GetDataxception;
    String TAG = "Nsp_Adaptor";


    public Nsp_Adaptor(List<String> iconList, Activity context) {
        this.iconList = iconList;
        this.context = context;

    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case "Profile":
                GetNspData("Profile");
                break;
            case "Gate Attendance":
                GetNspData("Gate%20Attendance");
                break;
            case "Class Attendance":
                GetNspData("Attendance");
                break;
            case "Finance":
                GetNspData("Finance");
                break;
            case "Schedule":
                GetNspData("Schedule");
                break;
            case "Student Marks":
                GetNspData("Student%20Marks");
                break;
            case "CIE Grades":
                GetNspData("Grades");
                break;
            case "TA Schedule":
                cu.ToasterLong(context, "NOT AVAILABLE YET");
                break;
            case "TA Log":
                GetNspData("TA%20Log");
                break;
            default:
                cu.ToasterLong(context, "ERROR");
                break;

        }
    }

    @Override
    public int getCount() {
        return iconList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.nsp_layout_for_grid_view, parent, false);
        }
        txt = convertView.findViewById(R.id.icon_text_nsp_portal);
        img = convertView.findViewById(R.id.iconimage_nsp_portal);
        txt.setOnClickListener(this);
        img.setOnClickListener(this);
        txt.setText(iconList.get(position));
        img.setTag(iconList.get(position));
        txt.setTag(iconList.get(position));

        switch (iconList.get(position)) {
            case "Profile":
                img.setImageResource(R.drawable.newprofile);
                break;
            case "Gate Attendance":
                img.setImageResource(R.drawable.gateattendance);
                break;
            case "Class Attendance":
                img.setImageResource(R.drawable.newattendance);
                break;
            case "Finance":
                img.setImageResource(R.drawable.newfinance);
                break;
            case "Schedule":
                img.setImageResource(R.drawable.newschedule);
                break;
            case "Student Marks":
                img.setImageResource(R.drawable.newstudentmarks);
                break;
            case "CIE Grades":
                img.setImageResource(R.drawable.newstudentmarks);
                break;
            case "TA Schedule":
                img.setImageResource(R.drawable.taicon);
                break;
            case "TA Log":
                img.setImageResource(R.drawable.talogicon);
                break;
            default:
                img.setVisibility(View.INVISIBLE);
                break;


        }


        return convertView;
    }

    public void GetNspData(String data) {
        cu.progressDialogShow(context, "Please Wait");
        File file1 = new File(Environment.getExternalStorageDirectory() + "/nixorapp/NspDocuments/" + data + ".pdf");
        UpdateRequest(file1, home_screen.GUID, data);
    }

    //CALLING CLOUD FUNCTION
    public void UpdateRequest(final File file1, String GUID, final String data) {
        // Create the arguments to the callable function.
       // String URL_string = "https://nsp.braincrop.net/Students/Reports/" + GUID + "?meth=" + data;
        Map<String, Object> dataToSend = new HashMap<>();
        dataToSend.put("name", data);
        dataToSend.put("guid", GUID);
        Log.i(TAG, "method executed");

        mFunctions
                .getHttpsCallable("DocumentAccessFunc")
                .call(dataToSend)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        String encodedPdf = (String) httpsCallableResult.getData();
                        Log.i(TAG,encodedPdf);
                       if(encodedPdf==null || encodedPdf.equals("Error")){
                           showError(file1, data);
                       }
                       else {
                          generateFile(data, file1, encodedPdf);
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "FAIlED");
                        showError(file1, data);

                    }
                });


    }

    private void generateFile(String data, File file1, String encodedPdf) {
        try {
            byte[] pdfAsBytes = Base64.decode(encodedPdf, 0);
            FileOutputStream os;
            os = new FileOutputStream(file1, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            GetDataxception = e;
            e.printStackTrace();
        }
        openPdfViewer(data);
    }

    private void openPdfViewer(final String data) {
        final AlertDialog alertDialog;

        Intent i = new Intent(context, NspPortalPdf.class);
        i.putExtra("FileName", data + ".pdf");
        cu.progressDialogHide();
        context.startActivity(i);
        Log.i(TAG, "Everything Done");


    }

    private void showError(File file1, final String data) {
        final AlertDialog alertDialog;
        if (file1.length() > 0) {
            cu.progressDialogHide();
            Date lastModDate = new Date(file1.lastModified());
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Unable to fetch new version");
            alertDialog.setMessage("We are unable to connect to server, however we have an older version of the document (From " + lastModDate + ").");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Show older version",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(context, NspPortalPdf.class);
                            i.putExtra("FileName", data + ".pdf");

                            context.startActivity(i);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Never mind",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.hide();
                        }
                    });

            alertDialog.show();

        } else {
            cu.progressDialogHide();
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Unable to fetch new version");
            alertDialog.setMessage("We are unable to connect to server, please try again later");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.hide();
                        }
                    });
            alertDialog.show();

        }


    }


}
