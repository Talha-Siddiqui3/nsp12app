package com.btb.nixorstudentapplication.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
            result= downloadImageNspUrl(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    public Bitmap downloadImageNspUrl(String URL_string) throws Exception{
        java.net.URL url = new URL(URL_string);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
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
