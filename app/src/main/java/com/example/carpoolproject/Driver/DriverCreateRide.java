package com.example.carpoolproject.Driver;

import com.example.carpoolproject.Model.firebase.ManageRideQuery;
import com.example.carpoolproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DriverCreateRide extends AppCompatActivity {
    ManageRideQuery manageRideQuery = ManageRideQuery.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spinnerTime, spinnerPickup, spinnerDropOff;
    private Button buttonOfferRide;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatePicker datePicker;

    Date currentDate = new Date();

    // Define the desired date format
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_create_ride);

        spinnerTime = findViewById(R.id.spinnerTime);
        spinnerPickup = findViewById(R.id.spinnerPickup);
        spinnerDropOff = findViewById(R.id.spinnerDropOff);
        buttonOfferRide = findViewById(R.id.buttonOfferRide);
        datePicker = findViewById(R.id.datePicker);

        // Set minimum date to today
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        // Populate spinners with data
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                this, R.array.time_options, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        ArrayAdapter<CharSequence> areaAdapter = ArrayAdapter.createFromResource(
                this, R.array.area_options, android.R.layout.simple_spinner_item);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPickup.setAdapter(areaAdapter);
        spinnerDropOff.setAdapter(areaAdapter);

        // Set listeners
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateLocationSpinners(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        buttonOfferRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerRide();
            }
        });
    }

    private void updateLocationSpinners(int selectedTimeIndex) {
        if (selectedTimeIndex == 0) {
            // 7:30 am ride
            // Set pickup areas
            ArrayAdapter<CharSequence> pickupAdapter = ArrayAdapter.createFromResource(
                    this, R.array.pickup_areas_morning, android.R.layout.simple_spinner_item);
            pickupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPickup.setAdapter(pickupAdapter);

            // Set drop-off areas
            ArrayAdapter<CharSequence> dropOffAdapter = ArrayAdapter.createFromResource(
                    this, R.array.dropoff_areas_morning, android.R.layout.simple_spinner_item);
            dropOffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDropOff.setAdapter(dropOffAdapter);

            // Disable 7:30 AM if the date is today
            Calendar today = Calendar.getInstance();
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            // Clear the time part to compare only the dates
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            selectedDate.set(Calendar.HOUR_OF_DAY, 0);
            selectedDate.set(Calendar.MINUTE, 0);
            selectedDate.set(Calendar.SECOND, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);

            if (today.equals(selectedDate)) {
                ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                        this, R.array.time_options_without_7_30_am, android.R.layout.simple_spinner_item);
                timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTime.setAdapter(timeAdapter);
            } else {
                ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
                        this, R.array.time_options, android.R.layout.simple_spinner_item);
                timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTime.setAdapter(timeAdapter);
            }
        } else {
            // 5:30 pm ride
            // Set pickup areas
            ArrayAdapter<CharSequence> pickupAdapter = ArrayAdapter.createFromResource(
                    this, R.array.pickup_areas_evening, android.R.layout.simple_spinner_item);
            pickupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPickup.setAdapter(pickupAdapter);

            // Set drop-off areas
            ArrayAdapter<CharSequence> dropOffAdapter = ArrayAdapter.createFromResource(
                    this, R.array.dropoff_areas_evening, android.R.layout.simple_spinner_item);
            dropOffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDropOff.setAdapter(dropOffAdapter);
        }
    }

    private void offerRide() {
        String selectedTime = spinnerTime.getSelectedItem().toString();
        String selectedPickup = spinnerPickup.getSelectedItem().toString();
        String selectedDropOff = spinnerDropOff.getSelectedItem().toString();
        int hours=7;
        if("5:30 PM".equals(selectedTime)){
            hours = 17;
        }
        // Process the selected ride details
        checkExistingRideandAdd(selectedTime, selectedPickup, selectedDropOff,hours);
        String message = "Offering ride at " + selectedTime +
                " from " + selectedPickup +
                " to " + selectedDropOff;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void checkExistingRideandAdd(String selectedTime, String selectedPickup, String selectedDropOff, int hours) {
        String driverID = mAuth.getUid();
        db.collection("rides")
                .whereEqualTo("driverID", driverID)
                .whereEqualTo("time", selectedTime)
                .whereEqualTo("status", "incomplete")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                Toast.makeText(DriverCreateRide.this,
                                        "Cannot offer another incomplete ride at the same time",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Boolean isValid = isRideTimeValid(hours);
                                if (isValid) {
                                    Timestamp timestamp = new Timestamp(getTime(hours).getTime()/ 1000, 0);
                                    manageRideQuery.addRideToDatabase(selectedPickup, selectedDropOff, selectedTime, "available", timestamp
                                            ,
                                            innerresult -> {
                                                Toast.makeText(DriverCreateRide.this, "Ride successfully added",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent goToMain = new Intent(DriverCreateRide.this, DriverMainPage.class);
                                                startActivity(goToMain);
                                                Log.d("DriverCreateRide", "Ride added to Firestore with UID: " + innerresult);
                                            },
                                            exception -> {
                                                Log.e("DriverCreateRide", "Error adding ride to Firestore: " + exception.getMessage());
                                            });
                                }
                            }
                        } else {
                            // Handle the error
                            Toast.makeText(DriverCreateRide.this,
                                    "Error checking existing rides", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Date getTime(int hours){
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),hours,30,0);
        return selectedCalendar.getTime();
    }
    private boolean isRideTimeValid( int hours) {

        Calendar currentTime = Calendar.getInstance();
        Date selectedCalender = getTime(hours);


        long timeDifferenceInMillis = selectedCalender.getTime() - currentTime.getTimeInMillis();
        long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis);

        if (hours==7 && hoursDifference < 9.5) {
            Toast.makeText(this, "Cannot offer ride in less than 9.5 hours difference", Toast.LENGTH_LONG).show();
            return false;
        } else if (hours==17 && hoursDifference < 4.5) {
            Toast.makeText(this, "Cannot offer ride in less than 4.5 hours difference", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
