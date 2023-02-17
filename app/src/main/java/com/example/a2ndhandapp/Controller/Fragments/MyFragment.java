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

import com.example.a2ndhandapp.Adapters.MyProductItemAdapter;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.CurrentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    private FirebaseDatabase firebaseDB;
    private RecyclerView my_RV_products;
    private ArrayList<Product> allProducts = new ArrayList<>();
    private MyProductItemAdapter myProductAdapter;
    private GetProductCallback getProductCallback;

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        firebaseDB = FirebaseDatabase.getInstance();

        findViews(view);

        initViews();

        return view;
    }

    private void initProductRV() {
        readAllProductsFromDB();

        myProductAdapter = new MyProductItemAdapter(getContext(), allProducts);
        my_RV_products.setLayoutManager(new LinearLayoutManager(getContext()));
        my_RV_products.setAdapter(myProductAdapter);
        myProductAdapter.setProductItemCallback(new ProductItemCallback() {

            @Override
            public void favoriteClicked(Product product, int position) {

            }

            @Override
            public void itemClicked(Product product, int position) {
                if (getProductCallback != null) {
                    getProductCallback.getProduct(product);
                }
            }
        });
    }

    private void initViews() {
        initProductRV();
    }

    private void findViews(View view) {
        my_RV_products = view.findViewById(R.id.my_RV_products);
    }


    private void readAllProductsFromDB() {
        DatabaseReference productsRef = firebaseDB.getReference("Products");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    allProducts.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Product product = ds.getValue(Product.class);
                        if (CurrentUser.getInstance().getUser().isMyProduct(product)) {
                            allProducts.add(product);
                        }
                    }
                    myProductAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
