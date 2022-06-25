package com.example.hibarking.mechanical;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;

import java.util.ArrayList;

public class main_mechanical extends AppCompatActivity {
 private RecyclerView recyclerview;
 private ArrayList<String> arr;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mechanical);
            add_data_array();
            RecyclerView_method();
            toolpar_intialize();
    }
    private void toolpar_intialize() {
        toolbar = findViewById(R.id.mechanical_appbar_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.mechanical_drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,  drawerLayout, toolbar, R.string.close, R.string.open);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void add_data_array()
    {
        arr=new ArrayList<>();
        arr.add("customer");
        arr.add("customer");
        arr.add("customer");
        arr.add("customer");
        arr.add("customer");
        arr.add("customer");
        arr.add("customer");
        arr.add("customer");


    }
    private void RecyclerView_method()
    {

        recyclerview=findViewById(R.id.mechanical_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter adapter=new adapter(arr, main_mechanical.this);
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}