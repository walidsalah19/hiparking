package com.example.hibarking.garage_manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hibarking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class show_garage_info extends Fragment {
private ProgressBar progressBar;
     private EditText name,price,num_unit,city,rate;
     private ImageButton location,paper;
     private FirebaseFirestore database;
     private String garage_id,latitude,longitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_show_garage_info, container, false);
        garage_id=getArguments().getString("garage_id").toString();
        progressBar_method(v);
        intialization_tool(v);

        return v;
    }
    private void intialization_tool(View v)
    {
        name=v.findViewById(R.id.sh_garage_name);
        city=v.findViewById(R.id.sh_garage_city);
        price=v.findViewById(R.id.sh_garage_hour_price);
        num_unit=v.findViewById(R.id.sh_garage_name);
        location=v.findViewById(R.id.sh_garage_location);
        rate=v.findViewById(R.id.sh_garage_hour_rate);
        paper=v.findViewById(R.id.sh_garage_paper);
    }


    private void progressBar_method(View v) {
        progressBar=v.findViewById(R.id.progress_bar);
        progressBar.setProgress(90);
    }
    private void get_garage_data()
    {
        database=FirebaseFirestore.getInstance();
        database.collection("garage_requist").document(garage_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    name.setText(task.getResult().get("garage_name").toString());
                    city.setText(task.getResult().get("garage_name").toString());
                    price.setText(task.getResult().get("garage_name").toString());
                    num_unit.setText(task.getResult().get("garage_name").toString());
                    name.setText(task.getResult().get("garage_name").toString());
                }
            }
        });
    }
}