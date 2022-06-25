package com.example.hibarking.driver.google_map;

import androidx.annotation.NonNull;

import com.example.hibarking.data_class.garage_model;
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

public class get_location_firebase {

       public static  ArrayList<garage_model> get_garage_data()
       {
            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
            ArrayList<garage_model> mArrayList = new ArrayList<>();

           firestore.collection("garage").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                          garage_model data= document.toObject(garage_model.class);
                          mArrayList.add(data);
                       }
                   }
               }
           });
            return mArrayList;
       }
      static int  count=0;
       public static int get_number_of_booking(String id)
       {

           DatabaseReference database = FirebaseDatabase.getInstance().getReference("booking");
           database.child(id).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot .exists()) {
                       count = (int) snapshot.getChildrenCount();
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
           return count;
       }

}
