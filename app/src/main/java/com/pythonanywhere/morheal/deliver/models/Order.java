package com.pythonanywhere.morheal.deliver.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pythonanywhere.morheal.deliver.models.address.Address;
import com.pythonanywhere.morheal.deliver.models.address.PrivateBuildAddress;
import com.pythonanywhere.morheal.deliver.models.product.Product;

public class Order implements Serializable {
    private static int ID = 0;
    public static int getID() {
        return ID;
    }
    // use this setter for avoid dublicated OrderID after reading / deserializable from file
    public static void setID(int newID) {
        ID=newID;
    }

    private int OrderID;
    private String Name;
    private Address Address;
    private Product product;
    private Boolean Paided;
    private Date OrderDateTime;

    public Order(Order order){
        OrderID = ID++;
        Name = order.getName();
        Paided = order.getPaided();
        OrderDateTime =order.getOrderDateTime();
        Address = order.getAddress();
        product = order.getProduct();
    }

    public Order() {
        this("",false, 2020, 1,1,0,0,0,new PrivateBuildAddress());
    }

    public Order(String name, Boolean paided, int year, int month, int date, int hrs, int min, int sec, Address address) {
        this("",false, 2020, 1,1,0,0,0,new PrivateBuildAddress(),new Product());
    }

    public Order(String name, Boolean paided, int year, int month, int date, int hrs, int min, int sec, Address address, Product product) {
        OrderID = ID++;
        Name = name;
        Paided = paided;
        OrderDateTime = new Date(year, month, date, hrs, min, sec);
        Address = address;
        this.product = product;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getProductPrice() {
        return product.getPrice();
    }

    public Address getAddress() {
        return Address;
    }

    public void setAddress(Address address) {
        Address = address;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Boolean getPaided() {
        return Paided;
    }

    public void setPaided(Boolean paided) {
        Paided = paided;
    }

    public Date getOrderDateTime() {
        return OrderDateTime;
    }

    public void setOrderDateTime(Date orderDateTime) {
        OrderDateTime = orderDateTime;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    @Override
    public String toString() {
        return "ID: "+OrderID+", Name: "+Name+", Date : "+ OrderDateTime.toString() + ", Address : "+Address.toString();
    }
}
