package com.example.a2ndhandapp.Controller.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Adapters.ProductItemAdapter;
import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
//import com.example.realtimedatabase.databinding.ActivityReadDataBinding;
import com.example.a2ndhandapp.Utils.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private AutoCompleteTextView auto_complete_text_category;
    private ArrayAdapter<String> adapterCategories;
    private ArrayList<String> categories = new ArrayList<>();
    private AutoCompleteTextView auto_complete_text_price;
    private ArrayAdapter<String> adapterPrices;
    private ArrayList<String> prices = new ArrayList<>();
    private RecyclerView home_LST_products;
    private ProductItemAdapter productAdapter;
    private User currentUser;
    private GetProductCallback getProductCallback;

    ArrayList<Product> products = new ArrayList<>();

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        Toast.makeText(getContext(), "HomeFragment", Toast.LENGTH_SHORT).show();

        findViews(view);

        initViews();

        return view;
    }

    public void getProducts() {
        ArrayList<Product> tempProducts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            tempProducts.add(new Product("Product " + i,
                    "asd " + i,
                    "" + i,
                    "Category " + i,
                    "UID " + i,
                    null, null));
        }
        products = tempProducts;
    }

    private void initViews() {
//        Toast.makeText(getContext(), "initViews", Toast.LENGTH_SHORT).show();
//        categories.clear();
//        if (categories.isEmpty())
//            categories = Database.getCategories();
//        Log.d("TAG", "initViews: " + categories);
////        prices.clear();
//        if (prices.isEmpty())
//            prices = Database.getPrices();

        getProducts();

        initProductRV();
        initCategoryList();
        initPriceList();
    }

    private void initPriceList() {
        if (prices.isEmpty()) {
            prices = Database.getPrices();
        }
        adapterPrices = new ArrayAdapter<String>(getContext(), R.layout.list_item, (List<String>) prices);
        auto_complete_text_price.setAdapter(adapterPrices);

        auto_complete_text_price.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String price = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Price: " + price, Toast.LENGTH_SHORT).show();
//                products.sort(Comparator.comparing(products::getPrice));
                Collections.sort(products, Comparator.comparing(Product::getPrice));
                if (price.equals("Higher to Lower")) {
                    Collections.reverse(products);
                }
                initProductRV();
            }
        });
    }

    private void initCategoryList() {
        if (categories.isEmpty()) {
            categories = Database.getCategories();
        }
        Log.d("asdasdasd", "" + auto_complete_text_category.getText().toString());
        Log.d("TAG", "initViews: " + categories);
        adapterCategories = new ArrayAdapter<String>(getContext(), R.layout.list_item, categories);
        auto_complete_text_category.setAdapter(adapterCategories);
        auto_complete_text_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                Log.d("asdasdasd", "" + auto_complete_text_category.getText().toString());
                Toast.makeText(getContext(), "Category: " + category, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initProductRV() {
        productAdapter = new ProductItemAdapter(getContext(), products);
        home_LST_products.setLayoutManager(new LinearLayoutManager(getContext()));
        home_LST_products.setAdapter(productAdapter);
        productAdapter.setProductItemCallback(new ProductItemCallback() {
            @Override
            public void favoriteClicked(Product product, int position) {
                if (currentUser != null) {
                    if (currentUser.getFavorites().contains(product)) {
                        currentUser.getFavorites().remove(product);
                        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        currentUser.getFavorites().add(product);
                        Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    }
                    home_LST_products.getAdapter().notifyItemChanged(position);
                }
                if (getProductCallback != null) {
                    Toast.makeText(getContext(), "Favorite clicked " + product.getName(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "asdasdasdasdasd", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemClicked(Product product, int position) {
                if (getProductCallback != null) {
                    Toast.makeText(getContext(), "position:  " + position, Toast.LENGTH_SHORT).show();
                    getProductCallback.getProduct(product);
                }
                // TODO - To use callback to transfer the information to the main activity so I know what to do
////                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new HomeFragment()).commit(); // Set the home fragment
////                getSupportActionBar().setTitle("Home"); // Set the title of the toolbar
            }
        });
    }

    private void findViews(View view) {
        auto_complete_text_category = view.findViewById(R.id.auto_complete_text_category);
        auto_complete_text_price = view.findViewById(R.id.auto_complete_text_price);
        home_LST_products = view.findViewById(R.id.home_LST_products);
    }


}
