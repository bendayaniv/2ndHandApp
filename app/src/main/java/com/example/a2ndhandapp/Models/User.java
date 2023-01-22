package com.example.a2ndhandapp.Models;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String phone;
    private String uid;
    private ArrayList<Product> products;
    private ArrayList<Product> favorites;
    private String address;

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    private String city;
    private String country;
    private String phoneNum;


    public User() {
    }

    public User(String name, String email, String phone, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public User setProducts(ArrayList<Product> products) {
        this.products = products;
        return this;
    }

    public User addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        return this;
    }

    public ArrayList<Product> getFavorites() {
        return favorites;
    }

    public User setFavorites(ArrayList<Product> favorites) {
        this.favorites = favorites;
        return this;
    }

    public User addFavorite(Product product) {
        if (this.favorites == null) {
            this.favorites = new ArrayList<>();
        }
        this.favorites.add(product);
        return this;
    }

    public User removeFavorite(Product product) {
        if (this.favorites != null) {
            this.favorites.remove(product);
        }
        return this;
    }
}
