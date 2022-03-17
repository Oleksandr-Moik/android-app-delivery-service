package com.pythonanywhere.morheal.deliver.models.address;

public class OfficeAddress extends Address {

    private String Number;
    private String Floor;

    public OfficeAddress() {
        this("", "", "", "", "", "");
    }

    public OfficeAddress(String state, String city, String street, String buildNum, String number, String floor) {
        super(state, city, street, buildNum);
        Number = number;
        Floor = floor;
        super.addField(Number);
        super.addField(Floor);
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        super.addField(number);
        Number = number;
    }

    public String getFloor() {

        return Floor;
    }

    public void setFloor(String floor) {
        super.addField(floor);
        Floor = floor;
    }

    public String toString() {
        String res = super.toString();
        if (Floor != "") res += ", пов. " + Floor;
        if (Number != "") res += ", оф. №" + Number;
        return res;
    }
}
