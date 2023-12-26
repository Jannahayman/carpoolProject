package com.example.carpoolproject.Passenger;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "passenger_table")

public class Passenger {
    @PrimaryKey @NotNull
    private String passengerId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "mobile")
    private String mobile;
    @ColumnInfo(name = "age")
    private String age;

    public Passenger(String passengerId,String name, String email, String mobile, String age) {
        this.passengerId=passengerId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.age = age;
    }

    public Passenger() {
        this.passengerId = "";
        this.name = "";
        this.email = "";
        this.mobile = "";
        this.age = "";
    }

    public String getPassengerId() {return passengerId;}

    public void setPassengerId(String passengerId) {this.passengerId = passengerId;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }



}
