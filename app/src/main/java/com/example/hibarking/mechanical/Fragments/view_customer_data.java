package com.example.hibarking.mechanical.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;

import com.example.hibarking.chating.chating;
import com.example.hibarking.driver.user_account.create_account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import com.example.hibarking.SharedPref;



public class view_customer_data extends Fragment {

    TextView name , date;
    CircleImageView imageView;
    String userId ,userDate,userName;
    AppCompatButton call, chat;
    String urls , phone ;
    SharedPref sharedPref;

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization();

        userId = getArguments().getString("UID");
        userName = getArguments().getString("Name");
        userDate = getArguments().getString("Date");
        name.setText(userName);
        date.setText(userDate);
        getDriverData();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(phone);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chating t=new chating();
                Bundle b=new Bundle();
                b.putString("id",userId);
                b.putString("type","Mechanical");
                t.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_contact_frame,t).addToBackStack(null).commitAllowingStateLoss();
            }
        });
    }

    public void initialization(){
        name = getActivity().findViewById(R.id.mec_customer_name);
        date = getActivity().findViewById(R.id.mec_customer_date);
        imageView = getActivity().findViewById(R.id.mec_customer_image);
        call = getActivity().findViewById(R.id.mec_customer_call);
        chat = getActivity().findViewById(R.id.mec_customer_chat);


    }

    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            //You already have permission
            try {
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }


    public void getDriverData(){
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();

        if (userId != null) {
            reference = firestore.collection("User").document(userId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        if (task.getResult().exists()) {

                            urls = task.getResult().getString("uri");
                            phone = task.getResult().getString("phone");
                            Picasso.get().load(urls).into(imageView);

                        }
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(getContext(), "" + nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_customer_data, container, false);
    }
}