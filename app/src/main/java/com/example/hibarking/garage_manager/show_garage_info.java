package com.example.hibarking.garage_manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hibarking.R;
import com.example.hibarking.SharedPref;
import com.example.hibarking.garage_manager.garage_data.map;
import com.example.hibarking.garage_manager.garage_data.move_location;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class show_garage_info extends Fragment {
private ProgressBar progressBar;
     private EditText name,price,num_unit,city,rate;
     private ImageButton location,paper;
     private FirebaseFirestore database;
     private String garage_id,latitude,longitude,url;
     SharedPref sharedPref;
     ProgressDialog dialog;
     private TextView parsent;
    double rate_num=0;
    int max_unit,booking_num=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_show_garage_info, container, false);
        garage_id=getArguments().getString("garage_id").toString();
        intialization_tool(v);
        get_garage_data();
        download_file(v);
       show_location(v);
        return v;
    }

    private void show_location(View v) {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_location.setLatitude(latitude);
                move_location.setLongitude(longitude);
                move_location.setType("shaw");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.grarage_manager_frameLayout, new map()).addToBackStack(null).commit();
            }
        });
    }

    private void download_file(View v) {
        paper=v.findViewById(R.id.sh_garage_paper);
        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                getActivity().startActivity(intent);
            }
        });
    }

    private void intialization_tool(View v)
    {
        progressBar=v.findViewById(R.id.progress_bar);
        parsent=v.findViewById(R.id.garage_persent_booking);
        name=v.findViewById(R.id.sh_garage_name);
        city=v.findViewById(R.id.sh_garage_city);
        price=v.findViewById(R.id.sh_garage_hour_price);
        num_unit=v.findViewById(R.id.sh_garage_name);
        location=v.findViewById(R.id.sh_garage_location);
        rate=v.findViewById(R.id.sh_garage_hour_rate);
        paper=v.findViewById(R.id.sh_garage_paper);
    }
    private void get_garage_data()
    {
        database=FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
        database.collection("garage_requist").document(garage_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    name.setText(task.getResult().get("garage_name").toString());
                    city.setText(task.getResult().get("city").toString());
                    price.setText(task.getResult().get("hour_price").toString());
                    num_unit.setText(task.getResult().get("unit_num").toString());
                    latitude=task.getResult().get("latitude").toString();
                    longitude=task.getResult().get("longitude").toString();
                    url=task.getResult().get("garage_paper").toString();
                    getrate();
                    get_booking_analysis();
                }
            }
        });
    }
    int count=0;
    private void getrate()
    {
       
        database.collection("garage_rate").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.get("garage_id").toString();
                        if (garage_id.equals(id))
                        {
                            count++;
                           rate_num+=Double.parseDouble(document.get("rate").toString());
                        }
                    }
                }
            }
        });
        rate.setText((rate_num/count)+"");
        dialog.dismiss();
    }
    private void get_booking_analysis()
    {
        max_unit=Integer.parseInt(num_unit.getText().toString());
        database.collection("booking").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String id = document.get("garage_id").toString();
                    if (garage_id.equals(id))
                    {
                        booking_num++;
                        progressBar_method(booking_num);
                    }
                }
            }
        });

    }
    private void progressBar_method(int num) {
        float persent=(num/max_unit)*100;
        parsent.setText(persent+" %");
        progressBar.setMax(max_unit);
        progressBar.setProgress(num);
    }

}