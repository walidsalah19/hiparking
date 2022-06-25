package com.example.hibarking.data_class;

public class garage_model {
    String garage_name,city,garage_id,manager_id,garage_paper;
    double latitude,longitude;
    int  unit_number,hour_price;

    public garage_model(String garage_name, String city, String garage_id, String manager_id, String garage_paper, double latitude, double longitude, int unit_number, int hour_price) {
        this.garage_name = garage_name;
        this.city = city;
        this.garage_id = garage_id;
        this.manager_id = manager_id;
        this.garage_paper = garage_paper;
        this.latitude = latitude;
        this.longitude = longitude;
        this.unit_number = unit_number;
        this.hour_price = hour_price;
    }

    public String getGarage_name() {
        return garage_name;
    }

    public String getCity() {
        return city;
    }

    public String getGarage_id() {
        return garage_id;
    }

    public String getManager_id() {
        return manager_id;
    }

    public String getGarage_paper() {
        return garage_paper;
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

    public int getHour_price() {
        return hour_price;
    }
}
