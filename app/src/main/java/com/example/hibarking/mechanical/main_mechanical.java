package com.example.hibarking.mechanical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;

import java.util.ArrayList;

public class main_mechanical extends AppCompatActivity {
 private RecyclerView recyclerview;
 private ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mechanical);
add_data_array();
RecyclerView_method();
    }
    private void add_data_array()
    {
        arr=new ArrayList<>();
        arr.add("walid");
    }
    private void RecyclerView_method()
    {

        recyclerview=findViewById(R.id.mechanical_recyclerview);
        adapter adapter=new adapter(this,arr, main_mechanical.this);
    }
}