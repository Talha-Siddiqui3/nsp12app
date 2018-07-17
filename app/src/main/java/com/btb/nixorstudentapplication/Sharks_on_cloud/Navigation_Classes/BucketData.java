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
import java.util.HashMap;
import java.util.Map;

public class BucketData {
    private BucketDataObject bucketDataObject;
    private ArrayList<BucketDataObject> bucketDataObjects;
    private common_util cu = new common_util();
    private ArrayList<String> photoUrls;

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
        photoUrls =new ArrayList<>();
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
                                photoUrls.add(null);

                            }
                        }
                    } else {
                        bucketDataObject.setName(doc.get("Name").toString());
                        bucketDataObject.setDate((Date) (doc.get("Date")));
                        bucketDataObject.setPhotoUrl(doc.get("PhotoUrl").toString());
                        bucketDataObject.setFolder(false);
                        bucketDataObjects.add(bucketDataObject);
                        photoUrls.add(doc.get("PhotoUrl").toString());
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
        BucketData_Adaptor bucketData_adaptor = new BucketData_Adaptor(bucketDataObjects, photoUrls);
        Soc_Main.setAdaptor_Generic(bucketData_adaptor);
    }

    //MARK: Main acitivty calls this
    public static void OnBackPressed() {
        Soc_Main.isCurrentlyRunning = "Buckets";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons();
        Buckets.initializeAdaptorAllBuckets(true);
    }
}
