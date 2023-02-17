package com.example.a2ndhandapp.Models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public Product(String name, String description, String price, String category,
                   String sellerName, String sellerEmail, ArrayList<String> images) {
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
        return this.name.equals(product.getName()) &&
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

    public void removeProductFromDB(FirebaseDatabase firebaseDB) {
        DatabaseReference productRef = firebaseDB.getReference("Products");
        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    ArrayList<Product> allProducts = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);

                        if (product != null) {
                            // TODO - to handle with images
                            if (!this.theSameProduct(product)) {
                                allProducts.add(product);
                            }
                        }
                    }
                    productRef.setValue(allProducts);
                }
            }
        });
    }
}
