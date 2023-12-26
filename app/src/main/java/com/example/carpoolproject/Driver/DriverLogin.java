package com.example.carpoolproject.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carpoolproject.Model.firebase.Authentication;
import com.example.carpoolproject.Model.firebase.ManageUserQuery;
import com.example.carpoolproject.Model.room.UserDao;
import com.example.carpoolproject.Model.room.UserDatabase;
import com.example.carpoolproject.Passenger.Passenger;
import com.example.carpoolproject.R;
import com.example.carpoolproject.carpoolProject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriverLogin extends AppCompatActivity {
    Authentication authentication = Authentication.getInstance();
    UserDatabase userDatabase = carpoolProject.getInstance().getDb();
    final UserDao userDao = userDatabase.userDao();
    ManageUserQuery manageUserQuery = ManageUserQuery.getInstance();
    private static final Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }
    public void insertDriver(Driver driver, UserDao userDao) {
        executor.execute(() -> userDao.insertDriver(driver));
    }
    private void addToRoomDb(String driverId) {
        manageUserQuery.getInstance().getDriverData(driverId,
                passenger -> {
                    insertDriver(passenger, userDao);
                },
                exception -> {
                    // Handle failure to get passenger data
                    Log.e("PassengerProfileView", "Error getting passenger data: " + exception.getMessage());
                });
    }

    void performLogin(){
        String inputEmail = ((EditText) findViewById(R.id.emailText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passText)).getText().toString();
        if(inputEmail.isEmpty()||password.isEmpty())
        {
            Toast.makeText(this,"Please fill are fields",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(inputEmail)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }
        authentication.logIn(inputEmail,password,
                result -> {
                    if("successful".equals(result)) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        addToRoomDb(currentUser.getUid());
                        Intent goToMain = new Intent(DriverLogin.this, DriverMainPage.class);
                        Toast.makeText(DriverLogin.this, "Authentication successful",
                                Toast.LENGTH_SHORT).show();
                        startActivity(goToMain);
                        Log.d("DriverLogin", "Driver authenticated");
                    }
                    else if ("wrong".equals(result)) {
                        Toast.makeText(DriverLogin.this, "Wrong email or password.",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                exception -> {
                    Log.e("DriverLogin", "Error logging in: " + exception.getMessage());
                });

    }


    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@eng\\.asu\\.edu\\.eg";
        return email.matches(emailPattern);
    }

    public void signUpClick(View v){
        Intent intent = new Intent(DriverLogin.this, DriverSignUp.class);
        startActivity(intent);
    }
}