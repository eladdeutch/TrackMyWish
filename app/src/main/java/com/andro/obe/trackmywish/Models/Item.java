package com.andro.obe.trackmywish.Models;

/**
 * Created by Brkmo on 28/04/2017.
 */

public class Item {
    private String itemName;
    private WishPlace mPlace;
    public Item(){}
    public Item(String itemName, WishPlace mPlace) {
        this.itemName = itemName;
        this.mPlace = mPlace;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public WishPlace getmPlace() {
        return mPlace;
    }

    public void setmPlace(WishPlace mPlace) {
        this.mPlace = mPlace;
    }
}
