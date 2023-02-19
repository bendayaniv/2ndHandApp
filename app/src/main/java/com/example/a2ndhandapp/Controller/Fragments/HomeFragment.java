package com.example.a2ndhandapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Adapters.StringAutoCompleteTextAdapter;
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
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    private RecyclerView home_RV_products;
    private ArrayList<Product> productsByCategory = new ArrayList<>();
    private ProductItemAdapter productAdapter;
    private GetProductCallback getProductCallback;
    private final ArrayList<String> sortOptions = new ArrayList<>();
    private AutoCompleteTextView home_auto_complete_text_sort;


    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);

        initView();

        initProductRV();

        return view;
    }

    private void initView() {
        readProductsByCategoryFromBD();

        getSortOptionsFromDB();

        StringAutoCompleteTextAdapter sortAutoCompleteTextAdapter = new StringAutoCompleteTextAdapter(getContext(), sortOptions);
        home_auto_complete_text_sort.setAdapter(sortAutoCompleteTextAdapter);

        home_auto_complete_text_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sortStyle = parent.getItemAtPosition(position).toString();
                CurrentUser.getInstance().setLastSortStyle(sortStyle);
                optionsToSortProducts(sortStyle);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void optionsToSortProducts(String sortStyle) {
        switch (sortStyle) {
            case "Price: Low to High":
                Collections.sort(productsByCategory, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if (o1.getPrice() == o2.getPrice())
                            return 0;
                        else if (o1.getPrice() > o2.getPrice())
                            return 1;
                        else
                            return -1;
                    }
                });
                break;
            case "Price: High to Low":
                Collections.sort(productsByCategory, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if (o1.getPrice() == o2.getPrice())
                            return 0;
                        else if (o1.getPrice() > o2.getPrice())
                            return -1;
                        else
                            return 1;
                    }
                });
                break;
            case "ABC: A to Z":
                Collections.sort(productsByCategory, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case "ABC: Z to A":
                Collections.sort(productsByCategory, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o2.getName().compareTo(o1.getName());
                    }
                });
                break;
        }
        home_RV_products.getAdapter().notifyDataSetChanged();
    }

    private void initProductRV() {
        productAdapter = new ProductItemAdapter(getContext(), productsByCategory);
        home_RV_products.setLayoutManager(new LinearLayoutManager(getContext()));
        home_RV_products.setAdapter(productAdapter);

        productAdapter.setProductItemCallback(new ProductItemCallback() {
            @SuppressLint("NotifyDataSetChanged")
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
        home_auto_complete_text_sort = view.findViewById(R.id.home_auto_complete_text_sort);
//        home_abc_auto_complete_text = view.findViewById(R.id.home_abc_auto_complete_text);
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
                if(CurrentUser.getInstance().getLastSortStyle() != null){
                    optionsToSortProducts(CurrentUser.getInstance().getLastSortStyle());
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getSortOptionsFromDB() {
        DatabaseReference pricesSortRef = FirebaseDatabase.getInstance().getReference("SortOptions");

        pricesSortRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    sortOptions.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        sortOptions.add(ds.getValue().toString());
                    }
                }
            }
        });
    }

}
