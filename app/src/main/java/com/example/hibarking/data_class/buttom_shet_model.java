package com.example.hibarking.data_class;

public class buttom_shet_model {
    private String garage_id,name,unit,rate,price;

    public buttom_shet_model(String garage_id, String name, String unit, String rate, String price) {
        this.garage_id = garage_id;
        this.name = name;
        this.unit = unit;
        this.rate = rate;
        this.price = price;
    }

    public String getGarage_id() {
        return garage_id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getRate() {
        return rate;
    }

    public String getPrice() {
        return price;
    }
}
