package com.example.a2ndhandapp.Interfaces;

import com.example.a2ndhandapp.Models.Product;

public interface SingleProductItemCallback {
    void favoriteClicked(Product product, int position);
    void closeClicked(Product product, int position);
}
