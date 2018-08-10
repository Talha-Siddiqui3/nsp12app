package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.imageHelper;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Adaptors.BucketDataGridView_Adaptor;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Misc.ASync_Listener;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Misc.checkedListener;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Misc.image_upload_Async;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Buckets;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Navigation_Classes.Subjects_homescreen;
import com.btb.nixorstudentapplication.Sharks_on_cloud.Objects.BucketDataObject;
import com.btb.nixorstudentapplication.GeneralLayout.activity_header;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.btb.nixorstudentapplication.Application_Home.home_screen.db;
import static com.btb.nixorstudentapplication.Misc.UriToPath.getPathFromUri;
import static com.btb.nixorstudentapplication.Misc.imageHelper.compressScaledBitmap;

public class MyBucket extends AppCompatActivity implements View.OnClickListener {
    private CollectionReference bucketCr;
    private DocumentReference usernameCr;
    private common_util cu;
    private String year;
    private String subject;
    private String username;
    private Intent intent;
    private GridView gv;
    private FloatingActionButton newFolder;
    private FloatingActionButton uploadFile;
    private FloatingActionMenu menu;
    private ProgressBar loading;
    private AnimatedCircleLoadingView circleLoading;
    public final int GALLERY_INTENT = 2;
    private StorageReference mstorage;
    private ArrayList<BucketDataObject> bucketDataObjects = new ArrayList<>();
    private ArrayList<String> folderNames = new ArrayList<>();
    private ArrayList<String> bucketIds = new ArrayList<>();
    private ArrayList<String> photoUrlsImageViewver = new ArrayList<>();
    private BucketDataObject bucketDataObject;
    private BucketDataGridView_Adaptor bucketDataGridView_adaptor;
    private Boolean isInitialData = false;
    private Handler mHandler = new Handler();
    private DocumentReference foldersdoc;
    private DocumentReference folderNamesdoc;
    private Activity context;
    private boolean myBucket = false;
    private activity_header activity_header;
    private Button trashButton;
    /* NotificationManagerCompat notificationManager;
     NotificationCompat.Builder mBuilder;
     HashMap<Integer, Boolean> notificationsMap;
     int notificationCounter = -1;*/
    private permission_util pm = new permission_util();
    private WriteBatch writeBatchFiles = db.batch();
    private WriteBatch writeBatchFolders = db.batch();
    private String currentCollectionReferenece;
    private String bucketType;
    private boolean isWriteBatchFilesTrue = false;
    private boolean isWriteBatchFoldersTrue = false;


    //ImageSelector
    private int imageLimit = 50;
    private String TAG = "MYBUCKET";
    private final int imageWidthCompressed = 800;
    private final int imageWidthThumbnail = 200;
    private ArrayList<String> imagesUri = new ArrayList<String>();
    private ArrayList<String> imagesFilesNames = new ArrayList<String>();
    private int countImagesUploaded = 0;
    private HashMap<String, Integer> imagesMap;
    private HashMap<String, String> imagesTypeUrls;
    private int countImagesBatched = 0;
    private int countImagesFailedToUpload = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bucket);
        activity_header = findViewById(R.id.toolbar_top_BucketData);
        context = this;
        cu = new common_util();
        getIntentInfo();
        gv = findViewById(R.id.GridView_MyBucket);
        loading = findViewById(R.id.progressBar_myBucket);
        isInitialData = true;
        getPermissions();
        setFirebaseReferences();
        getBucketData();


    }

    private void getIntentInfo() {
        intent = getIntent();
        subject = intent.getStringExtra("subject");
        year = intent.getStringExtra("year");
        bucketType = intent.getStringExtra("type");
        if (bucketType.equals("myBucket")) {
            setMyBucket();
            username = cu.getUserDataLocally(this, "username");//TODO: CHANGE THIS
        } else {
            username = intent.getStringExtra("username");
            activity_header.setActivityname(username + "'s" + " bucket");
        }
    }

    private void setMyBucket() {
        activity_header.setActivityname("My Bucket");
        newFolder = findViewById(R.id.newFolder);
        uploadFile = findViewById(R.id.uploadFile);
        menu = findViewById(R.id.floating_menu);
        circleLoading = findViewById(R.id.circle_loading_view);
        trashButton = findViewById(R.id.trash_button_BucketData);
        trashButton.setOnClickListener(this);
        mstorage = FirebaseStorage.getInstance().getReference();
        setLoadingAnimationListener();
        setListeners();
        myBucket = true;
    }

    private void setLoadingAnimationListener() {
        circleLoading.setAnimationListener(new AnimatedCircleLoadingView.AnimationListener() {
            @Override
            public void onAnimationEnd(boolean success) {
                circleLoading.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void setFirebaseReferences() {
        bucketCr = generateBucketReference();
        usernameCr = FirebaseFirestore.getInstance().collection("SharksOnCloud").document(year).collection("Subjects").document(subject)
                .collection("Users").document(username);
        foldersdoc = bucketCr.document("Folders");
        folderNamesdoc = bucketCr.document("Folder Names");
    }


    private void getPermissions() {
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pm.getPermissions(this, permissions);

    }


    private void openImageSelector(int numberOfImagesToSelect) {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
        startActivityForResult(intent, GALLERY_INTENT);
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
                imageSelectedFile = new File(getPathFromUri(this, Uri.parse(uri)));

            } else {
                Log.i(TAG, "Selected file already exists");
            }

            Log.i(TAG, imageSelectedFile.getAbsolutePath());
            Bitmap compressedImage = imageHelper.compressImage(this, imageSelectedFile);
            compressedImage = imageHelper.scaleBitmap(compressedImage, imageWidthCompressed, imageWidthCompressed);
            Bitmap thumbnailImage = imageHelper.scaleBitmap(compressedImage, imageWidthThumbnail, imageWidthThumbnail);

            String filename = imagesFilesNames.get(imagesUri.indexOf(uri));
            uploadImageToFirebaseStorage(filename, thumbnailImage, compressedImage);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //MARK: Get download url for uploaded image
    private void getUploadedImageDownloadURL(StorageReference ref, final String uploadedImageNameType, final String uniqueFileName) {
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    imagesTypeUrls.put(uploadedImageNameType, task.getResult().toString());
                    prepareUrls(imagesTypeUrls, uniqueFileName);
                    countImagesUploaded++;
                } else {

                    //TODO:Display alert
                    cu.ToasterLong(MyBucket.this, "Upload Failed");
                }
            }
        });
    }


    //MARK: Method to upload each individual image to firebase storage
    private void uploadImageToFirebaseStorage(final String filename, Bitmap bmThumb, Bitmap bmCompressed) {
        //final NotificationClass nc = new NotificationClass(context, notificationCounter);
        final String uniqueFileName = FirebaseDatabase.getInstance().getReference().child("SOCPushIDS").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("SOCPushIDS").push().setValue("USED");
        imagesMap.put(uniqueFileName, 0);
        uploadCompressedImage(uniqueFileName, bmCompressed, filename);
        uploadThumbnailImage(uniqueFileName, bmThumb, filename);

    }

    private void uploadCompressedImage(final String uniqueFileName, Bitmap bm, final String filename) {
        final String uniqueFileNameCompressed = uniqueFileName + "_compressed";
        final String filenameCompressed = filename + "_Compressed";
        final StorageReference ref = mstorage.child("SOC").child(year).child(subject).child(username).child(uniqueFileNameCompressed);
        ref.putBytes(compressScaledBitmap(bm))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int i = imagesMap.get(uniqueFileName);
                        imagesMap.put(uniqueFileName, i + 1);
                        getUploadedImageDownloadURL(ref, uniqueFileNameCompressed, uniqueFileName);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        errorUploadingImage(exception, filenameCompressed);


                    }
                });
    }

    private void uploadThumbnailImage(final String uniqueFileName, Bitmap bm, final String filename) {
        final String uniqueFileNameThumb = uniqueFileName + "_thumb";
        final String filenameThumb = filename + "_Thumb";
        final StorageReference ref = mstorage.child("SOC").child(year).child(subject).child(username).child(uniqueFileNameThumb);
        ref.putBytes(compressScaledBitmap(bm))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        int i = imagesMap.get(uniqueFileName);
                        imagesMap.put(uniqueFileName, i + 1);
                        getUploadedImageDownloadURL(ref, uniqueFileNameThumb, uniqueFileName);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        errorUploadingImage(exception, filenameThumb);

                    }
                });
    }


    //MARK: Error handling for failed upload
    private void errorUploadingImage(Exception e, String filename) {
        //TODO: Display alert
        countImagesFailedToUpload++;
        Log.i("FAILED", String.valueOf(countImagesFailedToUpload));
    }


    //MARK: Upload initiate method.
    public void uploadSelectedImages() {

        if (imagesUri != null) {
            for (String imageUri : imagesUri) {
                processImage(imageUri);
            }
        }


    }

    private void allImagesUploaded() {
        //TODO: Alert all images uploaded


    }

    //TODO:FIX DATE ISSUE
    private void prepareUrls(HashMap<String, String> imagesTypeUrls, String uniqueFileName) {
        String thumbName = uniqueFileName + "_thumb";
        String compressedName = uniqueFileName + "_compressed";
        if (imagesMap.get(uniqueFileName) == 2 && imagesTypeUrls.containsKey(thumbName) && imagesTypeUrls.containsKey(compressedName)) {
            uploadUrls(imagesTypeUrls.get(compressedName), imagesTypeUrls.get(thumbName), uniqueFileName);
        }

    }

    private void uploadUrls(String compUrl, String thumbUrl, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Type", "image");
        map.put("PhotoUrlImageViewver", compUrl);
        map.put("PhotoUrlThumbnail", thumbUrl);
        map.put("Date", FieldValue.serverTimestamp());
        writeBatchFiles.set(bucketCr.document(name), map);
        countImagesBatched++;
        float currentPercent = ((float) countImagesBatched / (imagesUri.size() - countImagesFailedToUpload)) * 100;
        circleLoading.setPercent((int) currentPercent);
        if (countImagesBatched == imagesUri.size() - countImagesFailedToUpload) {
            writeBatchFiles.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    countImagesUploaded++;
                    if (countImagesUploaded == (imagesUri.size() * 2)) {
                        allImagesUploaded();
                    }

                    // loading.setVisibility(View.INVISIBLE);
                    writeBatchFiles = db.batch();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //TODO: Add alert for failed upload
                    cu.ToasterLong(MyBucket.this, "UNFORTUNATELY UPLOAD FAILED");
                }
            });

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null) {
            closeMenu();
            //loading.setVisibility(View.VISIBLE);
            circleLoading.setVisibility(View.VISIBLE);
            circleLoading.startDeterminate();
            circleLoading.setPercent(0);
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);


            imagesUri = new ArrayList<String>();
            imagesFilesNames = new ArrayList<String>();
            imagesMap = new HashMap<>();
            imagesTypeUrls = new HashMap<>();
            countImagesBatched = 0;
            countImagesFailedToUpload = 0;
            countImagesUploaded = 0;


            //We are basically getting the URI of each image the user has selected
            for (Image img : images) {
                imagesUri.add(img.path);
                imagesFilesNames.add(img.name);

            }

            //Image has been selected
            if (imagesUri.size() != 0) {
                //TODO:Display uploading images and add a cancel button
                image_upload_Async task = new image_upload_Async();
                task.imagesFilesNames = imagesFilesNames;
                task.imagesUri = imagesUri;
                task.context = new WeakReference<Activity>(context);
                task.aSync_listener = new ASync_Listener() {
                    @Override
                    public void onResponseReceive(String filename, Bitmap compressedImage, Bitmap thumbnailImage) {
                        uploadImageToFirebaseStorage(filename, thumbnailImage, compressedImage);
                        Log.i(TAG, "DONE");
                    }
                };
                task.execute();

                //uploadSelectedImages();

            }
        }
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
                openImageSelector(imageLimit);

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
                    folderNamesdoc.set(map, SetOptions.merge());


                } else {
                    ArrayList<String> folderNamesList = new ArrayList<>();
                    folderNamesList.add(folderName);
                    map.put("FolderNames", folderNamesList);
                    Timestamp tempTimestamp = new Timestamp(new Date(1754424836000L));
                    map.put("Date", tempTimestamp);
                    folderNamesdoc.set(map, SetOptions.merge());
                }
            }
        });

    }


    private void checkIfDummyFieldExists() {
        //    usernameCr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        //  @Override
        //public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        //          if (task.isSuccessful()) {
        //            DocumentSnapshot document = task.getResult();
        //          if (document.exists()) {
        //            //cu.ToasterLong(MyBucket.this, "Upload Completed Succesfully");
        // loading.setVisibility(View.INVISIBLE);
        //menu.setVisibility(View.VISIBLE);
        //  } else {
        AddDummyField();
        //}
        // } else {
        //   cu.ToasterLong(MyBucket.this, "Failed to connect to Server");
        //}
        //}
        //});
    }

    private void AddDummyField() {
        Map<String, Object> dummyMap = new HashMap<>();
        dummyMap.put("Dummy", "DUMMY");
        dummyMap.put("student_name",cu.getUserDataLocally(this,"name"));
        dummyMap.put("className",getStudentClass());
        usernameCr.set(dummyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loading.setVisibility(View.INVISIBLE);
                menu.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getStudentClass() {
        String studentClass="";
        List<String> subjects=new ArrayList<String>(cu.getUserDataLocallyHashSet(this,"student_subjects"));
        List<String> classes=new ArrayList<String>(cu.getUserDataLocallyHashSet(this,"student_classes"));
        Log.i("CHECK KR LOU",subjects.toString());
        Log.i("CHECK KR LOU",classes.toString());
        int classPos=subjects.indexOf(subject);
        studentClass=classes.get(classPos).substring(3);
        return  studentClass;
    }


    private void getBucketData() {

        bucketCr.orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.i("IMPORTANT", queryDocumentSnapshots.getMetadata().toString());
                if (e != null || queryDocumentSnapshots.isEmpty()) {
                    if (myBucket) {
                        checkIfDummyFieldExists();
                    }
                } else {
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


    private void deleteBucket() {
        Soc_Main.socRoot.document(Subjects_homescreen.button_Selected).collection("Subjects").document(Buckets.subjectName)
                .collection("Users").document(username).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    cu.ToasterShort(context, "Bucket Deleted Successfully");
                    MyBucket.super.onBackPressed();
                }
            }
        });
    }


    private void deleteItems() {
        for (int i = 0; i < bucketDataObjects.size(); i++) {
            if (bucketDataGridView_adaptor.getCheckedItem(i) == true) {
                Log.i("123", "CHECKED HAI");
                if (bucketDataObjects.get(i).isFolder()) {
                    isWriteBatchFoldersTrue = true;
                    HashMap<String, Object> tempmap = new HashMap<>();
                    folderNames.remove(bucketDataObjects.get(i).getName());
                    tempmap.put("FolderNames", folderNames);
                    writeBatchFolders.set(folderNamesdoc, tempmap, SetOptions.merge());
                } else {
                    isWriteBatchFilesTrue = true;
                    writeBatchFiles.delete(bucketCr.document(bucketDataObjects.get(i).getID()));
                }
            } else {
                Log.i("123", "CHECKED NHI");
            }
        }
        /*if (bucketDataObjects.size() == 1) {
            showLastItemWarning();
        }*///TODO: ASK HASSASN WHAT TO DO WHEN ALL ITEMS DELETED

    }

    private void showLastItemWarning() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBucket();
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


    private void genericGetData(DocumentSnapshot documentSnapshot) {

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

        } else {
            if (!bucketIds.contains(documentSnapshot.getId()) && documentSnapshot.getTimestamp("Date") != null) {
                bucketDataObject = new BucketDataObject();
                bucketDataObject.setName(setName(documentSnapshot));
                bucketDataObject.setDateTimeStamp(documentSnapshot.getTimestamp("Date"));
                bucketDataObject.setPhotoUrlThumbnail((String) documentSnapshot.get("PhotoUrlThumbnail"));
                bucketDataObject.setFolder(false);
                bucketDataObject.setID(documentSnapshot.getId());
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
            bucketDataGridView_adaptor = new BucketDataGridView_Adaptor(bucketDataObjects, photoUrls, context, currentCollectionReferenece,
                    year, subject, bucketType, username);
            bucketDataGridView_adaptor.checkedListener = new checkedListener() {
                @Override
                public void onChecked(boolean checkedState) {
                    if (checkedState == true) {
                        trashButton.setVisibility(View.VISIBLE);
                    }
                }
            };
            gv.setAdapter(bucketDataGridView_adaptor);
        } else {
            bucketDataGridView_adaptor.notifyDataSetChanged();
        }
        loading.setVisibility(View.INVISIBLE);
        if (myBucket) {
            menu.setVisibility(View.VISIBLE);
        }
    }


    private CollectionReference generateBucketReference() {
      /*  CollectionReference cr=FirebaseFirestore.getInstance().collection("SharksOnCloud").document(year).collection("Subjects").document(subject)
                .collection("Users").document(username).collection("Buckets");
      */
        CollectionReference cr;
        if (intent.getStringExtra("collectionReference") == null) {
            currentCollectionReferenece = "SharksOnCloud" + "/" + year + "/" + "Subjects" + "/" + subject + "/" + "Users" + "/" + username + "/" + "Buckets";
            cr = FirebaseFirestore.getInstance().collection(currentCollectionReferenece);
        } else {
            cr = FirebaseFirestore.getInstance().collection(intent.getStringExtra("collectionReference"));
            currentCollectionReferenece = intent.getStringExtra("collectionReference");
        }
        return cr;
    }


    public void closeMenu() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                menu.toggle(true);
            }
        }, 350);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.trash_button_BucketData) {
            showDeleteItemsWarning();
            cu.alterDialogListener = new common_util.alterDialogListener() {
                @Override
                public void getResult(String buttonClicked) {
                    handleDeletionAfterWarning(buttonClicked);
                }
            };


        }
    }

    private void handleDeletionAfterWarning(String buttonClicked) {
        if (buttonClicked.equals("pos")) {
            deleteItems();
            clearSelections();
            if (isWriteBatchFilesTrue) {
                Log.i("123", "Commiting files");
                writeBatchFiles.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cu.ToasterShort(MyBucket.this, "Deleted Files Successfully");
                        } else {
                            cu.ToasterShort(MyBucket.this, "Failed to delete files please try again");
                        }
                        writeBatchFiles = db.batch();
                        isWriteBatchFilesTrue = false;
                    }
                });
            }
            if (isWriteBatchFoldersTrue) {
                writeBatchFolders.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cu.ToasterShort(MyBucket.this, "Deleted Folders Successfully");
                        } else {
                            cu.ToasterShort(MyBucket.this, "Failed to delete files please try again");
                        }
                        writeBatchFolders = db.batch();
                        isWriteBatchFoldersTrue = false;
                    }

                });
            }

        }

    }

    private void showDeleteItemsWarning() {
        cu.showAlertDialogue("Yes", "null", "Cancel", this, "Warning", "Are you sure you want to delete all the selected items.?");
    }

    @Override
    public void onBackPressed() {
        if (trashButton==null) {
            super.onBackPressed();
        }

        else {
            if(trashButton.getVisibility() == View.VISIBLE) {
                clearSelections();
            }
            else{
                super.onBackPressed();
            }

        }
    }

    private void clearSelections() {
        bucketDataGridView_adaptor.resetCheckedItems();
        trashButton.setVisibility(View.GONE);
    }
}

