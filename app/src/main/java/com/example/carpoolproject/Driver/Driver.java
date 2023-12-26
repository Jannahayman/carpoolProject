package com.example.carpoolproject.Driver;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


@Entity(tableName = "driver_table")
public class Driver {

    @PrimaryKey @NotNull
    private String driverId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "mobile")
    private String mobile;
    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "carModel")
    private String carModel;
    @ColumnInfo(name = "carPlate")
    private String carPlate;
    @ColumnInfo(name = "carColor")
    private String carColor;

    public Driver(String driver,String name, String email, String mobile, String age, String carModel, String carPlate,String carColor) {
        this.driverId = driver;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.age = age;
        this.carModel=carModel;
        this.carPlate=carPlate;
        this.carColor=carColor;

    }
    public Driver(){
    }
    public void setDriverId(String newId) {this.driverId = newId;}

    public String getDriverId() {return driverId;}

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }



    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }




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
