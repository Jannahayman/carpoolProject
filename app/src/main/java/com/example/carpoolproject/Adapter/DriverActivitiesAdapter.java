package com.example.carpoolproject.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolproject.Driver.DriverManageRide;
import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.R;
import com.example.carpoolproject.Model.firebase.Ride;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DriverActivitiesAdapter extends RecyclerView.Adapter<DriverActivitiesAdapter.DriverActivitiesViewHolder>{
    private List<Ride> rideItemList;
    private Activity myActivity;
    ManageRideQuery manageRideQuery = ManageRideQuery.getInstance();

    public DriverActivitiesAdapter(List<Ride> rideItemList, Activity activity) {
        this.rideItemList = rideItemList;
        this.myActivity = activity;
    }


    @NonNull
    @Override
    public DriverActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_driver_ongoing_trips, parent, false);
        return new DriverActivitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverActivitiesViewHolder holder, int position) {
        Ride rideItem = rideItemList.get(position);

        Timestamp date = rideItem.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd", Locale.getDefault());

        String formattedDate = sdf.format(date.toDate());
        String pickup = rideItem.getPickup();
        String dropOff = rideItem.getDropOff();
        String driverId = rideItem.getDriverId();

        String status = rideItem.getStatus();
        String time = rideItem.getTime();
        Double price = rideItem.getRate();

        holder.tvPickup.setText(pickup);
        holder.tvDropOff.setText(dropOff);
        holder.tvDate.setText(formattedDate);
        holder.tvStatus.setText(status);
        holder.tvTime.setText(time);
        holder.tvPrice.setText(price.toString());

        holder.manageRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manageRideQuery.getRideID(pickup, dropOff, time, driverId, status, date, price,
                        rideId -> {

                            Intent goToManageRide = new Intent(myActivity, DriverManageRide.class);
                            goToManageRide.putExtra("rideId",rideId);
                            myActivity.startActivity(goToManageRide);
                        },exception -> {});


            }
        });

    }

    @Override
    public int getItemCount() {
        return rideItemList.size();
    }

    public static class DriverActivitiesViewHolder extends RecyclerView.ViewHolder {
        TextView tvPickup, tvDropOff, tvTime, tvStatus, tvDate, tvPrice;
        Button manageRideBtn;

        public DriverActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            tvPickup = itemView.findViewById(R.id.pickupTextView);
            tvDropOff = itemView.findViewById(R.id.dropoffTextView);
            tvTime =  itemView.findViewById(R.id.timeTextView);
            tvStatus =  itemView.findViewById(R.id.statusTextView);
            tvDate = itemView.findViewById(R.id.dateTextView);
            tvPrice = itemView.findViewById(R.id.feesTextView);
            manageRideBtn = itemView.findViewById(R.id.manageRideButton);
        }
    }
}
