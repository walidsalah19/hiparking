package com.example.hibarking.data_class;

public class garage_manager_model {
    String username,email,national_id,id,image,phone;

    public garage_manager_model(String username, String email, String national_id, String id, String image,String phone) {
        this.username = username;
        this.email = email;
        this.national_id = national_id;
        this.id = id;
        this.image = image;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
