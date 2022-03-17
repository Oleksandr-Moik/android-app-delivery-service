package com.pythonanywhere.morheal.deliver.models.product;

import java.io.Serializable;

public class Product implements Serializable {

    private String Name;
    private Double Price;
    private String Manufacture;

    public Product() {
        this("", 0.0, "");
    }

    public Product(String name, Double price, String manufacture) {
        Name = name;
        Price = price;
        Manufacture = manufacture;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getManufacture() {
        return Manufacture;
    }

    public void setManufacture(String manufacture) {
        Manufacture = manufacture;
    }

    @Override
    public String toString() {
        return "\"" + Name + "\", \"" + Manufacture + "\"";
    }
}
