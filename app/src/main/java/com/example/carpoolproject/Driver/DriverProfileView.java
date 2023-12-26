package com.example.carpoolproject.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolproject.Model.firebase.ManageUserQuery;
import com.example.carpoolproject.Model.room.UserDao;
import com.example.carpoolproject.Model.room.UserDatabase;
import com.example.carpoolproject.Passenger.PassengerProfileView;
import com.example.carpoolproject.R;
import com.example.carpoolproject.Start.MainActivity;
import com.example.carpoolproject.carpoolProject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class DriverProfileView extends AppCompatActivity {
    TextView pName, pEmail, pPhone, pAge,cModel,cPlate,cColor;

    private static final Executor executor = Executors.newSingleThreadExecutor();

    ManageUserQuery manageUserQuery = ManageUserQuery.getInstance();
    UserDatabase userDatabase = carpoolProject.getInstance().getDb();
    UserDao userDao = userDatabase.userDao();


    protected void viewProfileInfo() {


            LiveData<Integer> driverCountLiveData = userDao.getDriverCount();
            driverCountLiveData.observe(this, driverCount -> {
                if (driverCount != null && driverCount > 0) {
                    LiveData<List<Driver>> driverLiveData = userDao.getAllDrivers(); // Assuming you have a method to get all drivers
                    driverLiveData.observe(this, drivers -> {
                        if (drivers != null && !drivers.isEmpty()) {
                            Driver firstDriver = drivers.get(0);
                            updateUI(firstDriver);
                        }
                    });
                }
            });
        }


    private void updateUI(Driver driver) {
        String driverName = driver.getName();
        String driverMobile = driver.getMobile();
        String driverEmail = driver.getEmail();
        String driverAge = driver.getAge();
        String carModel = driver.getCarModel();
        String carPlate = driver.getCarPlate();
        String carColor = driver.getCarColor();

        pName = findViewById(R.id.nameTextView);
        pPhone = findViewById(R.id.mobileValueTextView);
        pEmail = findViewById(R.id.emailValueTextView);
        pAge = findViewById(R.id.driverAgeValueTextView);
        cModel = findViewById(R.id.carModel);
        cPlate = findViewById(R.id.carPlateNumberValue);
        cColor = findViewById(R.id.carColorValue);

        pName.setText(driverName);
        pEmail.setText(driverEmail);
        pPhone.setText(driverMobile);
        pAge.setText(driverAge);
        cModel.setText(carModel);
        cPlate.setText(carPlate);
        cColor.setText(carColor);
    }

    // Helper method to fetch Driver data from Firebase
    private void fetchDriverData(String driverId) {
        manageUserQuery.getDriverData(driverId,
                driver -> {
                    insertDriver(driver, userDao); // Insert the data into Room database
                    updateUI(driver);
                },
                exception -> {
                    // Handle failure to get driver data
                    Log.e("DriverProfileView", "Error getting driver data: " + exception.getMessage());
                });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }

    public void insertDriver(Driver driver, UserDao userDao) {
        executor.execute(() -> userDao.insertDriver(driver));
    }

    public void clearDrivers() {
        executor.execute(() -> {
            userDao.clearDrivers();
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile_view);
        viewProfileInfo();

        Button viewActivitiesBtn = findViewById(R.id.activitiesBtn01);
        viewActivitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToActivities = new Intent(DriverProfileView.this, DriverHistoryTracking.class);
                startActivity(goToActivities);
            }
        });
        Button logOutBtn = findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDrivers();
                FirebaseAuth.getInstance().signOut(); // Sign out the current user

                Intent intent = new Intent(DriverProfileView.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();

                if(!isNetworkConnected()) {
                    logOutBtn.setEnabled(false);
                    Toast.makeText(DriverProfileView.this, "Please check your connection to log out.", Toast.LENGTH_LONG).show();
                }
            }
        });


                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button bookRide = findViewById(R.id.mainBtn01);
        bookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToBooking = new Intent(DriverProfileView.this, DriverMainPage.class);
                startActivity(goToBooking);
            }
        });

    }
}