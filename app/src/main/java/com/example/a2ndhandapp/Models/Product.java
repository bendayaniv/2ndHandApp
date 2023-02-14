package com.example.a2ndhandapp.Models;

import java.util.ArrayList;

public class Product {

    private String name;
    private String description;
    private String price;
    private String category;
    private ArrayList<String> images;


    public Product() {
    }

    public Product(String name, String description, String price, String category, ArrayList<String> images) {
        this.name = name;
        setDescription(description);
        this.price = price;
        this.category = category;
        this.images = images;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public Product setImages(ArrayList<String> images) {
        this.images = images;
        return this;
    }
}
