package com.example.carpoolproject.Model.room;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

public class UserViewModel {

    private UserRepository repository;

    public UserViewModel(Application application) {
        repository = new UserRepository(application);
    }

}