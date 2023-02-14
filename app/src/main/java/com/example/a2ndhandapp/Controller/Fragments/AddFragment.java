package com.example.a2ndhandapp.Controller.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.R;
import com.example.a2ndhandapp.Utils.Database;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddFragment extends Fragment {

    private AppCompatEditText add_EDT_productName;
    private AppCompatEditText add_EDT_productPrice;
    private AppCompatEditText add_EDT_productDescription;
    private TextInputLayout add_TIL_productCategory;
    private MaterialButton add_BTN_addProdImg;
    private ImageView add_IMG_productImage;
    private MaterialButton add_BTN_addProduct;
    private final int GALLERY_REQUEST_CODE = 1000;
    private ArrayList<String> categories = new ArrayList<>();
    private AutoCompleteTextView auto_complete_text;
    private ArrayAdapter<String> adapterItems;


    private GetProductCallback getProductCallback;

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        findViews(view);


        add_BTN_addProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK); // ACTION_PICK - open gallery
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // set the data to the gallery
                startActivityForResult(iGallery, GALLERY_REQUEST_CODE); // start the activity with the gallery
            }
        });

        categories = Database.getInstance().getCategories();
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.list_item, categories);

        auto_complete_text.setAdapter(adapterItems);
        auto_complete_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        add_BTN_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = add_EDT_productName.getText().toString();
                String productPrice = add_EDT_productPrice.getText().toString();
//                String productDescription = add_EDT_productDescription.getText().toString();
                String productCategory = add_TIL_productCategory.getEditText().getText().toString();
//                String productImage = add_IMG_productImage.toString();

                if (productName.isEmpty() || productPrice.isEmpty() || productCategory.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!productPrice.chars().allMatch(Character::isDigit)) {
                    Toast.makeText(getContext(), "Price is only with numbers", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    getProductCallback.backToHome();
                }
            }
        });


        return view;
    }

    /**
     * This method is called when the fragment is created, and find the views in the fragment
     *
     * @param view == the view of the fragment
     */
    private void findViews(View view) {
        add_EDT_productName = view.findViewById(R.id.add_EDT_productName);
        add_EDT_productPrice = view.findViewById(R.id.add_EDT_productPrice);
        add_EDT_productDescription = view.findViewById(R.id.add_EDT_productDescription);
        add_TIL_productCategory = view.findViewById(R.id.add_TIL_productCategory);
        add_IMG_productImage = view.findViewById(R.id.add_IMG_productImage);
        add_BTN_addProdImg = view.findViewById(R.id.add_BTN_addProdImg);
        add_BTN_addProduct = view.findViewById(R.id.add_BTN_addProduct);

        auto_complete_text = view.findViewById(R.id.auto_complete_text);
    }

    /**
     * This method is called when the activity is created, and find the views in the activity
     *
     * @param requestCode == the request code
     * @param resultCode  == the result code
     * @param data        == the data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { // if the result is ok
            if (requestCode == GALLERY_REQUEST_CODE) { // if the request code is the gallery request code
                add_IMG_productImage.setImageURI(data.getData()); // set the image to the image view
            }
        }
    }
}
