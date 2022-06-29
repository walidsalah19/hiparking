package com.example.hibarking.mechanical.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hibarking.R;
import com.example.hibarking.data_class.DateDrivers;
import com.example.hibarking.data_class.Driver;
import com.example.hibarking.data_class.DriverMechanicalRegister;
import com.example.hibarking.data_class.garage_model;
import com.example.hibarking.mechanical.adapter;
import com.example.hibarking.mechanical.main_mechanical;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ViewDrivers extends Fragment {

    RecyclerView recyclerview;
    adapter adapter;
    String currentUser_id;
    ArrayList<DateDrivers> drivers;
    ArrayList<DriverMechanicalRegister> arr;
    ArrayList<Driver> driver;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView_method();
        add_data_array();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_drivers, container, false);
    }
    private void add_data_array()
    {
        drivers=new ArrayList<>();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        currentUser_id=auth.getCurrentUser().getUid();
        FirebaseFirestore fireStore=FirebaseFirestore.getInstance();
        fireStore.collection("mechanical_divers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("mechanical_id").equals(currentUser_id)){
                            drivers.add(new DateDrivers(document.getString("driver_id"),document.getString("date")));
                        }
                    }
                    getDriverData();

                }
            }
        });

    }
    private void RecyclerView_method()
    {
        driver= new ArrayList<>();
        recyclerview=getActivity().findViewById(R.id.mechanical_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter =new adapter(driver,new main_mechanical());
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new adapter.OnItemClickListener() {
            @Override
            public void onItemUnRegister(String name, String uid, String date) {
                Fragment selected;
                Bundle bundle = new Bundle();
                bundle.putString("UID" ,uid);
                bundle.putString("Name",name);
                bundle.putString("Date",date);
                selected = new view_customer_data();
                selected.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.machanical_framelayout,selected).addToBackStack(null).commitAllowingStateLoss();

            }
        });
    }
    private void getDriverData() {
        for (int i = 0; i < drivers.size(); i++) {
            FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
            int finalI = i;
            fireStore.collection("User").document(drivers.get(i).getDriver()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    driver.add(new Driver(task.getResult().getString("uid").toString(),task.getResult().getString("name").toString(),"","","","",drivers.get(finalI).getDate().toString()));
                    adapter.notifyDataSetChanged();

                }
            });

        }
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}