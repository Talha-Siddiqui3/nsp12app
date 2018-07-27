package com.btb.nixorstudentapplication.Carpool;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

public class CreateRide_Continued extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener,
        View.OnClickListener {
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
    private String TAG = "CreateRide_Continued";
    private common_util cu = new common_util();
    private ArrayList<HashMap<String, String>> routesDataList = new ArrayList<>();
    private permission_util pu = new permission_util();
    private SegmentedGroup segmentedGroupRoutes;

    //map
    final LatLng nixorMainLatLng = new LatLng(24.803999, 67.0587205);
    final LatLng nixorNCFPLatLng = new LatLng(24.7915153, 66.9960104);
    final String nixorMainLocationString = "24.803999,67.0587205";
    final String nixorNCFPLocationString = "24.7915153,66.9960104";
    private LatLng origin;
    String currentlySelectedCampus = "main";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    MarkerOptions nixorMainOptions;
    MarkerOptions nixorNCFPOptions;
    private Polyline oldPolyLine;
    private boolean initialPolyDraw = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride__continued);
        segmentedGroupRoutes = findViewById(R.id.segmented_routes);
        getPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setMarkerOptions();


    }

    private void setMarkerOptions() {
        nixorMainOptions = new MarkerOptions();
        nixorMainOptions.position(nixorMainLatLng);
        nixorNCFPOptions = new MarkerOptions();
        nixorNCFPOptions.position(nixorNCFPLatLng);
    }

    private void getPermission() {
        String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_NETWORK_STATE};
        pu.getPermissions(this, permissions);
    }


    public void getMapDataCloudFunction(String destination, String origin) {
        // Create the arguments to the callable function.
        routesDataList.clear();

        Map<String, Object> data = new HashMap<>();
        data.put("destination", destination);
        data.put("origin", origin);
        Log.i(TAG, "method executed123");

        mFunctions
                .getHttpsCallable("map_function")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        ArrayList routeList = (ArrayList) httpsCallableResult.getData();
                        JSONArray jRoutes = new JSONArray(routeList);

                        try {
                            for (int i = 0; i < jRoutes.length(); i++) {
                                Log.i("123", String.valueOf(i));
                                HashMap<String, String> routeData = new HashMap<>();
                                JSONArray jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                                String duration = String.valueOf(jLegs.getJSONObject(0).getJSONObject("duration").get("text"));
                                String distance = String.valueOf(jLegs.getJSONObject(0).getJSONObject("distance").get("text"));
                                String start_dest = String.valueOf(jLegs.getJSONObject(0).get("start_address"));
                                String end_dest = String.valueOf(jLegs.getJSONObject(0).get("end_address"));
                                String overview_polyline = (String) ((JSONObject) jRoutes.get(i)).getJSONObject("overview_polyline").get("points");
                                routeData.put("duration", duration);
                                routeData.put("distance", distance);
                                routeData.put("start_dest", start_dest);
                                routeData.put("overview_polyline", overview_polyline);
                                routesDataList.add(routeData);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (routesDataList.size() != 0) {
                            drawRoute(0);
                            addRouteButtons();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "FAIlED");
                        cu.ToasterLong(CreateRide_Continued.this, "Failed to connect to server");


                    }
                });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(TAG, "NO PERMISSION");
            return;
        }
        mMap = googleMap;
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            addInitialMarkers(mMap, location);
                            Log.i(TAG, "Got it");
                        } else {
                            Log.i(TAG, "NULL");
                        }
                    }
                });
        mMap.setOnMapClickListener(this);

    }

    @SuppressLint("MissingPermission") //TODO:FIX THIS
    private void addInitialMarkers(GoogleMap mMap, Location location) {
        //getting user current location in both latLng and String.
        origin = new LatLng(location.getLatitude(), location.getLongitude());
        final String originLocationString = location.getLatitude() + "," + location.getLongitude();
        mMap.setMyLocationEnabled(true);
//Setting camera boundires to ensure dynamic zooming.
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        //add the marker locations that you'd like to display on the map
        // boundsBuilder.include(origin);
        boundsBuilder.include(nixorMainLatLng);
        boundsBuilder.include(origin);
        final LatLngBounds bounds = boundsBuilder.build();

//marker options for orign and nixor
        MarkerOptions originOptions = new MarkerOptions();
        originOptions.position(origin);
        originOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(nixorMainOptions);
        mMap.addMarker(originOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
        mMap.moveCamera(cameraUpdate);
        mMap.animateCamera(cameraUpdate);

//gets the routes nad info from cloud func
        getMapDataCloudFunction(nixorMainLocationString, originLocationString);
        Log.i("123546", nixorMainLocationString + " " + originLocationString);
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void drawRoute(int routeNumber) {
        if (!initialPolyDraw) {
            oldPolyLine.remove();
        } else {
            initialPolyDraw = false;
        }
        PolylineOptions options = new PolylineOptions().width(13).color(Color.parseColor("#990000")).geodesic(true);
        String encodedString = routesDataList.get(routeNumber).get("overview_polyline");
        List<LatLng> list = decodePoly(encodedString);
        for (int z = 0; z < list.size(); z++) {
            LatLng point = list.get(z);
            options.add(point);
        }

        oldPolyLine = mMap.addPolyline(options);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    private void addRouteButtons() {
        for (int i = 0; i < routesDataList.size(); i++) {
            RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radio_button_dynamic, null);
            radioButton.setText("Route" + " " + i);
            radioButton.setId(i);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._12ssp));
            radioButton.setOnClickListener(this);
            RadioGroup.LayoutParams param = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.MATCH_PARENT, 1.0f);
            radioButton.setLayoutParams(param);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            segmentedGroupRoutes.addView(radioButton);

        }
        segmentedGroupRoutes.setTintColor(getResources().getColor(R.color.colorPrimary));
        segmentedGroupRoutes.updateBackground();
    }

    private void removeRouteButtons() {
        segmentedGroupRoutes.removeAllViews();
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        if (oldPolyLine != null) {
            oldPolyLine.remove();
        }
        final String originLocationString = latLng.latitude + "," + latLng.longitude;
        origin = latLng;

    }

    private void updateMarkers(){
        MarkerOptions originOptions = new MarkerOptions();
        originOptions.position(origin);
        originOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(originOptions);
        if (currentlySelectedCampus.equals("main")) {
            mMap.addMarker(nixorMainOptions);
        } else if (currentlySelectedCampus.equals("ncfp")) {
            mMap.addMarker(nixorNCFPOptions);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        removeRouteButtons();
       // getMapDataCloudFunction(nixorMainLocationString, originLocationString);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Main_Button_CreateRideContinued) {
            currentlySelectedCampus = "main";
        } else if (view.getId() == R.id.NCFP_Button_CreateRideContinued) {
            currentlySelectedCampus = "ncfp";
        }
else {
            drawRoute(view.getId());
        }
        }
}
