package com.example.carpoolproject.Driver;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.carpoolproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverSignUp extends AppCompatActivity {
    ManageUserQuery manageUserQuery = ManageUserQuery.getInstance();
    Authentication authentication = Authentication.getInstance();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_up);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button myButton = findViewById(R.id.signUpBtn01);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });
    }

    void performSignUp() {
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String inputEmail = ((EditText) findViewById(R.id.emailText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passText01)).getText().toString();
        String age = ((EditText) findViewById(R.id.ageEditText)).getText().toString();
        String phoneNum = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
        String carModel = ((EditText) findViewById(R.id.carModel)).getText().toString();
        String carColor = ((EditText) findViewById(R.id.carColor)).getText().toString();
        String carPlate = ((EditText) findViewById(R.id.carPlateNumber)).getText().toString();

        if (inputEmail.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || phoneNum.isEmpty() || carModel.isEmpty() || carPlate.isEmpty() || carColor.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(inputEmail)) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }
        authentication.createUser(inputEmail, password,
                result -> {
                    if (result != null) {

                        Intent goToMain = new Intent(DriverSignUp.this, DriverMainPage.class);

                        Toast.makeText(DriverSignUp.this, " Signed up successfully",
                                Toast.LENGTH_SHORT).show();
                        startActivity(goToMain);

                        String driverid =mAuth.getUid();
                        Driver driver = new Driver(driverid, name, inputEmail, phoneNum, age, carModel, carPlate, carColor);
                        manageUserQuery.addDriverToDatabase(driver,
                                innerresult -> {
                                    Toast.makeText(DriverSignUp.this, "Welcome, "+ name,
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("DriverSignUp", "Driver added to Firestore with UID: " + innerresult);
                                },
                                exception -> {
                                    Log.e("DriverSignUp", "Error adding driver to Firestore: " + exception.getMessage());
                                });
                    }
                },
                exception -> {
                    Log.e("DriverSignUp", "Error adding driver to Firestore: " + exception.getMessage());
                });
        {

        }

    }
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@eng\\.asu\\.edu\\.eg";
        return email.matches(emailPattern);
    }

}