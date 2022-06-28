package com.example.hibarking.data_class;

public class garage_rate {
    float  rate;
   String garage_id;

    public garage_rate(float rate, String garage_id) {
        this.rate = rate;
        this.garage_id = garage_id;
    }

    public float getRate() {
        return rate;
    }

    public String getGarage_id() {
        return garage_id;
    }
}
