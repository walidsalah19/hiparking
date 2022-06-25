package com.example.hibarking.data_class;

public class garage_model {
    String name,city,id,rate;
    double latitude,longitude;
    int  unit_number,price;


    public garage_model(String name, String city, int price, double latitude, double longitude, int unit_number, String id, String rate) {
        this.name = name;
        this.city = city;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.unit_number = unit_number;
        this.id=id;
        this.rate=rate;
    }

    public String getRate() {
        return rate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getPrice() {
        return price;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getUnit_number() {
        return unit_number;
    }
}
