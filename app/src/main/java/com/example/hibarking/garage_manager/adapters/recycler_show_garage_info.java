package com.example.hibarking.garage_manager.adapters;

public class recycler_show_garage_info {
    String name,city;

    public recycler_show_garage_info(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
