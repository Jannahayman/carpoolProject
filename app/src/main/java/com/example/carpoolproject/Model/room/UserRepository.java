package com.example.carpoolproject.Model.room;

import android.app.Application;
import com.example.carpoolproject.carpoolProject;

public class UserRepository {
    private UserDao userDao;
    public UserRepository(Application application) {
        UserDatabase database = carpoolProject.getInstance().getDb();
        userDao = database.userDao();
    }
}