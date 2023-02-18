package com.example.a2ndhandapp.Controller.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Adapters.ProductItemAdapter;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    
    private RecyclerView favorites_RV_products;
    private ArrayList<Product> allMyFavoritesProducts = new ArrayList<>();
    private ProductItemAdapter productAdapter;
    private GetProductCallback getProductCallback;

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        findViews(view);

        initProductRV();

        return view;
    }

    private void initProductRV() {
        readAllFavoritesProductsFromDB();

        productAdapter = new ProductItemAdapter(getContext(), allMyFavoritesProducts);
        favorites_RV_products.setLayoutManager(new LinearLayoutManager(getContext()));
        favorites_RV_products.setAdapter(productAdapter);
        productAdapter.setProductItemCallback(new ProductItemCallback() {

            @Override
            public void favoriteClicked(Product product, int position) {
                if (CurrentUser.getInstance().getUser().isFavorite(product)) {
                    CurrentUser.getInstance().getUser().removeFavorite(product);
                } else {
                    CurrentUser.getInstance().getUser().addFavorite(product);
                }

                favorites_RV_products.getAdapter().notifyItemChanged(position);
                initProductRV();
            }

            @Override
            public void itemClicked(Product product, int position) {
                if (getProductCallback != null) {
                    getProductCallback.getProduct(product);
                }
            }
        });
    }

    private void readAllFavoritesProductsFromDB() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    allMyFavoritesProducts.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        if (CurrentUser.getInstance().getUser().isFavorite(product)) {
                            allMyFavoritesProducts.add(product);
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void findViews(View view) {
        favorites_RV_products = view.findViewById(R.id.favorites_RV_products);
    }
}
