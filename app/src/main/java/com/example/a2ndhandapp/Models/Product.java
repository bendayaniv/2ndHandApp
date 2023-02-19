package com.example.a2ndhandapp.Models;

import java.util.ArrayList;

public class Product {

    private String name;
    private String description;
    private int price;
    private String category;
    private String sellerName;
    private String sellerEmail;
    private ArrayList<String> images;
    private String id;
    private String imageId;


    public Product() {
    }

    public Product(String id, String name, String description, int price, String category,
                   String sellerName, String sellerEmail, ArrayList<String> images, String imageId) {
        this.id = id;
        this.name = name;
        setDescription(description);
        this.price = price;
        this.category = category;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.images = images;
        this.imageId = imageId;
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

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public Product setCategory(String category) {
        this.category = category;
        return this;
    }

    public boolean theSameProduct(Product product) {
        return this.id.equals(product.getId()) &&
                this.name.equals(product.getName()) &&
                this.description.equals(product.getDescription()) &&
                this.price == product.getPrice() &&
                this.category.equals(product.getCategory());
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public String getImageId() {
        return imageId;
    }

}
