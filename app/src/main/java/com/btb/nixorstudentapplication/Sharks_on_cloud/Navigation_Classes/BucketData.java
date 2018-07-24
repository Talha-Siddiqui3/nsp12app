package com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.BucketData_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Soc_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

public class BucketData {
    private BucketDataObject bucketDataObject;
    private ArrayList<BucketDataObject> bucketDataObjects;
    private common_util cu = new common_util();
    private ArrayList<String> photoUrlsImageViewver;
    BucketData_Adaptor bucketData_adaptor;
    Boolean isInitialData;

    public BucketData(Activity context, String userName) {
        Soc_Main.ShowLoading();
        RemovePreviousButtons();
        getBucketData(context, userName);
        isInitialData = true;
    }


    private static void AddPreviousButtons() {
        Buckets.bucketsButtons.setVisibility(View.VISIBLE);
        Buckets.my_Bucket.setVisibility(View.VISIBLE);

    }


    private void RemovePreviousButtons() {
        Buckets.bucketsButtons.setVisibility(View.GONE);
        Buckets.my_Bucket.setVisibility(View.GONE);
    }

/*
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
                if(Soc_Main.isCurrentlyRunning.equals("BucketData")) {
                    initializeAdaptorBucketData(bucketDataObjects);
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Soc_Main.HideLoading();
                cu.ToasterLong(context, "Error Retreiving data from server");
            }
        });
    }
*/

/*
    private void initializeAdaptorBucketData(ArrayList<BucketDataObject> bucketDataObjects) {
        Soc_Main.HideLoading();
        BucketData_Adaptor bucketData_adaptor = new BucketData_Adaptor(bucketDataObjects, photoUrlsImageViewver,Soc_Main.context);
        Soc_Main.setAdaptor_Generic(bucketData_adaptor);
    }
*/

    private void getBucketData(final Context context, final String userName) {
        bucketDataObjects = new ArrayList<>();
        photoUrlsImageViewver = new ArrayList<>();

        Soc_Main.socRoot.document(Subjects_homescreen.button_Selected).collection("Subjects").document(Buckets.subjectName)
                .collection("Users").document(userName).collection("Buckets").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || queryDocumentSnapshots.isEmpty()) { //if by latency empty bucket not deleted from firebase.
                    Soc_Main.HideLoading();
                    //cu.ToasterShort(context,"EMPTY BUCKET");
                }  else {

                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (isInitialData) {
                            genericGetData(dc);
                            Soc_Main.HideLoading();
                        } else {
                            genericGetData(dc);
                        }
                    }
                }

                initializeAdaptorBucketData(bucketDataObjects, photoUrlsImageViewver, isInitialData);
                isInitialData = false;
            }
        });
    }




    private void genericGetData(DocumentChange dc) {
        bucketDataObject = new BucketDataObject();
        if (dc.getDocument().getId().equals("Folders") || dc.getDocument().getId().equals("Folder Names")) {
            if (dc.getDocument().getId().equals("Folder Names")) {
                ArrayList<String> tempArray = new ArrayList<>();
                tempArray = (ArrayList<String>) dc.getDocument().get("FolderNames");
                for (int j = 0; j < tempArray.size(); j++) {
                    bucketDataObject = new BucketDataObject();
                    bucketDataObject.setName(tempArray.get(j));
                    bucketDataObject.setFolder(true);
                    if (isInitialData) {
                        bucketDataObjects.add(bucketDataObject);
                        photoUrlsImageViewver.add(null);
                    } else {
                        boolean found = false;
                        int k = 0;
                        int folderCount = 0;
                        while (k < bucketDataObjects.size() && found == false) {
                            if (bucketDataObjects.get(k).getName().equals(bucketDataObject.getName()) && bucketDataObjects.get(k).isFolder()) {
                                found = true;
                            }
                            if (bucketDataObjects.get(k).isFolder()) {
                                folderCount += 1;
                            }
                            k += 1;
                            Log.i("TEST", String.valueOf(found));
                        }
                        if (found == false || folderCount == 0) {
                            bucketDataObjects.add(bucketDataObject);
                            photoUrlsImageViewver.add(null);

                        }
                    }
                }
            }
        } else {
            bucketDataObject.setName(dc.getDocument().get("Name").toString());
            if(dc.getDocument().getTimestamp("Date")!=null) {
                Timestamp timestamp = dc.getDocument().getTimestamp("Date");
                bucketDataObject.setDate(timestamp.toDate());
            }
            if (dc.getDocument().get("PhotoUrlThumbnail") != null) {
                bucketDataObject.setPhotoUrlThumbnail(dc.getDocument().get("PhotoUrlThumbnail").toString());
            } else {
                bucketDataObject.setPhotoUrlThumbnail(null);

            }
            bucketDataObject.setFolder(false);
            if (isInitialData) {
                bucketDataObjects.add(bucketDataObject);
                if (dc.getDocument().get("PhotoUrlImageViewver") != null) {
                    photoUrlsImageViewver.add(dc.getDocument().get("PhotoUrlImageViewver").toString());
                } else {
                    photoUrlsImageViewver.add(null);
                }
            } else {
                boolean found = false;
                int k = 0;
                int fileCount = 0;
                while (k < bucketDataObjects.size() && found == false) {
                    if (bucketDataObjects.get(k).getName().equals(bucketDataObject.getName()) && !bucketDataObjects.get(k).isFolder()) {
                        found = true;
                        bucketDataObjects.get(k).setPhotoUrlThumbnail(bucketDataObject.getPhotoUrlThumbnail());
                        if (dc.getDocument().get("PhotoUrlImageViewver") != null) {
                            photoUrlsImageViewver.set(k, dc.getDocument().get("PhotoUrlImageViewver").toString());
                        } else {
                            photoUrlsImageViewver.add(null);
                        }
                    }
                    if (!bucketDataObjects.get(k).isFolder()) {
                        fileCount = fileCount + 1;
                    }
                    k += 1;
                }
                if (found == false || fileCount == 0) {
                    bucketDataObjects.add(bucketDataObject);
                    if (dc.getDocument().get("PhotoUrlImageViewver") != null) {
                        photoUrlsImageViewver.add(dc.getDocument().get("PhotoUrlImageViewver").toString());
                    } else {
                        photoUrlsImageViewver.add(null);
                    }
                }
            }
        }

    }


    private void initializeAdaptorBucketData(ArrayList<BucketDataObject> bucketDataObjects, ArrayList<String> photoUrls, Boolean isInitialData) {
        if (isInitialData) {
            bucketData_adaptor = new BucketData_Adaptor(bucketDataObjects, photoUrls, Soc_Main.context);
            Soc_Main.setAdaptor_Generic(bucketData_adaptor);
        } else {
            bucketData_adaptor.notifyDataSetChanged();
        }
    }


    //MARK: Main acitivty calls this
    public static void OnBackPressed() {
        Soc_Main.isCurrentlyRunning = "Buckets";//Providing back functionality for OnBackPressed in Soc_Main class(mainactvity).
        AddPreviousButtons();
        Buckets.initializeAdaptorAllBuckets(true);
    }
}
