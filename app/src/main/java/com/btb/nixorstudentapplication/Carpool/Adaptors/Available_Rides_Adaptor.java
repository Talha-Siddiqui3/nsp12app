package com.btb.nixorstudentapplication.Carpool.Adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btb.nixorstudentapplication.Carpool.Objects.CarpoolInfoObject;
import com.btb.nixorstudentapplication.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ramotion.foldingcell.FoldingCell;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Available_Rides_Adaptor extends RecyclerView.Adapter<Available_Rides_Adaptor.Rv_ViewHolder> {
    private ArrayList<CarpoolInfoObject> originalCarpoolInfoObjects;
    private HashMap<String, String> photoUrlMap;
    private Context context;
    private final LatLng nixorMainLatLng = new LatLng(24.803999, 67.0587205);
    private final LatLng nixorNCFPLatLng = new LatLng(24.7915316, 67.0660508);
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private String currentData;
    private ArrayList<CarpoolInfoObject> newCarpoolInfoObjects = new ArrayList<>();

    @Override
    public long getItemId(int position) {
        if (newCarpoolInfoObjects.size() != 0) {
            return (long) newCarpoolInfoObjects.get(position).getTimestamp().getNanoseconds();
        } else {
            return 0;
        }
    }

    public Available_Rides_Adaptor(ArrayList<CarpoolInfoObject> carpoolInfoObjects, HashMap<String, String> photoUrlMap, Context context) {
        this.originalCarpoolInfoObjects = carpoolInfoObjects;
        newCarpoolInfoObjects.addAll(this.originalCarpoolInfoObjects);
        this.photoUrlMap = photoUrlMap;
        this.context = context;

    }


    @NonNull
    @Override
    public Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.foldingview_availablerides_carpool, parent, false);
        return new Rv_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rv_ViewHolder holder, int position) {


        if (unfoldedIndexes.contains(position)) {
            holder.fc.unfold(true);
        } else {
            holder.fc.fold(true);
        }
        setFoldedView(holder, position);
        setUnfoldedView(holder, position);


    }

    private void setUnfoldedView(final Rv_ViewHolder holder, final int position) {
        if (holder.myMap != null) {
            holder.setMapLocation();
        }


        Glide.with(context).load(photoUrlMap.get(newCarpoolInfoObjects.get(position).getStudent_username())).into(holder.displayPicFoldingContent);
        int availableSeatsTemp = newCarpoolInfoObjects.get(position).getNumberOfSeats() - newCarpoolInfoObjects.get(position).getOccupiedSeats();
        int tempPrice = (int) Math.ceil(newCarpoolInfoObjects.get(position).getEstimatedCost() / (newCarpoolInfoObjects.get(position).getOccupiedSeats() + 1));
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh.mm aa");
        Date timeValue = new Date(((long) newCarpoolInfoObjects.get(position).getSelectedTime() * 1000L));
        String timeString = timeFormatter.format(timeValue);
        holder.availableSeatsFoldingContent.setText(String.valueOf(availableSeatsTemp));
        holder.priceFoldingContent.setText("RS:" + String.valueOf(tempPrice));
        holder.nameFoldingContent.setText(newCarpoolInfoObjects.get(position).getStudent_name());
        holder.studentIDFoldingContent.setText(newCarpoolInfoObjects.get(position).getStudent_id());
        holder.campusTypeFoldingContent.setText(newCarpoolInfoObjects.get(position).getMainCampusOrNcfp() + " Campus");
        holder.timeFoldingContent.setText(timeString);

        if (newCarpoolInfoObjects.get(position).getOneTimeOrScheduled().equals("once")) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            Date dateValue = new Date(((long) newCarpoolInfoObjects.get(position).getSelectedTime() * 1000L));
            ///  String dateString = dateFormatter.format(new Date(Double.doubleToLongBits(originalCarpoolInfoObjects.get(position).getSelectedTime()) * 1000L));
            String dateString = dateFormatter.format(dateValue);
            holder.dateFoldingContent.setText(dateString);
            currentData += dateString;
        } else {
            holder.dateFoldingContent.setText(newCarpoolInfoObjects.get(position).getDaysAvailableString());
            currentData += (newCarpoolInfoObjects.get(position).getDaysAvailableString());
        }
        if (newCarpoolInfoObjects.get(position).getPrivateCarOrTaxi().equals("privateCar")) {
            holder.driverType.setText("Me");
            currentData += "Me";
        } else {
            holder.driverType.setText("Other");
            currentData += "Other";
        }
        holder.occupiedSeats.setText(String.valueOf(newCarpoolInfoObjects.get(position).getOccupiedSeats()));
        holder.totalSeats.setText(String.valueOf(newCarpoolInfoObjects.get(position).getNumberOfSeats()));
        holder.contact.setText(String.valueOf(newCarpoolInfoObjects.get(position).getStudent_number()));


    }

    private void setFoldedView(final Rv_ViewHolder holder, int position) {

        Glide.with(context).load(photoUrlMap.get(newCarpoolInfoObjects.get(position).getStudent_username())).into(holder.displayPicFoldingTitle);
        holder.availableSeatsFoldingTitle.setText(newCarpoolInfoObjects.get(position).getAvailableSeatsString());
        holder.priceFoldingTitle.setText(newCarpoolInfoObjects.get(position).getPriceString());
        holder.nameFoldingTitle.setText(newCarpoolInfoObjects.get(position).getStudent_name());
        Log.i("NAME", newCarpoolInfoObjects.get(position).getStudent_name().toString());
        holder.studentIDFoldingTitle.setText(newCarpoolInfoObjects.get(position).getStudent_id());
        holder.campusTypeFoldingTitle.setText(newCarpoolInfoObjects.get(position).getMainCampusOrNcfp() + " Campus");
        holder.timeFoldingTitle.setText(newCarpoolInfoObjects.get(position).getTimeString());

        if (newCarpoolInfoObjects.get(position).getOneTimeOrScheduled().equals("once")) {
            holder.dateFoldingTitle.setText(newCarpoolInfoObjects.get(position).getDateString());
        } else {
            holder.dateFoldingTitle.setText(newCarpoolInfoObjects.get(position).getDaysAvailableString());
        }

    }


    @Override
    public void onViewRecycled(@NonNull Rv_ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.myMap != null) {
            holder.myMap.clear();
            holder.myMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
    }

    @Override
    public int getItemCount() {
        return newCarpoolInfoObjects.size();
    }

  /*  public String getDays(HashMap<String, Boolean> dayArray) {

        String daysAvailable = "";

        if (dayArray.get("Monday")) {
            daysAvailable += ", Mon";
        }
        if (dayArray.get("Tuesday")) {
            daysAvailable += ", Tue";
        }
        if (dayArray.get("Wednesday")) {
            daysAvailable += ", Wed";
        }
        if (dayArray.get("Thursday")) {
            daysAvailable += ", Thu";
        }
        if (dayArray.get("Friday")) {
            daysAvailable += ", Fri";
        }
        if (dayArray.get("Saturday")) {
            daysAvailable += ", Sat";
        }
        if (dayArray.get("Sunday")) {
            daysAvailable += ", Sun";
        }
        daysAvailable = daysAvailable.substring(1).trim();
        return daysAvailable;


    }
*/

    public void FilterResult(String searchQuery) {
        unfoldedIndexes.clear();
        if (searchQuery.length() == 0) {
            newCarpoolInfoObjects.clear();
            newCarpoolInfoObjects.addAll(originalCarpoolInfoObjects);
        } else {
            newCarpoolInfoObjects.clear();
            for (int i = 0; i < originalCarpoolInfoObjects.size(); i++) {
                boolean show = true;
                List<String> tempDataArray = Arrays.asList(originalCarpoolInfoObjects.get(i).getStringifiedCurrentData().toLowerCase().split(";"));
                List<String> searchQueryArray = Arrays.asList(searchQuery.split("\\s+(?!seats)(?!am)"));
                Log.i("asdasdasd", tempDataArray.toString());
                Log.i("asdasdasd", searchQueryArray.toString());
                show = searchDataForQuery(tempDataArray, searchQueryArray);
//[^\S+am]
                if (show) {
                    newCarpoolInfoObjects.add(originalCarpoolInfoObjects.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    private boolean searchDataForQuery(List<String> tempDataArray, List<String> searchQueryArray) {
        boolean show = true;
        int i = 0;
        while (i < searchQueryArray.size() && show == true) {
            show=getContains(tempDataArray,searchQueryArray,i);
            i+=1;
        }
        return show;
    }


    public boolean getContains(List<String> tempDataArray, List<String> searchQueryArray,int i){
        int j = 0;
        boolean contains=false;
        while (j < tempDataArray.size() && contains == false) {
            if (tempDataArray.get(j).contains(searchQueryArray.get(i))) {
                contains=true;
            }
            j+=1;
        }
        return contains;
    }


    public void updateData() {
        newCarpoolInfoObjects.clear();
        newCarpoolInfoObjects.addAll(originalCarpoolInfoObjects);
        notifyDataSetChanged();
    }


    class Rv_ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        private TextView availableSeatsFoldingTitle;
        private TextView priceFoldingTitle;
        private TextView nameFoldingTitle;
        private TextView dateFoldingTitle;
        private TextView timeFoldingTitle;
        private TextView studentIDFoldingTitle;
        private TextView campusTypeFoldingTitle;
        private ImageView displayPicFoldingTitle;
        private Handler mHandler = new Handler();

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
        private FoldingCell fc;
        private GoogleMap myMap;
        private MapView mapView;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            availableSeatsFoldingTitle = itemView.findViewById(R.id.seats_foldingTitle);
            priceFoldingTitle = itemView.findViewById(R.id.price_foldingTitle);
            nameFoldingTitle = itemView.findViewById(R.id.name_foldingTitle);
            dateFoldingTitle = itemView.findViewById(R.id.date_folding_title);
            timeFoldingTitle = itemView.findViewById(R.id.time_foldingTitle);
            studentIDFoldingTitle = itemView.findViewById(R.id.student_id_foldingTitle);
            campusTypeFoldingTitle = itemView.findViewById(R.id.campus_type_foldingTitle);
            displayPicFoldingTitle = itemView.findViewById(R.id.displayPhoto_foldingTitle);

            //unfolded
            availableSeatsFoldingContent = itemView.findViewById(R.id.seatsAvailable_textview_FoldingContent);
            priceFoldingContent = itemView.findViewById(R.id.price_foldingcontent);
            nameFoldingContent = itemView.findViewById(R.id.name_foldingContent);
            dateFoldingContent = itemView.findViewById(R.id.date_folding_content);
            timeFoldingContent = itemView.findViewById(R.id.time_foldingContent);
            studentIDFoldingContent = itemView.findViewById(R.id.student_id_foldingContent);
            campusTypeFoldingContent = itemView.findViewById(R.id.campus_type_foldingCOntent);
            displayPicFoldingContent = itemView.findViewById(R.id.displayPhoto_foldingContetn);
            occupiedSeats = itemView.findViewById(R.id.occupiedseats_textview_FoldingContent);
            totalSeats = itemView.findViewById(R.id.totalseats_textview_FoldingContent);
            driverType = itemView.findViewById(R.id.drivertype_textview_foldingContent);
            contact = itemView.findViewById(R.id.contact_Textview_FoldingCOntent);

            fc = itemView.findViewById(R.id.folding_cell_availableRides);
            mapView = (MapView) itemView.findViewById(R.id.map_FoldingView);
            setFcOnClickListener();
            initializeMapView();

        }

        private void setFcOnClickListener() {
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ADADPTOR POS", String.valueOf(getAdapterPosition()));
                    fc.toggle(false);
                    setIsRecyclable(false);
                    waitForAninmation();
                    if (!unfoldedIndexes.contains(getAdapterPosition())) {
                        unfoldedIndexes.add(getAdapterPosition());
                        Log.i("ADADPTOR POS", "IF EXECUTED");

                    } else if (unfoldedIndexes.contains(getAdapterPosition())) {
                        unfoldedIndexes.remove(getAdapterPosition());
                        Log.i("ADADPTOR POS", "ELSE EXECUTED");

                    }

                }
            });
        }


        public void initializeMapView() {
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
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
            LatLng origin = new LatLng(newCarpoolInfoObjects.get(getAdapterPosition()).getMyLat(), newCarpoolInfoObjects.get(getAdapterPosition()).getMyLong());
            LatLng destination = null;
            if (newCarpoolInfoObjects.get(getAdapterPosition()).getMainCampusOrNcfp().equals("Main")) {
                destination = nixorMainLatLng;
            } else {
                destination = nixorNCFPLatLng;
            }
            MarkerOptions originOptions = new MarkerOptions();
            originOptions.position(origin);
            myMap.addMarker(originOptions);
            MarkerOptions campusOptions = new MarkerOptions();
            campusOptions.position(destination);
            campusOptions.icon(BitmapDescriptorFactory.fromBitmap(getNixorMarkerIcon()));
            myMap.addMarker(campusOptions);
            CameraUpdate cameraUpdate = setMapBounds(origin, destination);
            myMap.moveCamera(cameraUpdate);
            myMap.animateCamera(cameraUpdate);
            drawRoute();
        }


        private Bitmap getNixorMarkerIcon() {
            int height = 150;
            int width = 150;
            BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.nixor_marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            return smallMarker;
        }


        private CameraUpdate setMapBounds(LatLng origin, LatLng destination) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            //add the marker locations that you'd like to display on the map
            // boundsBuilder.include(origin);
            boundsBuilder.include(destination);
            boundsBuilder.include(origin);
            final LatLngBounds bounds = boundsBuilder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, context.getResources().getDimensionPixelSize(R.dimen._316sdp),
                    context.getResources().getDimensionPixelSize(R.dimen._130sdp), context.getResources().getDimensionPixelSize(R.dimen._34sdp));
            return cameraUpdate;
        }

        private void drawRoute() {
            PolylineOptions options = new PolylineOptions().width(13).color(Color.parseColor("#990000")).geodesic(true);
            String encodedString = newCarpoolInfoObjects.get(getAdapterPosition()).getRoute();
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

        private void waitForAninmation() {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    setIsRecyclable(true);
                    fc.fold(false);
                }
            }, 1400);
        }

    }

}
