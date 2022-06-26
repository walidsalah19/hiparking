package com.example.hibarking.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.hibarking.R;

public class EmergancyFragment extends Fragment {
    private FrameLayout Tourism,Ambulance,Traffic,Police,Fire,Electricity,Gas_Emergency;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_emergancy, container, false);
        Tourism=v.findViewById(R.id.Tourism);
        Ambulance=v.findViewById(R.id.Ambulance);
        Traffic=v.findViewById(R.id.Traffic);
        Police=v.findViewById(R.id.Police);
        Fire=v.findViewById(R.id.Fire);
        Electricity=v.findViewById(R.id.Electricity);
        Gas_Emergency=v.findViewById(R.id.Gas_Emergency);
        Tourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Tourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        Gas_Emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("01159088197");
            }
        });
        return v;
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
}