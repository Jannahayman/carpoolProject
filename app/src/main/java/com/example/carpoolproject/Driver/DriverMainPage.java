package com.example.carpoolproject.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carpoolproject.R;

public class DriverMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main_page);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button createRide = findViewById(R.id.createRidebtn);
        createRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCreateRide = new Intent(DriverMainPage.this, DriverCreateRide.class);
                startActivity(goToCreateRide);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button activitiesBtn = findViewById(R.id.activitiesBtn01);
        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToActivities = new Intent(DriverMainPage.this, DriverHistoryTracking.class);
                startActivity(goToActivities);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button profileBtn = findViewById(R.id.profileBtn01);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotToProfile = new Intent(DriverMainPage.this, DriverProfileView.class);
                startActivity(gotToProfile);
            }
        });
    }
}