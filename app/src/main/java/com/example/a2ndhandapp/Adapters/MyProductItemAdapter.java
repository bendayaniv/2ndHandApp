package com.example.a2ndhandapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2ndhandapp.Interfaces.ProductItemCallback;
import com.example.a2ndhandapp.Models.Product;
import com.example.a2ndhandapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This adapter is for the RV of the products  in the My page
 */
public class MyProductItemAdapter extends RecyclerView.Adapter<MyProductItemAdapter.MyProductViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private ProductItemCallback myProductCallback;

    public MyProductItemAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public MyProductItemAdapter setProductItemCallback(ProductItemCallback productCallback) {
        this.myProductCallback = productCallback;
        return this;
    }

    @NonNull
    @Override
    public MyProductItemAdapter.MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_product_item, parent, false);
        MyProductViewHolder myProductViewHolder = new MyProductViewHolder(view);
        return myProductViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyProductItemAdapter.MyProductViewHolder holder, int position) {
        Product currentProduct = getItem(position);

        holder.myProduct_LBL_name.setText(currentProduct.getName() + "");
        holder.myProduct_LBL_category.setText(currentProduct.getCategory() + "");
        holder.myProduct_LBL_price.setText(currentProduct.getPrice() + "₪");

        // The uploading image part from Storage Firebase
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/" + currentProduct.getImageId());

        try {
            File localFile = File.createTempFile("tempFile", ".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.myProduct_IMG_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.myProduct_IMG_image.setImageResource(R.drawable.temporary_img);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    private Product getItem(int position) {
        return products.get(position);
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView myProduct_IMG_image;
        private MaterialTextView myProduct_LBL_name;
        private MaterialTextView myProduct_LBL_category;
        private MaterialTextView myProduct_LBL_price;


        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            myProduct_IMG_image = itemView.findViewById(R.id.myProduct_IMG_image);
            myProduct_LBL_name = itemView.findViewById(R.id.myProduct_LBL_name);
            myProduct_LBL_category = itemView.findViewById(R.id.myProduct_LBL_category);
            myProduct_LBL_price = itemView.findViewById(R.id.myProduct_LBL_price);

            itemView.setOnClickListener(v -> {
                myProductCallback.itemClicked(getItem(getAdapterPosition()),
                        getAdapterPosition());
            });


        }
    }
}
