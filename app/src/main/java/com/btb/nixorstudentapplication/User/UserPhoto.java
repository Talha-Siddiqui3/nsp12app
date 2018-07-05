package com.btb.nixorstudentapplication.User;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.btb.nixorstudentapplication.Application_Home.home_screen;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserPhoto {
    com.btb.nixorstudentapplication.Misc.common_util common_util = new common_util();

    String TAG ="UserPhoto";




    //Calls method to display photo on home screen
    public void setDisplay(String photourl,Context context){

        ((home_screen)context).setDisplay(photourl,context);
    }
    //Checks if Firebase storage reference present in Shared Pref
    public void getPhoto(String username,Context context){

        String photourl= common_util.getUserDataLocally(context,"photourl");
        if(photourl==null){
            checkIfPhotoSavedToFirebase(username,context);
            Log.i(TAG,"Not saved in shared pref");
        }else {
            Log.i(TAG,"Saved in shared pref");
            setDisplay(photourl,context);
        }
    }
    //Checks if copy saved in Firebase
    public void checkIfPhotoSavedToFirebase(final String username, final Context context){
        ((home_screen)context).db.collection("users").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if(documentSnapshot.get("photourl")!=null){
                    String photourl = documentSnapshot.get("photourl").toString();
                    setDisplay(photourl,context);
                    common_util.saveUserDataLocally(context,"photourl",photourl);
                }else{
                    getPhotoUrlNsp(username,context);
                }
            }
        });
    }
    //Gets photoUrl from Shared Pref and downloads it in the form of a bitmap
    public void getPhotoUrlNsp(String username, Context context){
        String photoUrl=  common_util.getUserDataLocally(context,"nsp_photo");
        if(photoUrl!=null){
            String[] params = {photoUrl,username};
            ImageDownloader imageDownloader = new ImageDownloader(context);
            imageDownloader.execute(params);
        }else
        {
            Log.i(TAG,"PhotoURL not in shared pref");
        }
    }
    //Bitmap is saved in Firebase
    public void uploadBitmapToFirebase(Bitmap bitmap, final String username, final Context context){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final StorageReference photoURLRef = storageRef.child("display_photo/"+username+"/dp.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = photoURLRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               String photourl = photoURLRef.getDownloadUrl().toString();
                ((home_screen)context).db.collection("users").document(username).update("photourl",photourl);
                 common_util.saveUserDataLocally(context,"photourl",photourl);
               setDisplay(photourl,context);
            }
        });
    }
}
