package com.pythonanywhere.morheal.deliver.models.address;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Address implements Serializable {

    private ArrayList<String> fieldListPairs;
    private String State;
    private String City;
    private String Street;
    private String BuildNum;

    public Address() {
        this("", "", "", "");
    }

    public Address(String state, String city, String street, String buildNum) {
        fieldListPairs = new ArrayList<>(4);

        State = state;
        City = city;
        Street = street;
        BuildNum = buildNum;

        addField(State);
        addField(City);
        addField(Street);
        addField(BuildNum);
    }

    @Override
    public String toString() {
        String res = "";
        if (State != "") res += State + " обл., ";
        if (City != "") res += City + ", ";
        if (Street != "") res += "вул. " + Street + ", ";
        if (BuildNum != "") res += "буд. " + BuildNum;

        return res;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        addField(state);
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        addField(city);
        City = city;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        addField(street);
        Street = street;
    }

    public String getBuildNum() {
        return BuildNum;
    }

    public void setBuildNum(String buildNum) {
        addField(buildNum);
        BuildNum = buildNum;
    }

    public void addField(String str) {
        if (str != "") {
            fieldListPairs.add(str);
        }
    }

    public ArrayList<String> getFields() {
        return fieldListPairs;
    }



}
