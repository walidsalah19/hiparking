package com.example.hibarking.garage_manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hibarking.R;
import com.example.hibarking.garage_manager.adapters.garage_show_adapter;
import com.example.hibarking.garage_manager.adapters.recycler_show_garage_info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class display_garages extends Fragment {
    private ArrayList<recycler_show_garage_info> arrayList;
    private RecyclerView recyclerview;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private String user_id;
    private FloatingActionButton add_new_garage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_display_garages, container, false);
       firebase_describtion();
       FloatingActionButton_method(v);
       recyclerview_method(v);
       return v;
    }
    private void FloatingActionButton_method(View v)
    {
        add_new_garage=v.findViewById(R.id.add_new_garage);
        add_new_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_fragment(new add_garage_info());
            }
        });
    }
    private void move_fragment(Fragment Fragment)
    {
       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.grarage_manager_frameLayout,Fragment).addToBackStack(null).commitAllowingStateLoss();
    }
    private void firebase_describtion()
    {
        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
    }
    private void recyclerview_method(View v) {
        recyclerview=v.findViewById(R.id.grarage_manager_recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList=new ArrayList<>();
        get_garage_data();

    }
    private void get_garage_data() {
        database.collection("garage_requist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String u_id=document.get("manager_id").toString();
                        if (user_id.equals(u_id)) {
                            recycler_show_garage_info data = new recycler_show_garage_info(document.get("garage_name").toString(), document.get("city").toString(), document.get("garage_id").toString());
                            arrayList.add(data);
                        }
                    }
                    garage_show_adapter adapter=new  garage_show_adapter(arrayList,display_garages.this);
                    recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}