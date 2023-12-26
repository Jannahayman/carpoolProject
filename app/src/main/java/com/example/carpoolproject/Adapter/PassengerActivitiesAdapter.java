package com.example.carpoolproject.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolproject.R;
import com.example.carpoolproject.Model.firebase.Ride;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PassengerActivitiesAdapter extends RecyclerView.Adapter<PassengerActivitiesAdapter.ActivitiesViewHolder>{
    private List<Ride> rideItemList;

    public PassengerActivitiesAdapter(List<Ride> rideItemList) {
        this.rideItemList = rideItemList;
    }

    @NonNull
    @Override
    public PassengerActivitiesAdapter.ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_passenger_ongoing_trips, parent, false);
        return new PassengerActivitiesAdapter.ActivitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerActivitiesAdapter.ActivitiesViewHolder holder, int position) {
        Ride rideItem = rideItemList.get(position);
        Timestamp date = rideItem.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd", Locale.getDefault());
        String formattedDate = sdf.format(date.toDate());
        // Set data to views
        holder.tvPickup.setText(rideItem.getPickup());
        holder.tvDropOff.setText(rideItem.getDropOff());
        holder.tvDate.setText(formattedDate);
        holder.tvStatus.setText(rideItem.getStatus());
        holder.tvTime.setText(rideItem.getTime());
        holder.tvPrice.setText(String.valueOf(rideItem.getRate()));

    }

    @Override
    public int getItemCount() {
        return rideItemList.size();
    }

    public static class ActivitiesViewHolder extends RecyclerView.ViewHolder {
        TextView tvPickup, tvDropOff, tvTime, tvStatus, tvDate, tvPrice;

        public ActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views

            tvPickup = itemView.findViewById(R.id.pickupTextView);
            tvDropOff = itemView.findViewById(R.id.dropoffTextView);
            tvTime =  itemView.findViewById(R.id.timeTextView);
            tvStatus =  itemView.findViewById(R.id.statusTextView);
            tvDate = itemView.findViewById(R.id.dateTextView);
            tvPrice = itemView.findViewById(R.id.feesTextView);
        }
    }
}
