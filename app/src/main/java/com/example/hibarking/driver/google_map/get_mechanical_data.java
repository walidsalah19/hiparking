package com.example.hibarking.driver.google_map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hibarking.data_class.garage_model;
import com.example.hibarking.data_class.user_mechanical_data;
import com.example.hibarking.driver.user_mechanical.mechanical_user;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class get_mechanical_data {
    private GoogleMap mMap;
    Fragment fragment;
    ArrayList<user_mechanical_data> arr;
    String name;
    public get_mechanical_data(GoogleMap mMap, Fragment fragment) {
        this.mMap = mMap;
        this.fragment = fragment;
    }
    public ArrayList<user_mechanical_data> get_garage_data()
    {
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        ArrayList<garage_model> mArrayList;
        arr=new ArrayList<>();
        mArrayList = new ArrayList<>();

        firestore.collection("Mechanical").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user_mechanical_data data=new user_mechanical_data(document.get("name").toString(),
                                document.get("uid").toString(),document.get("phone").toString(),
                                Double.parseDouble( document.get("longitude").toString()), Double.parseDouble(document.get("latitude").toString())
                               );
                        arr.add(data);
                    }
                    add_garage_map();
                }

            }
        });
        return arr;
    }
    public void add_garage_map()
    {
        mMap.clear();
        for (int i = 0; i < arr.size(); i++) {
            // adding marker to each location on google maps
            LatLng lat=new LatLng(arr.get(i).getLatitude(), arr.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(lat)
                    .title(arr.get(i).getName()))
            ;
        }
    }

    double  rate=0.0;
    int rate_count=0;
    public  void get_rate(String id,String name)
    {
        this.name=name;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("mechanical_rate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String g_id=document.get("uid").toString();
                        if (id.equals(g_id))
                        {
                            rate_count++;
                            rate+=Double.parseDouble(document.get("rate").toString());
                        }
                    }
                    double rate_value=rate/rate_count;
                    if (rate==0.0)
                    {
                        rate_value=5.0;
                    }
                    Bottom_Sheet_Menu m=new Bottom_Sheet_Menu(id,name,"mechanical",
                            rate_value+" "," ");
                    m.show(fragment.getChildFragmentManager(),"MapsFragment");
                    rate=0.0;
                    rate_count=0;
                }
            }
        });
    }
}
