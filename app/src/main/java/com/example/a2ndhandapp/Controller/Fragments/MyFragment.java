package com.example.a2ndhandapp.Controller.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a2ndhandapp.Interfaces.GetProductCallback;
import com.example.a2ndhandapp.R;

public class MyFragment extends Fragment {

    private GetProductCallback getProductCallback;

    public void setGetProductCallback(GetProductCallback getProductCallback) {
        this.getProductCallback = getProductCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }
}
