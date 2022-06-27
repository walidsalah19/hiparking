package com.example.hibarking.data_class;

public class user_mechanical_data {
    String name,id,phone;
    double longitude,latitude;

    public user_mechanical_data(String name, String id, String phone, double longitude, double latitude) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
