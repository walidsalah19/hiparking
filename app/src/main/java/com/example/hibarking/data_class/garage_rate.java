package com.example.hibarking.data_class;

public class garage_rate {
    int rate,garage_id;

    public garage_rate(int rate, int garage_id) {
        this.rate = rate;
        this.garage_id = garage_id;
    }

    public int getRate() {
        return rate;
    }

    public int getGarage_id() {
        return garage_id;
    }
}
