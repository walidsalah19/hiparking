package com.example.hibarking.data_class;

public class Mechanical {
    String name , email, location;
    String nationalID , phone ;

    public Mechanical( String name, String email, String location, String nationalID,  String phone) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.nationalID = nationalID;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}

