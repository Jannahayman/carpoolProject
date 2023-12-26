package com.example.carpoolproject.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.Passenger.PassengerHistoryTracking;
import com.example.carpoolproject.Passenger.PassengerMainPage;
import com.example.carpoolproject.R;

public class DriverManageRide extends AppCompatActivity {


    ManageRideQuery manageRideQuery= ManageRideQuery.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_manage_ride);
        replaceFragment(new DriverRideRequestsFrag());



        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button activitiesBtn = findViewById(R.id.activitiesBtn01);
        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToActivities = new Intent(DriverManageRide.this, DriverHistoryTracking.class);
                startActivity(goToActivities);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button mainBtn = findViewById(R.id.mainBtn01);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(DriverManageRide.this, DriverMainPage.class);
                startActivity(goToMain);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button profileBtn = findViewById(R.id.profileBtn01);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotToProfile = new Intent(DriverManageRide.this, DriverProfileView.class);
                startActivity(gotToProfile);
            }
        });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button startRideBtn = findViewById(R.id.startRideButton);
        startRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DriverManageRide.this,"Ride has started",Toast.LENGTH_SHORT).show();
                TextView tvRideStarted = findViewById(R.id.tvRideStarted);
                tvRideStarted.setVisibility(View.VISIBLE);
            }
        });    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button endRideBtn = findViewById(R.id.endRideButton);
        endRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getInfo =getIntent();
                String rideId=getInfo.getStringExtra("rideId");
                manageRideQuery.updateRideStatusToComplete(rideId,
                        aVoid->{
                    manageRideQuery.updateRequestStatus(rideId,"complete",aaVoid->{Toast.makeText(DriverManageRide.this, "Ride ended", Toast.LENGTH_LONG).show();
                        Intent goToMain =new Intent(DriverManageRide.this, DriverHistoryTracking.class);
                        startActivity(goToMain);},exception -> {
                        Log.e("DriverManageRide.endRideBtn", "error updating request status: "+ exception.getMessage() );
                    });


                        },exception -> {});

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_ride_requests, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}