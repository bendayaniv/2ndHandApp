package com.example.a2ndhandapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a2ndhandapp.Interfaces.MyProductItemCallback;
import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MyProductItemAdapter extends RecyclerView.Adapter<MyProductItemAdapter.MyProductViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private MyProductItemCallback myProductCallback;

    public MyProductItemAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public MyProductItemAdapter setMyProductItemCallback(MyProductItemCallback myProductCallback) {
        this.myProductCallback = myProductCallback;
        return this;
    }

    @NonNull
    @Override
    public MyProductItemAdapter.MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_item, parent, false);
        MyProductViewHolder myProductViewHolder = new MyProductViewHolder(view);
        return myProductViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductItemAdapter.MyProductViewHolder holder, int position) {
        Product currentProduct = getItem(position);

        holder.myProduct_LBL_name.setText(currentProduct.getName() + "");
        holder.myProduct_LBL_category.setText(currentProduct.getCategory() + "");
        holder.myProduct_LBL_price.setText(currentProduct.getPrice() + " NIS₪");

        Glide.
                with(MyProductItemAdapter.this.context)
                .load((String) null) // TODO:  change to currentProduct.getImages().get(0)
                .placeholder(R.drawable.temporary_img)
                .into(holder.myProduct_IMG_image);
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    private Product getItem(int position) {
        return products.get(position);
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView myProduct_IMG_image;
        private AppCompatImageView myProduct_IMG_delete;
        private MaterialTextView myProduct_LBL_name;
        private MaterialTextView myProduct_LBL_category;
        private MaterialTextView myProduct_LBL_price;


        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            myProduct_IMG_image = itemView.findViewById(R.id.myProduct_IMG_image);
            myProduct_IMG_delete = itemView.findViewById(R.id.myProduct_IMG_delete);
            myProduct_LBL_name = itemView.findViewById(R.id.myProduct_LBL_name);
            myProduct_LBL_category = itemView.findViewById(R.id.myProduct_LBL_category);
            myProduct_LBL_price = itemView.findViewById(R.id.myProduct_LBL_price);

            itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Clicked on " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Log.d("asdadasdasd", "asdasd: " + getItem(getAdapterPosition()).getName());
                myProductCallback.itemClicked(getItem(getAdapterPosition()),
                        getAdapterPosition());
            });

            myProduct_IMG_delete.setOnClickListener(v -> {
                if (myProductCallback != null) {
                    myProductCallback.deleteClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });


        }
    }
}
