package com.example.carpoolproject.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolproject.Driver.DriverRideRequestsFrag;
import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.Passenger.BookRide;
import com.example.carpoolproject.Passenger.Passenger;
import com.example.carpoolproject.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BookingRequestsAdapter extends RecyclerView.Adapter<BookingRequestsAdapter.ActivitiesViewHolder>{
    private List<Passenger> requestItemList;
    private Activity myActivity;
    ManageRideQuery manageRideQuery= ManageRideQuery.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public BookingRequestsAdapter(List<Passenger> requestItemList, Activity activity) {
        this.requestItemList = requestItemList;
        this.myActivity = activity;
    }




    @NonNull
    @Override
    public BookingRequestsAdapter.ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_ride_request, parent, false);
        return new BookingRequestsAdapter.ActivitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRequestsAdapter.ActivitiesViewHolder holder, int position) {
      Passenger passenger = requestItemList.get(position);
      String passengerId= passenger.getPassengerId();
      holder.tvName.setText(passenger.getName());
      holder.tvPhone.setText(passenger.getMobile());
      holder.tvAge.setText(passenger.getAge());

        // Set data to views

        holder.acceptReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageRideQuery.updateRequestStatusToAccepted(passengerId,mAuth.getUid(),"pending",
                        rideIds->{
                            reloadDriverRideRequestsFragment();

                        },
                        exception -> {});


        }});
        holder.rejectReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageRideQuery.updateRequestStatusToRejected(passengerId,mAuth.getUid(),"pending",
                        rideIds->{
                            reloadDriverRideRequestsFragment();

                        },
                        exception -> {});

            }
        });
    }

    private void reloadDriverRideRequestsFragment() {
        // Get the fragment manager
        androidx.fragment.app.FragmentManager fragmentManager = ((AppCompatActivity) myActivity).getSupportFragmentManager();

        // Create a new instance of DriverRideRequestsFrag
        DriverRideRequestsFrag driverRideRequestsFrag = new DriverRideRequestsFrag();

        // Replace the current fragment with the new instance
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_ride_requests, driverRideRequestsFrag) // Replace R.id.fragment_container with your fragment container ID
                .commit();
    }

    @Override
    public int getItemCount() {
        return requestItemList.size();
    }

    public static class ActivitiesViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvAge, tvPayment;
        Button acceptReqBtn,rejectReqBtn;

        public ActivitiesViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views

            tvName = itemView.findViewById(R.id.passengerNameTextView);
            tvPhone = itemView.findViewById(R.id.phoneNumberTextView);
            tvAge =  itemView.findViewById(R.id.ageTextView);
            //tvPayment =  itemView.findViewById(R.id.paymentMethodTextView);

            acceptReqBtn = itemView.findViewById(R.id.acceptButton);
            rejectReqBtn = itemView.findViewById(R.id.rejectButton);

        }

    }

}
