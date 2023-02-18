package com.example.a2ndhandapp.Models;

import java.util.ArrayList;

public class Product {

    private String name;
    private String description;
    private String price;
    private String category;
    private String sellerName;
    private String sellerEmail;
    private ArrayList<String> images;
    private String id;
    private static int counter = 0;


    public Product() {
    }

    public Product(String id, String name, String description, String price, String category,
                   String sellerName, String sellerEmail, ArrayList<String> images) {
        this.id = id;
        this.name = name;
        setDescription(description);
        this.price = price;
        this.category = category;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
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

    public boolean theSameProduct(Product product) {
        return this.id.equals(product.getId()) &&
                this.name.equals(product.getName()) &&
                this.description.equals(product.getDescription()) &&
                this.price.equals(product.getPrice()) &&
                this.category.equals(product.getCategory());
    }

    public String getSellerName() {
        return sellerName;
    }

    public Product setSellerName(String sellerName) {
        this.sellerName = sellerName;
        return this;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public Product setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
        return this;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Product setImages(ArrayList<String> images) {
        this.images = images;
        return this;
    }

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

//    public void removeProductFromDB(FirebaseDatabase firebaseDB) {
//        DatabaseReference productRef = firebaseDB.getReference("Products");
//
//        productRef.getRef().child(String.valueOf(this.id)).removeValue();
//    }
}
