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

    //sets name id  timings etc
    @Override
    public void onBindViewHolder(@NonNull final RV_Adaptor_3_For_Search_Ta.Rv_ViewHolder holder, final int position) {
        Log.i(TAG, ta_objects.get(position).getTaName());
        holder.TaNameFolded.setText(ta_objects.get(position).getTaName());
        holder.TaSubjectFolded.setText(ta_objects.get(position).getSubject());
        holder.TaNameUnfolded.setText(ta_objects.get(position).getTaName());
        holder.TaSubjectUnfolded.setText(ta_objects.get(position).getSubject());
        holder.TaID.setText(ta_objects.get(position).getTaID());
     /*   holder.relativeLayoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //sends the speceific TAObject from the ArrayList of TaObjects.
                //Ta_Dialogue ta_dialogue = new Ta_Dialogue(context,ta_objects.get(position));
                //ta_dialogue.ParseData();
                //ta_dialogue.ExtractTime();
               // ta_dialogue.UpdateLayout();
                //ta_dialogue.show();
            }
        });
    */
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
        //RelativeLayout relativeLayoutLayout;
        FoldingCell fc;
        private Map<String, Object> map;

        public Rv_ViewHolder(View itemView) {
            super(itemView);
            TaNameFolded = itemView.findViewById(R.id.TA_NAME_FoldingView_Folded);
            TaSubjectFolded = itemView.findViewById(R.id.Teaches_FoldingVIew_Folded);
            TaNameUnfolded = itemView.findViewById(R.id.Ta_Name_FoldingView_Unfolded);
            TaID = itemView.findViewById(R.id.Ta_Id_FoldingVIew);
            TaSubjectUnfolded = itemView.findViewById(R.id.Teaches_FoldingView_Unfolded);
            BookTa = itemView.findViewById(R.id.Submit_Button_FoldingView);
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
        map.put("TaFirebaseTokens",ta_objects.get(position).getTaFirebaseTokens());
        
        cr.add(map);
    }


}







