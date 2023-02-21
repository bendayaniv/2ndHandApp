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

import com.example.a2ndhandapp.Adapters.CategoryAdapter;
import com.example.a2ndhandapp.Interfaces.ChoosingCategorySearchCallback;
import com.example.a2ndhandapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView search_RV_categories;
    private ArrayList<String> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    private ChoosingCategorySearchCallback categoryCallback;

    public void setCategoryCallback(ChoosingCategorySearchCallback categoryCallback) {
        this.categoryCallback = categoryCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        findViews(view);
        initCategoryRV();
        return view;
    }

    private void initCategoryRV() {
        getCategoriesFromDB();

        categoryAdapter = new CategoryAdapter(getContext(), categories);

        search_RV_categories.setLayoutManager(new LinearLayoutManager(getContext()));
        search_RV_categories.setAdapter(categoryAdapter);
        categoryAdapter.setChoosingCategorySearchCallback(new ChoosingCategorySearchCallback() {
            @Override
            public void categoryClicked() {
                categoryCallback.categoryClicked();
            }
        });
    }

    private void findViews(View view) {
        search_RV_categories = view.findViewById(R.id.search_RV_categories);
    }


    /**
     * Getting the categories options from the DB
     */
    public void getCategoriesFromDB() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");
        categoriesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    categories.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        categories.add(ds.getValue().toString());
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
