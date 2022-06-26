package com.example.hibarking.mechanical.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hibarking.R;
import com.example.hibarking.data_class.DriverMechanicalRegister;
import com.example.hibarking.mechanical.adapter;
import com.example.hibarking.mechanical.main_mechanical;

import java.util.ArrayList;

public class ViewDrivers extends Fragment {
    private RecyclerView recyclerview;
    private  adapter adapter;
    private ArrayList<DriverMechanicalRegister> arr;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_data_array();
        RecyclerView_method();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_drivers, container, false);
    }
    private void add_data_array()
    {
        arr=new ArrayList<>();
        arr.add(new DriverMechanicalRegister("id1", "driver1","12/2/11344","my car break down on this location i need help please",133.45,134.3524));
        arr.add(new DriverMechanicalRegister("id2", "driver2","12/2/11344","my car break down on this location i need help please",133.45,134.3524));
        arr.add(new DriverMechanicalRegister("id3", "driver3","12/2/11344","my car break down on this location i need help please",133.45,134.3524));
        arr.add(new DriverMechanicalRegister("id4", "driver4","12/2/11344","mu car break down on this location i need help please",133.45,134.3524));
        arr.add(new DriverMechanicalRegister("id5", "driver5","12/2/11344","the car break down on this location i need help please",133.45,134.3524));

    }
    private void RecyclerView_method()
    {

        recyclerview=getActivity().findViewById(R.id.mechanical_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter =new adapter(arr,new main_mechanical());
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new adapter.OnItemClickListener() {
            @Override
            public void onItemUnRegister(String name, String uid, String description, String date) {
                Fragment selected;
                Bundle bundle = new Bundle();
                bundle.putString("UID" ,uid);
                bundle.putString("Name",name);
                bundle.putString("Description",description);
                bundle.putString("Date",date);
                selected = new view_customer_data();
                selected.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.machanical_framelayout,selected).addToBackStack(null).commitAllowingStateLoss();

            }
        });
    }
}