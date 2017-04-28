package com.andro.obe.trackmywish;

/**
 * Created by Brkmo on 28/04/2017.
 */

public class Item {
    private String itemName;
    private double distance;

    public Item(String itemName, double distance) {
        this.itemName = itemName;
        this.distance = distance;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
