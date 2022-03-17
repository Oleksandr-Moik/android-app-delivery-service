package com.pythonanywhere.morheal.deliver.models.address;

public class PrivateBuildAddress extends Address {

    public PrivateBuildAddress() {
        this("", "", "", "");
    }

    public PrivateBuildAddress(String state, String city, String street, String buildNum) {
        super(state, city, street, buildNum);
    }
}
