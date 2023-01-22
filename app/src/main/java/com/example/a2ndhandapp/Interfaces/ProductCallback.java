package com.example.a2ndhandapp.Interfaces;

import com.example.a2ndhandapp.Models.Product;

public interface ProductCallback {
    void favoriteClicked(Product product, int position);
    void itemClicked(Product product, int position);
}
