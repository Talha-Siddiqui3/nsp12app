package com.btb.nixorstudentapplication.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.mklimek.sslutilsandroid.SslUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
    Context activity;
    String username;

    public ImageDownloader(Context activity) {
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap result;
        username =strings[1];
        try {
            result= downloadImageNspUrl(strings[0],activity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    public Bitmap downloadImageNspUrl(String URL_string, Context context) throws Exception{
        java.net.URL url = new URL(URL_string);
        SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "nixor.cer");
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        UserPhoto photoClass = new UserPhoto();
        photoClass.uploadBitmapToFirebase(bitmap,username,activity);
        super.onPostExecute(bitmap);
    }
}
