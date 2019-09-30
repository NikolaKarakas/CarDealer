package com.example.myapplication.Model;

public class    Users {

    private String name;
    private String user_id;
    private String image, address;

    public Users(String name, String user_id, String image, String address, String email) {
        this.name = name;
        this.user_id = user_id;
        this.image = image;
        this.address = address;
        this.email = email;
    }

    private String email;

    public Users() {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /*public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_id() {
                return user_id;
            }
        */
    public String getName() {
        return name;
    }
}
