package com.btb.nixorstudentapplication.Sharks_on_cloud.Misc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.imageHelper;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.btb.nixorstudentapplication.Application_Home.home_screen.db;
import static com.btb.nixorstudentapplication.Misc.UriToPath.getPathFromUri;

public class image_upload_Async extends AsyncTask<Void, Void, Void> {
    WriteBatch batch = db.batch();
    public final int GALLERY_INTENT = 2;
    private StorageReference mstorage;
    private common_util cu;
    //ImageSelector
    private String TAG = "MYBUCKET";


    private int imageWidthCompressed = 800;
    private int imageWidthThumbnail = 200;
    public ArrayList<String> imagesUri = new ArrayList<String>();
    public ArrayList<String> imagesFilesNames = new ArrayList<String>();
    public WeakReference<Activity> context;
    Bitmap compressedImage;
    Bitmap thumbnailImage;
    String filename;
public ASync_Listener aSync_listener;



    @Override
    protected Void doInBackground(Void... voids) {
        uploadSelectedImages();
        return null;
    }

    //MARK: Error handling for failed compression
    private void errorCompressingImage(String uri, Exception e) {
        //TODO: Handle image compression failed
        String nameofFailedImage = imagesFilesNames.get(imagesUri.indexOf(uri));

    }

    //MARK: Process the array of images and compress them
    private void processImage(String uri) {
        try {

            File imageSelectedFile = new File(uri);
            if (!imageSelectedFile.exists()) {
                imageSelectedFile = new File(getPathFromUri(context.get(), Uri.parse(uri)));

            } else {
                Log.i(TAG, "Selected file already exists");
            }

            Log.i(TAG, imageSelectedFile.getAbsolutePath());
            compressedImage = imageHelper.compressImage(context.get(), imageSelectedFile);
            compressedImage = imageHelper.scaleBitmap(compressedImage, imageWidthCompressed, imageWidthCompressed);
            thumbnailImage = imageHelper.scaleBitmap(compressedImage, imageWidthThumbnail, imageWidthThumbnail);

            filename = imagesFilesNames.get(imagesUri.indexOf(uri));
            aSync_listener.onResponseReceive(filename,compressedImage,thumbnailImage);
            //uploadImageToFirebaseStorage(filename, thumbnailImage, compressedImage);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //MARK: Upload initiate method.
    public void uploadSelectedImages() {

        if (imagesUri != null) {
            for (String imageUri : imagesUri) {
                processImage(imageUri);
            }
        }


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("123456789","ALL IMAGES COMPRESSED");
    }


}

