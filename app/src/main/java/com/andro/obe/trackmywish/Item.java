package com.andro.obe.trackmywish;

/**
 * Created by Brkmo on 28/04/2017.
 */

public class Item {
    private String itemName;
    private double latitude;
    private double longitude;
    public Item(){}
    public Item(String itemName, double latitude, double longitude) {
        this.itemName = itemName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
