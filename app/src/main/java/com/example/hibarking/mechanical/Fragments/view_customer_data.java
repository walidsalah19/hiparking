package com.example.hibarking.mechanical.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.R;


public class view_customer_data extends Fragment {

    TextView name, description, date ;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialization();

        if(getArguments() != null){
            name.setText(getArguments().getString("Name"));
            description.setText(getArguments().getString("Description"));
            date.setText(getArguments().getString("Date"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_customer_data, container, false);
    }

    public void initialization(){
        name = getActivity().findViewById(R.id.mechanical_driver_name);
        description = getActivity().findViewById(R.id.mechanical_driver_description);
        date = getActivity().findViewById(R.id.mechanical_driver_date);
    }
}