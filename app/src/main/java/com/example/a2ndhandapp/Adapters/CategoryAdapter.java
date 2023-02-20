package com.example.a2ndhandapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a2ndhandapp.Interfaces.GetCategoryCallback;
import com.example.a2ndhandapp.Models.User;
import com.example.a2ndhandapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

/**
 * This adapter is for the RV of the categories options in the Search page
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<String> categories;
    private GetCategoryCallback categoryCallback;
    private int[] categoryImages = {R.drawable.all_icon, R.drawable.accessory_icon, R.drawable.books_icon,
            R.drawable.clothes_icon, R.drawable.electronics_icon, R.drawable.home_icon,
            R.drawable.shoes_icon, R.drawable.sports_icon, R.drawable.toys_icon,
            R.drawable.other_icon};
    private User currentUser;

    public CategoryAdapter(Context context, ArrayList<String> categories) {
        this.context = context;
        this.categories = categories;
    }

    public CategoryAdapter setCategoryCallback(GetCategoryCallback categoryCallback) {
        this.categoryCallback = categoryCallback;
        return this;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_category, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = getItem(position);

        holder.category_LBL_name.setText(category + "");

        Glide.
                with(CategoryAdapter.this.context)
                .load(categoryImages[position])
                .placeholder(R.drawable.temporary_img)
                .into(holder.category_IMG_image);

    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    private String getItem(int position) {
        return categories.get(position);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView category_IMG_image;
        private MaterialTextView category_LBL_name;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category_IMG_image = itemView.findViewById(R.id.category_IMG_image);
            category_LBL_name = itemView.findViewById(R.id.category_LBL_name);

            itemView.setOnClickListener(v -> {
                if (categoryCallback != null) {
                    categoryCallback.categoryClicked(getItem(getAdapterPosition()),
                            getAdapterPosition());
                }
            });
        }
    }

}
