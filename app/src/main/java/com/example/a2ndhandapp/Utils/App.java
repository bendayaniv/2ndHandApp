package com.example.a2ndhandapp.Utils;

import android.app.Application;

import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Utils.ImageLoader;

import java.util.ArrayList;

public class App extends Application {

//    private ArrayList<Product> products;

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader.initImageLoader(this);
        CurrentUser.initCurrentUser(this);
//        products = new ArrayList<>();
//        products = DataManager.getProducts();
//        ImageLoader.initImageLoader(this);
    }
}
