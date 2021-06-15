package com.aruns.sirvacs_a_lot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aruns.sirvacs_a_lot.models.SessionDetail;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Viewholder>{

    private Context context;
    private ArrayList<SessionDetail> sessionModelArrayList;

    public ResultAdapter(Context context, ArrayList<SessionDetail> sessionModelArrayList) {
        this.context = context;
        this.sessionModelArrayList = sessionModelArrayList;
    }

    @NonNull
    @Override
    public ResultAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        SessionDetail model = sessionModelArrayList.get(position);
        holder.sessionDate.setText(model.date);
        holder.vaccineName.setText(model.vaccine);
        holder.centerName.setText(model.centreName);
        holder.centerAddress.setText(model.centreAddress);
        holder.feeType.setText(model.feeType);
        holder.availableSlotsCount.setText(model.availableSlots.toString());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return sessionModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView sessionDate, vaccineName, centerName, centerAddress, feeType,availableSlotsCount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            sessionDate=itemView.findViewById(R.id.sessionDate);
            vaccineName=itemView.findViewById(R.id.vaccineName);
            centerName=itemView.findViewById(R.id.centreName);
            centerAddress=itemView.findViewById(R.id.centerAddress);
            feeType=itemView.findViewById(R.id.feeType);
            availableSlotsCount=itemView.findViewById(R.id.availableSlotsCount);
        }
    }
}
