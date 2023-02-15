package com.example.a2ndhandapp.Utils;

import android.content.Context;
import android.util.Log;

import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;

import java.util.ArrayList;

public class CurrentUser {

    private static CurrentUser instance = null;
    private User user;
    private String currentCategory;


    private ArrayList<Product> currentShowingProducts = new ArrayList<>();

    private CurrentUser() {
    }

    public static void initCurrentUser(Context context) {
        if (instance == null) {
            instance = new CurrentUser();
        }
        getInstance().setCurrentCategory("All");
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public CurrentUser setUser(User user) {
        this.user = user;
        return this;
    }

    public CurrentUser removeUser() {
        this.user = null;
        setCurrentCategory("All");
        return this;
    }

    /**
     * The currentCategory is to know what to show on the home screen
     */
    public String getCurrentCategory() {
        return currentCategory;
    }

    public CurrentUser setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
        return this;
    }

    /**
     * This two methods are
     * @return
     */
    public ArrayList<Product> getCurrentShowingProducts() {
        return currentShowingProducts;
    }

    public CurrentUser setCurrentShowingProducts(ArrayList<Product> currentShowingProducts) {
        this.currentShowingProducts = currentShowingProducts;
        return this;
    }
}
