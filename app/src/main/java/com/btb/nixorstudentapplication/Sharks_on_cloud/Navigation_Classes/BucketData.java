package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.BucketData_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class BucketData {
    private BucketDataObject bucketDataObject;
    private ArrayList<BucketDataObject> bucketDataObjects;
    private common_util cu = new common_util();
    private ArrayList<String> photoUrlsImageViewver;

    public BucketData(Activity context, String userName) {
        RemovePreviousButtons();
        getBucketData(context, userName);
    }


    private static void AddPreviousButtons() {
        Buckets.bucketsButtons.setVisibility(View.VISIBLE);
        Buckets.my_Bucket.setVisibility(View.VISIBLE);

    }


    private void RemovePreviousButtons() {
        Buckets.bucketsButtons.setVisibility(View.GONE);
        Buckets.my_Bucket.setVisibility(View.GONE);
    }


    private void getBucketData(final Activity context, String userName) {
        bucketDataObjects = new ArrayList<>();
        photoUrlsImageViewver =new ArrayList<>();
        Soc_Main.socRoot.document(Subjects_homescreen.button_Selected).collection("Subjects").document(Buckets.subjectName)
                .collection("Users").document(userName).collection("Buckets").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                    DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(i);
                    bucketDataObject = new BucketDataObject();
                    if (doc.getId().equals("Folders") || doc.getId().equals("Folder Names")) {
                        if (doc.getId().equals("Folder Names")) {
                            ArrayList<String> tempArray = new ArrayList<>();
                            tempArray = (ArrayList<String>) doc.get("FolderNames");
                            for (int j = 0; j < tempArray.size(); j++) {
                                bucketDataObject = new BucketDataObject();
                                bucketDataObject.setName(tempArray.get(j));
                                bucketDataObject.setFolder(true);
                                bucketDataObjects.add(bucketDataObject);
                                photoUrlsImageViewver.add(null);

                            }
                        }
                    } else {
                        bucketDataObject.setName(doc.get("Name").toString());
                        bucketDataObject.setDate((Date) (doc.get("Date")));
                        bucketDataObject.setPhotoUrlThumbnail(doc.get("PhotoUrlThumbnail").toString());
                        bucketDataObject.setFolder(false);
                        bucketDataObjects.add(bucketDataObject);
                        photoUrlsImageViewver.add(doc.get("PhotoUrlImageViewver").toString());
                    }
                }
                initializeAdaptorBucketData(bucketDataObjects);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cu.ToasterLong(context, "Error Retreiving data from server");
            }
        });
    }

    private void initializeAdaptorBucketData(ArrayList<BucketDataObject> bucketDataObjects) {
        BucketData_Adaptor bucketData_adaptor = new BucketData_Adaptor(bucketDataObjects, photoUrlsImageViewver,Soc_Main.context);
        Soc_Main.setAdaptor_Generic(bucketData_adaptor);
    }

    //MARK: Main acitivty calls this
    public static void OnBackPressed() {
        Soc_Main.isCurrentlyRunning = "Buckets";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons();
        Buckets.initializeAdaptorAllBuckets(true);
    }
}
