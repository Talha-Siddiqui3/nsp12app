/*
package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.BucketDataGridView_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.BucketData_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class BucketData {
    private CollectionReference bucketCr;
    private ArrayList<BucketDataObject> bucketDataObjects = new ArrayList<>();
    private ArrayList<String> folderNames = new ArrayList<>();
    private ArrayList<String> bucketIds = new ArrayList<>();
    private ArrayList<String> photoUrlsImageViewver = new ArrayList<>();
    private BucketDataObject bucketDataObject;
    private BucketDataGridView_Adaptor bucketDataGridView_adaptor;
    private GridView gv;
    private common_util cu = new common_util();
    private Activity context;

    Boolean isInitialData;

    public BucketData(Activity context, String userName) {
        Soc_Main.ShowLoading();
        RemovePreviousButtons();
        getBucketData(userName);
        isInitialData = true;
        this.context=context;
    }


    private static void AddPreviousButtons() {
        Buckets.bucketsButtons.setVisibility(View.VISIBLE);
        Buckets.my_Bucket.setVisibility(View.VISIBLE);

    }


    private void RemovePreviousButtons() {
        Buckets.bucketsButtons.setVisibility(View.GONE);
        Buckets.my_Bucket.setVisibility(View.GONE);
    }



    private void getBucketData(final String userName) {
        bucketCr = FirebaseFirestore.getInstance().collection("SharksOnCloud").document(Buckets.yearSelected).collection("Subjects").document(Buckets.subjectName)
                .collection("Users").document(userName).collection("Buckets");
        bucketCr.orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.i("IMPORTANT", queryDocumentSnapshots.getMetadata().toString());
                if (e != null || !queryDocumentSnapshots.isEmpty()) {
                    bucketDataObjects.clear();
                    photoUrlsImageViewver.clear();
                    bucketIds.clear();
                    folderNames.clear();
                    for (DocumentSnapshot dc : queryDocumentSnapshots.getDocuments()) {
                        genericGetData(dc);
                    }
                }

                initializeAdaptorBucketData(bucketDataObjects, photoUrlsImageViewver, isInitialData);
                isInitialData = false;
            }
        });
    }


    private void genericGetData(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.getId().equals("Folders") || documentSnapshot.getId().equals("Folder Names")) {
            if (documentSnapshot.getId().equals("Folder Names")) {
                ArrayList<String> tempArray = new ArrayList<>();
                tempArray = (ArrayList<String>) documentSnapshot.get("FolderNames");
                for (int j = 0; j < tempArray.size(); j++) {
                    if (!folderNames.contains(tempArray.get(j))) {
                        bucketDataObject = new BucketDataObject();
                        bucketDataObject.setName(tempArray.get(j));
                        bucketDataObject.setFolder(true);
                        folderNames.add(tempArray.get(j));
                        bucketDataObjects.add(bucketDataObject);
                        photoUrlsImageViewver.add(null);
                    }
                }
            }
        } else {
            if (!bucketIds.contains(documentSnapshot.getId()) && documentSnapshot.getTimestamp("Date") != null) {
                bucketDataObject = new BucketDataObject();
                bucketDataObject.setName(setName(documentSnapshot));
                bucketDataObject.setDateTimeStamp(documentSnapshot.getTimestamp("Date"));
                bucketDataObject.setPhotoUrlThumbnail((String) documentSnapshot.get("PhotoUrlThumbnail"));
                bucketDataObject.setFolder(false);
                bucketDataObjects.add(bucketDataObject);
                photoUrlsImageViewver.add((String) documentSnapshot.get("PhotoUrlImageViewver"));
                bucketIds.add(documentSnapshot.getId());
            }
        }


    }

    private String setName(DocumentSnapshot documentSnapshot) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("d/MM/yyyy");
        Date dateValue = documentSnapshot.getTimestamp("Date").toDate();
        String dateString = dateFormatter.format(dateValue);
        return dateString;
    }

    private void initializeAdaptorBucketData(ArrayList<BucketDataObject> bucketDataObjects, ArrayList<String> photoUrls, Boolean isInitialData) {
        if (isInitialData) {
            bucketDataGridView_adaptor = new BucketDataGridView_Adaptor(bucketDataObjects, photoUrls, context);
            Soc_Main.setAdaptor_Generic_GridView(bucketDataGridView_adaptor);
            Soc_Main.HideLoading();
        } else {
            bucketDataGridView_adaptor.notifyDataSetChanged();
        }

    }







    //MARK: Main acitivty calls this
    public static void OnBackPressed() {
        Soc_Main.isCurrentlyRunning = "Buckets";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons();
        Buckets.initializeAdaptorAllBuckets(true);
    }
}
*/
