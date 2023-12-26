
package com.example.carpoolproject.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.carpoolproject.Model.firebase.ManageUserQuery;
import com.example.carpoolproject.Passenger.BookRide;
import com.example.carpoolproject.R;
import com.example.carpoolproject.Model.firebase.Ride;
import com.google.firebase.Timestamp;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    ManageUserQuery manageUserQuery = ManageUserQuery.getInstance();

    private Activity myActivity;

    private List<Ride> rideItemList;

    public RideAdapter(List<Ride> rideItemList, Activity activity) {
        this.rideItemList = rideItemList;
        this.myActivity = activity;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_ar_home, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {

        Ride rideItem = rideItemList.get(position);
        String pickup = rideItem.getPickup();
        String dropoff = rideItem.getDropOff();
        String time = rideItem.getTime();
        String driverID = rideItem.getDriverId();
        String rate = String.valueOf(rideItem.getRate());
        String availableSeats = String.valueOf(rideItem.getAvailable_seats());

        holder.tvPickup.setText(pickup);
        holder.tvDropOff.setText(dropoff);
        holder.tvTime.setText(time);
        holder.tvPrice.setText(rate);
        holder.tvAvailable.setText("Seats left: "+ availableSeats);

        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageUserQuery.getDriverData(driverID,
                        driver -> {
                            String driverName = driver.getName();
                            String driverPhone = driver.getMobile();
                            String carModel = driver.getCarModel();
                            String carPlate = driver.getCarPlate();
                            String carColor = driver.getCarColor();

                            Intent intent = new Intent(myActivity, BookRide.class);


                            intent.putExtra("driverID", driverID);
                            intent.putExtra("driverName", driverName);
                            intent.putExtra("driverPhone", driverPhone);
                            intent.putExtra("carModel", carModel);
                            intent.putExtra("carPlate", carPlate);
                            intent.putExtra("carColor", carColor);

                            // Add other data you need to pass
                            intent.putExtra("pickup", rideItem.getPickup());
                            intent.putExtra("dropoff", rideItem.getDropOff());
                            intent.putExtra("time", rideItem.getTime());
                            intent.putExtra("status", rideItem.getStatus());
                            Timestamp timestamp = rideItem.getTimestamp();
                            intent.putExtra("timestamp", timestamp.getSeconds());                            intent.putExtra("rate", rideItem.getRate());
                            intent.putExtra("available_seats", rideItem.getAvailable_seats());

                            // Start the intent
                            myActivity.startActivity(intent);

                        },
                        exception -> {
                            // Handle failure to get driver data
                            Log.e("RideAdapter", "Error getting driver data: " + exception.getMessage());});
            }
        });

    }

    @Override
    public int getItemCount() {
        return rideItemList.size();
    }

    public static class RideViewHolder extends RecyclerView.ViewHolder {
        TextView tvPickup, tvDropOff, tvTime, tvPrice, tvAvailable;
        ImageView viewArrow;
        Button btnBookNow;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            tvPickup = itemView.findViewById(R.id.tv_pickup_item_ar_fragment_home);
            viewArrow = itemView.findViewById(R.id.view_arrow_item_ar_fragment_home);
            tvDropOff = itemView.findViewById(R.id.tv_drop_off_item_ar_fragment_home);
            tvTime = itemView.findViewById(R.id.tv_date_item_ar_fragment_home);
            tvPrice = itemView.findViewById(R.id.tv_price_item_ar_fragment_home);
            tvAvailable = itemView.findViewById(R.id.tv_available_seats);

            btnBookNow = itemView.findViewById(R.id.button_book_now_item_ar_fragment_home);

        }
    }
}
