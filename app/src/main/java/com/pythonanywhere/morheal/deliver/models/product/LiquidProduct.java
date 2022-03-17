package com.pythonanywhere.morheal.deliver.models.product;

public class LiquidProduct extends Product {

    private Double Volume;

    public LiquidProduct() {
        this(0.0);
    }

    public LiquidProduct(Double volume) {
        this("", 0.0, "", volume);
    }

    public LiquidProduct(String name, Double price, String manufacture, Double volume) {
        super(name, price, manufacture);
        Volume = volume;
    }

    public Double getVolume() {
        return Volume;
    }

    public void setVolume(Double volume) {
        Volume = volume;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", (%.2f)", Volume);
    }
}
