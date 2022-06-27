package com.example.hibarking.data_class;

public class DateDrivers {
    String driver, date;

    public DateDrivers(String driver, String date) {

        this.driver = driver;
        this.date = date;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
