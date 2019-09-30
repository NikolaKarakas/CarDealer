package com.example.myapplication;

import java.util.ArrayList;

public class Car {


    private String name, description, image;
    private String post_id;
    private String Brand;
    private String Model;
    private String Year;
    private String Mileage;
    private String Fuel;
    private String Image;
    private String Price;
    private String Description;
    public static  ArrayList<Car> watchcars = new ArrayList<Car>();





    private String AdminId;


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMileage() {
        return Mileage;
    }

    public void setMileage(String mileage) {
        Mileage = mileage;
    }

    public String getFuel() {
        return Fuel;
    }

    public void setFuel(String fuel) {
        Fuel = fuel;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAdminId() {
        return AdminId;
    }

    public void setAdminId(String adminId) {
        AdminId = adminId;
    }






    public Car(String post_id, String Brand, String Model, String Year, String Mileage,
               String Fuel, String Image, String Price, String UserId, String Descritpion) {
        this.post_id = post_id;
        this.Brand = Brand;
        this.Model = Model;
        this.Year = Year;
        this.Mileage = Mileage;
        this.Fuel = Fuel;
        this.Image = Image;
        this.Price = Price;
        this.Description = Descritpion;
        this.AdminId = UserId;


    }

    public String getDescription() {
        return description;
    }

    public Car(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }


    public Car()
    {

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {

        this.image = image;
    }
}
