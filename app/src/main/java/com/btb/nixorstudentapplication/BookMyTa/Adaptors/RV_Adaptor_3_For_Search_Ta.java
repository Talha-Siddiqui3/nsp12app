package com.btb.nixorstudentapplication.BookMyTa.Adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.btb.nixorstudentapplication.BookMyTa.TA_Object;
import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.common_util;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Adaptor For Searching TA Layout
public class RV_Adaptor_3_For_Search_Ta extends RecyclerView.Adapter<RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder> {
    List<TA_Object> ta_objects = new ArrayList<>();
    Main_Activity_Ta_Tab mainActivityTa_tab = new Main_Activity_Ta_Tab();
    Context context;
    private common_util cu = new common_util();
    private CollectionReference cr = FirebaseFirestore.getInstance().collection("BookMyTa/BookMyTaDocument/Requests");


    String TAG = "RV_Adaptor_3_For_Search_Ta";

    public RV_Adaptor_3_For_Search_Ta(List<TA_Object> ta_objects) {
        this.ta_objects = ta_objects;

    }

    @NonNull
    @Override
    public RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.foldingview_book_my_ta_search, parent, false);
        return new Rv_ViewHolder(view);
    }

    //sets name id  timings etc for displating.
    @Override
    public void onBindViewHolder(@NonNull final RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder holder, final int position) {
        Log.i(TAG, ta_objects.get(position).getTaName());
        holder.TaNameFolded.setText(ta_objects.get(position).getTaName());
        holder.TaSubjectFolded.setText(ta_objects.get(position).getSubject());
        holder.TaNameUnfolded.setText(ta_objects.get(position).getTaName());
        holder.TaSubjectUnfolded.setText(ta_objects.get(position).getSubject());
        holder.TaID.setText(ta_objects.get(position).getTaID());
        holder.Monday = ta_objects.get(position).getMonday();
        holder.Tuesday = ta_objects.get(position).getTuesday();
        holder.Wednesday = ta_objects.get(position).getWednesday();
        holder.Thursday = ta_objects.get(position).getThursday();
        holder.Friday = ta_objects.get(position).getFriday();
        holder.mondayTime.setText(ExtractTime(holder.Monday));
        holder.tuesdayTime.setText(ExtractTime(holder.Tuesday));
        holder.wednesdayTime.setText(ExtractTime(holder.Wednesday));
        holder.thursdayTime.setText(ExtractTime(holder.Thursday));
        holder.fridayTime.setText(ExtractTime(holder.Friday));

        holder.fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fc.toggle(false);
            }
        });
        holder.BookTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(holder.map, position);
                cu.ToasterShort(context, "Request sent");
                holder.fc.fold(false);
            }
        });

    }

    private String ExtractTime(HashMap<String, Boolean> map) {
        String timings = "";
        int truecount = 0;
        for (int i = 8; i < map.size()+8; i++) {
            if (map.get(String.valueOf(i)) == true) {
                timings = timings + i + "-" + (i + 1) + "/";
                truecount += 1;
            }
        }


        if (truecount == 0) {
            return "Unavailable";
        } else {
            timings=timings.substring(0,timings.length()-1);
            return timings;
        }
    }

    @Override
    public int getItemCount() {
        return ta_objects.size();
    }

    class Rv_ViewHolder extends RecyclerView.ViewHolder {
        TextView TaNameFolded;
        TextView TaSubjectFolded;
        private TextView TaNameUnfolded;
        private TextView TaID;
        private TextView TaSubjectUnfolded;
        private Button BookTa;
        private TextView mondayTime;
        private TextView tuesdayTime;
        private TextView wednesdayTime;
        private TextView thursdayTime;
        private TextView fridayTime;
        //RelativeLayout relativeLayoutLayout;
        FoldingCell fc;
        private Map<String, Object> map;
        private HashMap<String, Boolean> Monday;
        private HashMap<String, Boolean> Tuesday;
        private HashMap<String, Boolean> Wednesday;
        private HashMap<String, Boolean> Thursday;
        private HashMap<String, Boolean> Friday;


        public Rv_ViewHolder(View itemView) {
            super(itemView);
            TaNameFolded = itemView.findViewById(R.id.TA_NAME_FoldingView_Folded);
            TaSubjectFolded = itemView.findViewById(R.id.Teaches_FoldingVIew_Folded);
            TaNameUnfolded = itemView.findViewById(R.id.Ta_Name_FoldingView_Unfolded);
            TaID = itemView.findViewById(R.id.Ta_Id_FoldingVIew);
            TaSubjectUnfolded = itemView.findViewById(R.id.Teaches_FoldingView_Unfolded);
            BookTa = itemView.findViewById(R.id.Submit_Button_FoldingView);
            mondayTime = itemView.findViewById(R.id.Monday_Time);
            tuesdayTime = itemView.findViewById(R.id.Tuesday_Time);
            wednesdayTime = itemView.findViewById(R.id.Wednesday_Time);
            thursdayTime = itemView.findViewById(R.id.Thursday_Time);
            fridayTime = itemView.findViewById(R.id.Friday_Time);
            // relativeLayoutLayout = itemView.findViewById(R.id.cell_title_layout);
            context = itemView.getContext();
            fc = itemView.findViewById(R.id.cell);
            map = new HashMap<>();

        }
    }


    private void sendRequest(Map<String, Object> map, int position) {
        map.put("Status", "Pending");
        map.put("TaName", ta_objects.get(position).getTaName());
        map.put("StudentName", cu.getUserDataLocally(context, "name"));
        map.put("latestUpdateTimestamp", FieldValue.serverTimestamp());
        map.put("TaUserName", ta_objects.get(position).getTaUserName());
        map.put("StudentUserName", ta_objects.get(position).getStudentUserName());
        map.put("TA_Year", ta_objects.get(position).getStudentYear());
        map.put("StudentYear", ta_objects.get(position).getStudentYear());
        cr.add(map);
    }


}







