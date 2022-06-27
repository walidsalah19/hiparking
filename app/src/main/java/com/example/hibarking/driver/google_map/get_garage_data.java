package com.example.hibarking.driver.google_map;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hibarking.data_class.garage_model;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class get_garage_data {
    private GoogleMap mMap;
    Fragment fragment;
    ArrayList <garage_model>arr;
    String name,unit,hour;
    public get_garage_data(GoogleMap mMap, Fragment fragment) {
        this.mMap = mMap;
        this.fragment = fragment;
    }
     public ArrayList<garage_model> get_garage_data()
       {
            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
           ArrayList<garage_model> mArrayList;
           arr=new ArrayList<>();
           mArrayList = new ArrayList<>();

           firestore.collection("garage_requist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                          garage_model data= new garage_model(document.get("garage_name").toString(),document.get("city").toString(),
                                  document.get("garage_id").toString(),document.get("manager_id").toString(),document.get("garage_paper").toString()
                          ,Double.parseDouble(document.get("latitude").toString()),Double.parseDouble(document.get("longitude").toString()),
                                  Integer.parseInt(document.get("unit_num").toString()),Integer.parseInt(document.get("hour_price").toString()));
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

        for (int i = 0; i < arr.size(); i++) {
            // adding marker to each location on google maps
            LatLng lat=new LatLng(arr.get(i).getLatitude(), arr.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(lat)
                    .title(arr.get(i).getGarage_name()))
            ;
        }
    }
     int  count=0;
       public  void get_number_of_booking(String id,String name,String hour,String unit)
       {
           this.name=name;
           this.hour=hour;
           this.unit=unit;
           FirebaseFirestore firestore=FirebaseFirestore.getInstance();
           firestore.collection("booking").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           String g_id=document.get("garage_id").toString();
                           if (id.equals(g_id))
                           {
                               ++count;
                           }
                       }
                       get_rate(id);
                   }
               }
           });
       }
        double  rate=0.0;
        int rate_count=0;
       public  void get_rate(String id)
       {
           FirebaseFirestore firestore=FirebaseFirestore.getInstance();
           firestore.collection("garage_rate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           String g_id=document.get("garage_id").toString();
                           if (id.equals(g_id))
                           {
                               rate_count++;
                               rate+=Double.parseDouble(document.get("rate").toString());
                           }
                       }
                       Bottom_Sheet_Menu m=new Bottom_Sheet_Menu(id,name,(Integer.parseInt(unit)-count)+" / "+unit,
                               rate/rate_count+" ",hour+" ");
                       m.show(fragment.getChildFragmentManager(),"MapsFragment");
                       count=0;
                       rate=0.0;
                       rate_count=0;
                   }
               }
           });
       }

}
