package com.pythonanywhere.morheal.deliver.models.product;

public class SolidMatterProduct extends Product {

    private Double Width;
    private Double Height;
    private Double Length;

    public SolidMatterProduct() {
        this(0.0, 0.0, 0.0);
    }

    public SolidMatterProduct(Double width, Double height, Double length) {
        this("", 0.0, "", width, height, length);
    }

    public SolidMatterProduct(String name, Double price, String manufacture, Double width, Double height, Double length) {
        super(name, price, manufacture);
        Width = width;
        Height = height;
        Length = length;
    }

    public Double getWidth() {
        return Width;
    }

    public void setWidth(Double width) {
        Width = width;
    }

    public Double getHeight() {
        return Height;
    }

    public void setHeight(Double height) {
        Height = height;
    }

    public Double getLength() {
        return Length;
    }

    public void setLength(Double length) {
        Length = length;
    }

    @Override
    public String toString() {
        return super.toString()+String.format(", (%.2f-%.2f-%.2f)", Width, Height, Length);
    }
}
