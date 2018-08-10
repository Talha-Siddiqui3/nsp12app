/*
package com.btb.nixorstudentapplication.Carpool.Adaptors;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.btb.nixorstudentapplication.Carpool.Objects.CarpoolInfoObject;
import com.btb.nixorstudentapplication.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ramotion.foldingcell.FoldingCell;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Available_Rides_ListView_Adaptor extends ArrayAdapter<CarpoolInfoObject>   {
    private ArrayList<CarpoolInfoObject> carpoolInfoObjects;
    private HashMap<String, String> photoUrlMap;
    private Context context;
    private final LatLng nixorMainLatLng = new LatLng(24.803999, 67.0587205);
    private final LatLng nixorNCFPLatLng = new LatLng(24.7915316, 67.0660508);
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public Available_Rides_ListView_Adaptor(ArrayList<CarpoolInfoObject> carpoolInfoObjects, HashMap<String, String> photoUrlMap, Context context) {
        super(context, 0, carpoolInfoObjects);
        this.carpoolInfoObjects = carpoolInfoObjects;
        this.photoUrlMap = photoUrlMap;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.foldingview_availablerides_carpool, parent, false);

            viewHolder.availableSeatsFoldingTitle = cell.findViewById(R.id.seats_foldingTitle);
            viewHolder.priceFoldingTitle = cell.findViewById(R.id.price_foldingTitle);
            viewHolder.nameFoldingTitle = cell.findViewById(R.id.name_foldingTitle);
            viewHolder.dateFoldingTitle = cell.findViewById(R.id.date_folding_title);
            viewHolder.timeFoldingTitle = cell.findViewById(R.id.time_foldingTitle);
            viewHolder.studentIDFoldingTitle = cell.findViewById(R.id.student_id_foldingTitle);
            viewHolder.campusTypeFoldingTitle = cell.findViewById(R.id.campus_type_foldingTitle);
            viewHolder.displayPicFoldingTitle = cell.findViewById(R.id.displayPhoto_foldingTitle);

            //unfolded
            viewHolder.availableSeatsFoldingContent = cell.findViewById(R.id.seatsAvailable_textview_FoldingContent);
            viewHolder.priceFoldingContent = cell.findViewById(R.id.price_foldingcontent);
            viewHolder.nameFoldingContent = cell.findViewById(R.id.name_foldingContent);
            viewHolder.dateFoldingContent = cell.findViewById(R.id.date_folding_content);
            viewHolder.timeFoldingContent = cell.findViewById(R.id.time_foldingContent);
            viewHolder.studentIDFoldingContent = cell.findViewById(R.id.student_id_foldingContent);
            viewHolder.campusTypeFoldingContent = cell.findViewById(R.id.campus_type_foldingCOntent);
            viewHolder.displayPicFoldingContent = cell.findViewById(R.id.displayPhoto_foldingContetn);
            viewHolder.occupiedSeats = cell.findViewById(R.id.occupiedseats_textview_FoldingContent);
            viewHolder.totalSeats = cell.findViewById(R.id.totalseats_textview_FoldingContent);
            viewHolder.driverType = cell.findViewById(R.id.drivertype_textview_foldingContent);
            viewHolder.contact = cell.findViewById(R.id.contact_Textview_FoldingCOntent);
            viewHolder.mapView = (MapView) cell.findViewById(R.id.map_FoldingView);
            viewHolder.myMap.;
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }
        //bind data to adadpotr
        initializeMapView(viewHolder);

        return cell;
    }



    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }




    public void initializeMapView(ViewHolder viewHolder) {
        if (viewHolder.mapView != null) {
            viewHolder.mapView.onCreate(null);
            viewHolder.mapView.onResume();
            viewHolder.mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        setMapLocation();
    }

    public void setMapLocation() {
        myMap.clear();
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng origin = new LatLng(carpoolInfoObjects.get(getAdapterPosition()).getMyLat(), carpoolInfoObjects.get(getAdapterPosition()).getMyLong());
        LatLng destination = null;
        if (carpoolInfoObjects.get(getAdapterPosition()).getMainCampusOrNcfp().equals("Main")) {
            destination = nixorMainLatLng;
        } else {
            destination = nixorNCFPLatLng;
        }
        MarkerOptions originOptions = new MarkerOptions();
        originOptions.position(origin);
        myMap.addMarker(originOptions);
        MarkerOptions campusOptions = new MarkerOptions();
        campusOptions.position(destination);
        myMap.addMarker(campusOptions);
        CameraUpdate cameraUpdate = setMapBounds(origin, destination);
        myMap.moveCamera(cameraUpdate);
        myMap.animateCamera(cameraUpdate);
        drawRoute();
    }


    private CameraUpdate setMapBounds(LatLng origin, LatLng destination) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        //add the marker locations that you'd like to display on the map
        // boundsBuilder.include(origin);
        boundsBuilder.include(destination);
        boundsBuilder.include(origin);
        final LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, context.getResources().getDimensionPixelSize(R.dimen._316sdp),
                context.getResources().getDimensionPixelSize(R.dimen._130sdp), 150);
        return cameraUpdate;
    }

    private void drawRoute() {
        PolylineOptions options = new PolylineOptions().width(13).color(Color.parseColor("#990000")).geodesic(true);
        String encodedString = carpoolInfoObjects.get(getAdapterPosition()).getRoute();
        List<LatLng> list = decodePoly(encodedString);
        for (int z = 0; z < list.size(); z++) {
            LatLng point = list.get(z);
            options.add(point);
        }
        myMap.addPolyline(options);

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

















    // View lookup cache
    private static class ViewHolder{
        private TextView availableSeatsFoldingTitle;
        private TextView priceFoldingTitle;
        private TextView nameFoldingTitle;
        private TextView dateFoldingTitle;
        private TextView timeFoldingTitle;
        private TextView studentIDFoldingTitle;
        private TextView campusTypeFoldingTitle;
        private ImageView displayPicFoldingTitle;

        //unfolded
        private TextView availableSeatsFoldingContent;
        private TextView priceFoldingContent;
        private TextView nameFoldingContent;
        private TextView dateFoldingContent;
        private TextView timeFoldingContent;
        private TextView studentIDFoldingContent;
        private TextView campusTypeFoldingContent;
        private ImageView displayPicFoldingContent;
        private TextView occupiedSeats;
        private TextView totalSeats;
        private TextView driverType;
        private TextView contact;
        private GoogleMap myMap;
        private MapView mapView;




    }
}










*/
