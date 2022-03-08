package com.example.hibarking.driver.user_account;

public class create_account_class {
    private String user_name ,phone_number,user_id,license,id_card_number,email, dataofcreation;

    public create_account_class(String user_name, String phone_number, String user_id, String license, String id_card_number, String email, String dataofcreation) {
        this.user_name = user_name;
        this.phone_number = phone_number;
        this.user_id = user_id;
        this.license = license;
        this.id_card_number = id_card_number;
        this.email = email;
        this.dataofcreation = dataofcreation;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLicense() {
        return license;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public String getEmail() {
        return email;
    }

    public String getDataofcreation() {
        return dataofcreation;
    }
}
