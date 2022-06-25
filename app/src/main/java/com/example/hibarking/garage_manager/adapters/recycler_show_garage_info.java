package com.example.hibarking.garage_manager.adapters;

public class recycler_show_garage_info {
    String name,city,garage_id;

    public recycler_show_garage_info(String name, String city, String garage_id) {
        this.name = name;
        this.city = city;
        this.garage_id = garage_id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getGarage_id() {
        return garage_id;
    }
}
