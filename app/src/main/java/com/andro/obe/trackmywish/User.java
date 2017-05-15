package com.andro.obe.trackmywish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brkmo on 15/05/2017.
 */

public class User {
    private String userName;
    private String email;
    private List<Item> items;

    public User(){
        items = new ArrayList<>();
    }
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        items = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
