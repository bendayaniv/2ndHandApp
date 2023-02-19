package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView home_RV_products;
    private ArrayList<Product> productsByCategory = new ArrayList<>();
    private ProductItemAdapter productAdapter;
    private GetProductCallback getProductCallback;


    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);

        initProductRV();

        readProductsByCategoryFromBD();

        return view;
    }

    private void initProductRV() {
        productAdapter = new ProductItemAdapter(getContext(), productsByCategory);
        home_RV_products.setLayoutManager(new LinearLayoutManager(getContext()));
        home_RV_products.setAdapter(productAdapter);

        productAdapter.setProductItemCallback(new ProductItemCallback() {
            @Override
            public void favoriteClicked(Product product, int position) {
                CurrentUser.getInstance().getUser().addFavorite(product);
                productsByCategory.remove(productsByCategory.get(position));
                home_RV_products.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void itemClicked(Product product, int position) {
                if (getProductCallback != null) {
                    getProductCallback.getProduct(product);
                }
            }
        });
    }

    private void findViews(View view) {
        home_RV_products = view.findViewById(R.id.home_RV_products);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void readProductsByCategoryFromBD() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsByCategory.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        if (product != null) {
                            if (!CurrentUser.getInstance().getUser().isMyProduct(product)
                                    && !CurrentUser.getInstance().getUser().isFavorite(product)) {
                                if (CurrentUser.getInstance().getCurrentCategory().equals(product.getCategory())) {
                                    productsByCategory.add(product);
                                } else if (CurrentUser.getInstance().getCurrentCategory().equals("All")) {
                                    productsByCategory.add(product);
                                }
                            }
                        }
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
