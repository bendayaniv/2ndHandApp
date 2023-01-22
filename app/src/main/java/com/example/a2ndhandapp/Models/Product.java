package com.example.a2ndhandapp.Models;

import java.util.ArrayList;

public class Product {

    private String name;
    private String description;
    private String price;
    private String category;
    private String uid;
    private ArrayList<String> images;
    private User user;
//    private ArrayList<User> userWhoLike;

    public Product() {
    }

    public Product(String name, String description, String price, String category, String uid, ArrayList<String> images, User user) {
        this.name = name;
//        this.description = description;
        setDescription(description);
        this.price = price;
        this.category = category;
        this.uid = uid;
        this.images = images;
        this.user = user;
//        userWhoLike = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Product setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Product setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Product setImages(ArrayList<String> images) {
        this.images = images;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Product setUser(User user) {
        this.user = user;
        return this;
    }

//    public Product addToUserWhoLike(User user){
//        userWhoLike.add(user);
//        return this;
//    }
//
//    public Product removeFromUserWhoLike(User user){
//        userWhoLike.remove(user);
//        return this;
//    }
}
