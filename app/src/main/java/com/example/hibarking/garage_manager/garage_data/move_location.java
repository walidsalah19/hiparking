package com.example.hibarking.garage_manager.garage_data;

public class move_location {
   static String longitude="",latitude="",type="";

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        move_location.type = type;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        move_location.longitude = longitude;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        move_location.latitude = latitude;
    }
}
