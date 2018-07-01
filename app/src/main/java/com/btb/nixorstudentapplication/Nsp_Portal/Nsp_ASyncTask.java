package com.btb.nixorstudentapplication.Nsp_Portal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.mklimek.sslutilsandroid.SslUtils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Nsp_ASyncTask extends AsyncTask<Void, Void, Void> {
    private com.btb.nixorstudentapplication.Misc.common_util common_util;
    private File file1;
    private Activity context;
    private String TAG = "Nsp_ASyncTask";
    private Exception GetDataxception;
    private common_util cu = new common_util();
    private AlertDialog alertDialog;
private String GUID;
private String data;

    public Nsp_ASyncTask(File file1, Activity context,String GUID,String data) {
        this.file1 = file1;
        this.context = context;
        this.GUID=GUID;
        this.data=data;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (file1.exists() == false) {

            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String URL_string = "https://nsp.braincrop.net/Students/Reports/"+GUID+"?meth="+data;
            SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "nixor.cer");
            java.net.URL url = new URL(URL_string);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(file1);

            byte[] buffer = new byte[1024];//Set buffer type
            int len1 = 0;//init length
            while ((len1 = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);//Write new file
            }

            //Close all connection after doing task
            fos.close();
            inputStream.close();

        } catch (Exception e) {
            GetDataxception = e;
            e.printStackTrace();
        }

        return null;
    }




    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (GetDataxception == null) {
            Intent i = new Intent(context, Nsp_Portal_PDF_LOADER.class);
            i.putExtra("FileName", data+".pdf");
            cu.progressDialogHide();
            context.startActivity(i);
            Log.i(TAG, "Everything Done");
        } else {

            Log.i(TAG, "Cannot connect to Portal");

                if (file1.length() > 0) {
                    cu.progressDialogHide();
                    Date lastModDate = new Date(file1.lastModified());
                    alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Unable to fetch new version");
                    alertDialog.setMessage("We are unable to connect to server, however we have an older version of the document (From "+lastModDate+").");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Show older version",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context, Nsp_Portal_PDF_LOADER.class);
                                    i.putExtra("FileName", data+".pdf");

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

                }
else{
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
}