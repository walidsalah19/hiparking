package com.example.hibarking.garage_manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hibarking.R;

public class show_garage_info extends Fragment {
private ProgressBar progressBar;
     private TextView name,price,num_unit,city;
     private ImageButton location,paper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_show_garage_info, container, false);
        progressBar_method(v);

        return v;
    }
    private void intialization_tool(View v)
    {
        name=v.findViewById(R.id.sh_garage_name);
        city=v.findViewById(R.id.sh_garage_city);
        name=v.findViewById(R.id.sh_garage_hour_price);name=v.findViewById(R.id.sh_garage_name);
    }

    private void progressBar_method(View v) {
        progressBar=v.findViewById(R.id.progress_bar);
        progressBar.setProgress(90);
    }
}