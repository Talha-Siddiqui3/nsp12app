package com.btb.nixorstudentapplication.Carpool.Fragments_For_Tabs;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.btb.nixorstudentapplication.Carpool.Adaptors.Available_Rides_Adaptor;
import com.btb.nixorstudentapplication.Carpool.Objects.CarpoolInfoObject;
import com.btb.nixorstudentapplication.Carpool.Rides_Filter;
import com.btb.nixorstudentapplication.Carpool.queryListener;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Available_Rides extends Fragment implements View.OnClickListener {
    private View view;
  public static CollectionReference ridesCr;
    public static Query initialQuery;
    private boolean isInitialData = true;
    private ArrayList docIds;
    private ArrayList<CarpoolInfoObject> carpoolInfoObjectsMain = new ArrayList<>();
    private ArrayList<CarpoolInfoObject> carpoolInfoObjectsNCFP = new ArrayList<>();
    private ArrayList<CarpoolInfoObject> carpoolInfoObjectsToSend = new ArrayList<>();


    private CarpoolInfoObject carpoolInfoObject;
    private common_util cu = new common_util();
    private Available_Rides_Adaptor available_rides_adaptor;
    private CollectionReference crUsers = FirebaseFirestore.getInstance().collection("/users");
    private HashMap<String, String> photoUrlMap = new HashMap<>();
    private Bundle savedInstanceState;
    private FusedLocationProviderClient mFusedLocationClient;
    private permission_util pu = new permission_util();
    private String TAG = "Available_Rides";
    private Location origin;
    private boolean gotLocation = false;
    private boolean gotProfileImages = false;
    private String CurrentButtonSelected = "MAIN";


    //xml
    private RecyclerView rv;
    private RadioButton mainButton;
    private RadioButton NCFPButton;
    private SegmentedGroup segmentedGroup;
    private ProgressBar loading;
    private EditText searchField;
    private ImageView filterButton;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.available_rides, container, false);
        ridesCr = FirebaseFirestore.getInstance().collection("/Carpool/Rides/AvailableRides");
        initialQuery = ridesCr;
        rv = (RecyclerView) view.findViewById(R.id.rides_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setMeasurementCacheEnabled(false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setDrawingCacheEnabled(true);
        rv.setHasFixedSize(true);
        getPermission();
        getPhotoUrls();
        mainButton = view.findViewById(R.id.MainCampus_Button_AvailableRides);
        NCFPButton = view.findViewById(R.id.NCFP_Button_AvaialbleRides);
        mainButton.setOnClickListener(this);
        NCFPButton.setOnClickListener(this);
        segmentedGroup = view.findViewById(R.id.segmnetedGroup_CampusType_AvaialbleRides);
        loading = view.findViewById(R.id.loading_avaialbleRides);
        searchField = view.findViewById(R.id.searchfield);
        filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(this);
        addSearchListener();
        addQueryListener();
        return view;

    }

    private void addQueryListener() {
        Rides_Filter.queryListener = new queryListener() {
            @Override
            public void getQuery(Query query) {
                getAvailableRides(query);
            }
        };
    }

    private void addSearchListener() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchField.getText().toString().toLowerCase((Locale.getDefault()));
                available_rides_adaptor.FilterResult(text);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSION", "NOT GRANTED");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            origin = location;
                            gotLocation = true;
                            getAvailableRides(initialQuery);
                            Log.i(TAG, "Got it");
                        } else {
                            Log.i(TAG, "NULL");
                            cu.showAlertDialogue(getActivity(), "ERROR", "Cant access location, please make sure your location is turned on and you are connected to intenet").show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cu.showAlertDialogue(getActivity(), "ERROR", "Cant access location, please make sure your location is turned on and you are connected to intenet").show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getPermission() {
        String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE};
        pu.getPermissions(getActivity(), permissions);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLocation();
    }


    private void getPhotoUrls() {
        crUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                    photoUrlMap.put(task.getResult().getDocuments().get(i).getId(), (String) task.getResult().getDocuments().get(i).get("photourl"));
                }
                gotProfileImages = true;
                getAvailableRides(initialQuery);
            }
        });
    }


    private void getAvailableRides(Query query) {
        if (gotLocation && gotProfileImages) {
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        cu.showAlertDialogue(getActivity(), "ERROR", "Error retrieving data form server, if problem persists please contact support.").show();
                        Log.i(TAG, e.toString());
                    } else {
                        docIds = new ArrayList();
                        carpoolInfoObjectsMain.clear();
                        carpoolInfoObjectsNCFP.clear();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            getDataGeneric(documentSnapshot);
                        }
                        sortData();
                        if (CurrentButtonSelected.equals("MAIN")) {
                            setAdaptor(isInitialData, carpoolInfoObjectsMain);
                        } else {
                            setAdaptor(isInitialData, carpoolInfoObjectsNCFP);
                        }
                        isInitialData = false;

                    }
                }
            });
        }
    }

    private void sortData() {
        Collections.sort(carpoolInfoObjectsMain, new Comparator<CarpoolInfoObject>() {
            @Override
            public int compare(CarpoolInfoObject lhs, CarpoolInfoObject rhs) {
                return Double.compare(lhs.getDistanceFromMyLocation(), (rhs.getDistanceFromMyLocation()));
            }
        });
        Collections.sort(carpoolInfoObjectsNCFP, new Comparator<CarpoolInfoObject>() {
            @Override
            public int compare(CarpoolInfoObject lhs, CarpoolInfoObject rhs) {
                return Double.compare(lhs.getDistanceFromMyLocation(), (rhs.getDistanceFromMyLocation()));
            }
        });

    }


    private void getDataGeneric(DocumentSnapshot documentSnapshot) {
        if (!docIds.contains(documentSnapshot.getId())) {
            carpoolInfoObject = addDataToObject(documentSnapshot);
            if (carpoolInfoObject != null) {
                if (carpoolInfoObject.getMainCampusOrNcfp().equals("Main")) {
                    carpoolInfoObjectsMain.add(carpoolInfoObject);
                } else {
                    carpoolInfoObjectsNCFP.add(carpoolInfoObject);
                }
            }
        }

    }

    private CarpoolInfoObject addDataToObject(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.get("route") != null && documentSnapshot.get("rideDuration") != null) {
            String currentData = "";
            CarpoolInfoObject carpoolInfoObject = new CarpoolInfoObject();

            carpoolInfoObject.setStudent_name(documentSnapshot.get("student_name").toString());
            carpoolInfoObject.setStudent_id(documentSnapshot.get("student_id").toString());
            carpoolInfoObject.setStudent_username(documentSnapshot.get("student_username").toString());
            carpoolInfoObject.setStudent_number(documentSnapshot.get("student_number").toString());
            carpoolInfoObject.setMainCampusOrNcfp(documentSnapshot.get("mainCampusOrNcfp").toString());
            carpoolInfoObject.setRoute(documentSnapshot.get("route").toString());
            carpoolInfoObject.setRideDuration(documentSnapshot.get("rideDuration").toString());
            carpoolInfoObject.setMyLat((Double) documentSnapshot.get("myLat"));
            carpoolInfoObject.setMyLong((Double) documentSnapshot.get("myLong"));
            carpoolInfoObject.setOccupiedSeats((int) (long) documentSnapshot.get("occupiedSeats"));
            carpoolInfoObject.setNumberOfSeats((int) (long) documentSnapshot.get("numberOfSeats"));
            carpoolInfoObject.setEstimatedCost((Double) documentSnapshot.get("estimatedCost"));
            carpoolInfoObject.setTotalDistance((Double) documentSnapshot.get("totalDistance"));
            carpoolInfoObject.setSelectedTime((Double) documentSnapshot.get("selectedTime"));
            carpoolInfoObject.setTimestamp((Timestamp) documentSnapshot.get("timestamp"));

            int availableSeatsTemp = carpoolInfoObject.getNumberOfSeats() - carpoolInfoObject.getOccupiedSeats();
            int tempPrice = (int) Math.ceil(carpoolInfoObject.getEstimatedCost() / (carpoolInfoObject.getOccupiedSeats() + 1));
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");
            Date timeValue = new Date(((long) carpoolInfoObject.getSelectedTime() * 1000L));
            String timeString = timeFormatter.format(timeValue);
            Log.i("123", timeString + "123");

            carpoolInfoObject.setTimeString(timeString);
            carpoolInfoObject.setPriceString("RS:" + String.valueOf(tempPrice));
            carpoolInfoObject.setAvailableSeatsString(String.valueOf(availableSeatsTemp) + " seats");

            currentData = String.valueOf(carpoolInfoObject.getAvailableSeatsString()) + ";" + carpoolInfoObject.getPriceString() + ";" + carpoolInfoObject.getStudent_name() + ";" +
                    carpoolInfoObject.getStudent_id() + ";" +
                    timeString + ";";

            //imp values for if else
            carpoolInfoObject.setPrivateCarOrTaxi(documentSnapshot.get("privateCarOrTaxi").toString());
            carpoolInfoObject.setOneTimeOrScheduled(documentSnapshot.get("oneTimeOrScheduled").toString());
            if (carpoolInfoObject.getPrivateCarOrTaxi().equals("privateCar")) {
                carpoolInfoObject.setiAmTheDriver((Boolean) documentSnapshot.get("iAmTheDriver"));
                currentData += "Me" + ";";
            } else {
                currentData += "Other" + ";";
            }
            if (carpoolInfoObject.getOneTimeOrScheduled().equals("scheduled")) {
                carpoolInfoObject.setSelectedDays((HashMap<String, Boolean>) documentSnapshot.get("selectedDays"));
                carpoolInfoObject.setDaysAvailableString(getDays(carpoolInfoObject.getSelectedDays()).get("daysAvailableShort"));
                currentData += (getDays(carpoolInfoObject.getSelectedDays())).get("daysAvailableFull") + ";";
            }
            if (carpoolInfoObject.getOneTimeOrScheduled().equals("once")) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
                Date dateValue = new Date(((long) carpoolInfoObject.getSelectedTime() * 1000L));
                ///  String dateString = dateFormatter.format(new Date(Double.doubleToLongBits(originalCarpoolInfoObjects.get(position).getSelectedTime()) * 1000L));
                String dateString = dateFormatter.format(dateValue);
                carpoolInfoObject.setDateString(dateString);
                currentData += dateString + ";";
            }

            carpoolInfoObject.setDistanceFromMyLocation(getDistanceFromMyLocation(carpoolInfoObject));
            carpoolInfoObject.setStringifiedCurrentData(currentData);
            return carpoolInfoObject;
        } else {
            return null;
        }
    }


    public HashMap<String, String> getDays(HashMap<String, Boolean> dayArray) {

        String daysAvailableShort = "";
        String daysAvailableFull = "";

        if (dayArray.get("Monday")) {
            daysAvailableShort += ", Mon";
            daysAvailableFull += ", Monday";
        }
        if (dayArray.get("Tuesday")) {
            daysAvailableShort += ", Tue";
            daysAvailableFull += ", Tuesday";
        }
        if (dayArray.get("Wednesday")) {
            daysAvailableShort += ", Wed";
            daysAvailableFull += ", Wednesday";
        }
        if (dayArray.get("Thursday")) {
            daysAvailableShort += ", Thu";
            daysAvailableFull += ", Thursday";
        }
        if (dayArray.get("Friday")) {
            daysAvailableShort += ", Fri";
            daysAvailableFull += ", Friday";
        }
        if (dayArray.get("Saturday")) {
            daysAvailableShort += ", Sat";
            daysAvailableFull += ", Saturday";
        }
        if (dayArray.get("Sunday")) {
            daysAvailableShort += ", Sun";
            daysAvailableFull += ", Sunday";
        }
        daysAvailableShort = daysAvailableShort.substring(1).trim();
        daysAvailableFull = daysAvailableFull.substring(1).trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("daysAvailableShort", daysAvailableShort);
        map.put("daysAvailableFull", daysAvailableFull);

        return map;


    }


    private double getDistanceFromMyLocation(CarpoolInfoObject carpoolInfoObject) {
        Location destination = new Location("");
        destination.setLatitude(carpoolInfoObject.getMyLat());
        destination.setLongitude(carpoolInfoObject.getMyLong());
        double distanceFromMyLocation = origin.distanceTo(destination);
        return distanceFromMyLocation;
    }


    private void setAdaptor(boolean isInitialData, ArrayList<CarpoolInfoObject> Data) {
        carpoolInfoObjectsToSend.clear();
        carpoolInfoObjectsToSend.addAll(Data);
        if (isInitialData) {
            available_rides_adaptor = new Available_Rides_Adaptor(carpoolInfoObjectsToSend, photoUrlMap, getContext());
            available_rides_adaptor.setHasStableIds(true);
            rv.setAdapter(available_rides_adaptor);
            loading.setVisibility(View.GONE);
            segmentedGroup.setVisibility(View.VISIBLE);
        } else {
            available_rides_adaptor.updateData();
            Log.i("ELSE EXECUTED", "ELSE");
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.MainCampus_Button_AvailableRides) {
            CurrentButtonSelected = "MAIN";
            setAdaptor(false, carpoolInfoObjectsMain);

        } else if (view.getId() == R.id.NCFP_Button_AvaialbleRides) {
            setAdaptor(false, carpoolInfoObjectsNCFP);
            CurrentButtonSelected = "NCFP";
        } else if (view.getId() == R.id.filterButton) {
            startActivity(new Intent(getContext(), Rides_Filter.class));
        }
    }
}


























