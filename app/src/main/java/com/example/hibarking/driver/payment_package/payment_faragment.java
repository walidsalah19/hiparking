package com.example.hibarking.driver.payment_package;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.SharedPref;
import com.example.hibarking.driver.google_map.MapsFragment;
import com.fevziomurtekin.payview.Payview;
public class payment_faragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_faragment, container, false);
        Button payview =(Button) v.findViewById(R.id.skap);

        // on below line we are setting pay on listener for our card.
       payview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Bundle b=new Bundle();
               b.putString("type","garage");
               MapsFragment m=new MapsFragment();
               m.setArguments(b);
             getActivity().getSupportFragmentManager().beginTransaction()
                       .add(R.id.main_framelayout, m).commit();           }
       });
        return v;
    }
}