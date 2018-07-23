package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.BucketData_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.BucketData;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MyBucket extends AppCompatActivity {
    private CollectionReference bucketCr;
    private DocumentReference usernameCr;
    private common_util cu;
    private String year;
    private String subject;
    private String username;
    private Intent i;
    private RecyclerView rv;
    private FloatingActionButton newFolder;
    private FloatingActionButton uploadFile;
    private FloatingActionMenu menu;
    private ProgressBar loading;
    public static final int GALLERY_INTENT = 2;
    private StorageReference mstorage;
    private ArrayList<BucketDataObject> bucketDataObjects;
    private ArrayList<String> photoUrlsImageViewver;
    private BucketDataObject bucketDataObject;
    private BucketData_Adaptor bucketData_adaptor;
    private Boolean isInitialData = false;
    private Handler mHandler = new Handler();
    DocumentReference foldersdoc;
    DocumentReference folderNamesdoc;
    boolean isDataRemoved = false;
    boolean isDataAdded = false;
    public static Activity context;
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder mBuilder;
    static HashMap<Integer,Boolean> notificationsMap=new HashMap<>();
    static int notificationCounter=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bucket);
        context = this;
        loading = findViewById(R.id.progressBar_myBucket);
        newFolder = findViewById(R.id.newFolder);
        uploadFile = findViewById(R.id.uploadFile);
        menu = findViewById(R.id.floating_menu);
        mstorage = FirebaseStorage.getInstance().getReference();
        rv = (RecyclerView) findViewById(R.id.mybucket_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setItemViewCacheSize(20);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        isInitialData = true;
        cu = new common_util();
        year = cu.getUserDataLocally(this, "year");
        setYear();
        username = cu.getUserDataLocally(this, "username");
        i = getIntent();
        subject = i.getStringExtra("subject");
        bucketCr = FirebaseFirestore.getInstance().collection("SharksOnCloud").document(year).collection("Subjects").document(subject)
                .collection("Users").document(username).collection("Buckets");
        usernameCr = FirebaseFirestore.getInstance().collection("SharksOnCloud").document(year).collection("Subjects").document(subject)
                .collection("Users").document(username);
        foldersdoc = bucketCr.document("Folders");
        folderNamesdoc = bucketCr.document("Folder Names");
        getBucketData();
        setListeners();



    }

    private void setListeners() {
        newFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFolderName();

            }
        });
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    private void getFolderName() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String folderName = input.getText().toString();
                uploadFolder(folderName);
                menu.toggle(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void uploadFolder(final String folderName) {
        final Map<String, Object> map = new HashMap<>();

        folderNamesdoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    ArrayList<String> folderNamesList = new ArrayList<>();
                    folderNamesList = (ArrayList<String>) task.getResult().get("FolderNames");
                    folderNamesList.add(folderName);
                    map.put("FolderNames", folderNamesList);
                    folderNamesdoc.set(map);

                } else {
                    ArrayList<String> folderNamesList = new ArrayList<>();
                    folderNamesList.add(folderName);
                    map.put("FolderNames", folderNamesList);
                    folderNamesdoc.set(map);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            notificationCounter+=1;
            notificationsMap.put(notificationCounter,true);
            closeMenu();
            final Uri uri = data.getData();
            final StorageReference ref = mstorage.child("SOC").child(year).child(subject).child(username).child(uri.getLastPathSegment());
            final UploadTask uploadTask = ref.putFile(uri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    boolean done = false;
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if (progress >= 100) {
                        done = true;
                    }
                    uploadNotification(uri.getLastPathSegment(), (int) progress, done,notificationCounter,notificationsMap.get(notificationCounter));
                   notificationsMap.put(notificationCounter,false);
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        uploadUrl(task.getResult().toString(), uri.getLastPathSegment());
                                    } else {
                                        cu.ToasterLong(MyBucket.this, "Upload Failed");
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cu.ToasterLong(MyBucket.this, "Upload Failed");
                }
            });
        }
    }

    //TODO:FIX DATE ISSUE
    private void uploadUrl(String url, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Type", "image");
        map.put("PhotoUrl", url);
        map.put("Data", FieldValue.serverTimestamp());
        bucketCr.document(name).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cu.ToasterLong(MyBucket.this, "UNFORTUNATELY UPLOAD FAILED");
            }
        });

    }

    private void checkIfDummyFieldExists() {
        usernameCr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //cu.ToasterLong(MyBucket.this, "Upload Completed Succesfully");
                        loading.setVisibility(View.INVISIBLE);
                        menu.setVisibility(View.VISIBLE);
                    } else {
                        AddDummyField();
                    }
                } else {
                    cu.ToasterLong(MyBucket.this, "Failed to connect to Server");
                }
            }
        });
    }

    private void AddDummyField() {
        Map<String, Object> dummyMap = new HashMap<>();
        dummyMap.put("Dummy", "DUMMY");
        usernameCr.set(dummyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loading.setVisibility(View.INVISIBLE);
                menu.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setYear() {
        if (year.equals("2020")) {
            year = "AS";
        } else {
            year = "A2";
        }
    }


    //Any way to not copy paste this? xD
    private void getBucketData() {
        bucketDataObjects = new ArrayList<>();
        photoUrlsImageViewver = new ArrayList<>();

        bucketCr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.i("IMPORTANT", queryDocumentSnapshots.getMetadata().toString());
                if (e != null || queryDocumentSnapshots.isEmpty()) {
                    checkIfDummyFieldExists();
                } else {

                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (isInitialData) {
                            genericGetData(dc);
                            loading.setVisibility(View.INVISIBLE);
                            menu.setVisibility(View.VISIBLE);
                        } else {
                            genericGetData(dc);
                        }
                    }
                }

                initializeAdaptorBucketData(bucketDataObjects, photoUrlsImageViewver, true);
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
            bucketDataObject.setDate((Date) (dc.getDocument().get("Date")));
            if (dc.getDocument().get("PhotoUrlThumbnail") != null) {
                bucketDataObject.setPhotoUrlThumbnail(dc.getDocument().get("PhotoUrlThumbnail").toString());
                Log.i("thumnail url:", dc.getDocument().get("PhotoUrlThumbnail").toString());
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
                        Log.i("ImageUrl", dc.getDocument().get("PhotoUrlImageViewver").toString());
                    } else {
                        photoUrlsImageViewver.add(null);
                    }
                }
            }
        }

    }

    private void initializeAdaptorBucketData(ArrayList<BucketDataObject> bucketDataObjects, ArrayList<String> photoUrls, Boolean isInitialData) {
        if (isInitialData) {
            bucketData_adaptor = new BucketData_Adaptor(bucketDataObjects, photoUrls, context);
            rv.setAdapter(bucketData_adaptor);
        } else {
            bucketData_adaptor.notifyDataSetChanged();
        }
    }

    public void closeMenu() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                menu.toggle(true);
            }
        }, 350);
    }

    public void uploadNotification(String name, int progress, boolean done, int notificationId,boolean initialNotification) {
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = progress;
        if (initialNotification) {
            notificationManager = NotificationManagerCompat.from(this);
            mBuilder = new NotificationCompat.Builder(this, "111");
            mBuilder.setContentTitle(name)
                    .setContentText("Uploading(0%)")
                    .setSmallIcon(R.drawable.shark_black)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

// Issue the initial notification with zero progress

            mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            notificationManager.notify(notificationId, mBuilder.build());
// Do the job here that tracks the progress.
// Usually, this should be in a worker thread


        } else {
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            mBuilder.setContentText("Uploading(" + progress + "%)");
            notificationManager.notify(notificationId, mBuilder.build());
        }

        if (done) {
// When done, update the notification one more time to remove the progress bar
            mBuilder.setContentText("Upload complete")
                    .setProgress(0, 0, false);
            notificationManager.notify(notificationId, mBuilder.build());
        }
    }


}
//TODO:FIX ALREADY EXIST UPLOADS