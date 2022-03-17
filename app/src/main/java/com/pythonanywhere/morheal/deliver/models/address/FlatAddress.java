package com.pythonanywhere.morheal.deliver.models.address;

public class FlatAddress extends Address {

    private String Number;

    public FlatAddress() {
        this("", "", "", "", "");
    }

    public FlatAddress(String state, String city, String street, String buildNum, String number) {
        super(state, city, street, buildNum);
        Number = number;
        super.addField(Number);
    }

    public String getFlatNum() {
        return Number;
    }

    public void setFlatNum(String flatNum) {
        super.addField(flatNum);
        Number = flatNum;
    }

    public String toString() {
        String res = super.toString();
        if(Number!="") res +=", кв. " + Number;
        return  res;
    }
}
