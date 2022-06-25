package com.example.hibarking.driver.google_map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.hibarking.R;
import com.example.hibarking.driver.booking_package.booking_fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class Bottom_Sheet_Menu extends BottomSheetDialogFragment {

    private String garage_id,name,unit,rate,price;

    private TextView location_name,location_unit,location_rate,location_price;

    public Bottom_Sheet_Menu(String garage_id, String name, String unit, String rate, String price) {
        this.garage_id = garage_id;
        this.name = name;
        this.unit = unit;
        this.rate = rate;
        this.price = price;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        location_name = v.findViewById(R.id.bottomsheet_name);
        location_rate = v.findViewById(R.id.bottomsheet_rate);
        location_unit = v.findViewById(R.id.bottomsheet_apace);
        location_price = v.findViewById(R.id.bottomsheet_price);
        location_name.setText(name);
        location_rate.setText(rate);
        location_price.setText(price);
        location_unit.setText(unit);
        Button booking;
        booking = (Button) v.findViewById(R.id.user_main_booking);
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (garage_id != null) {
                    Bundle b = new Bundle();
                    b.putString("id", garage_id);
                    booking_fragment booking = new booking_fragment();
                    booking.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, booking).addToBackStack(null).commitAllowingStateLoss();
                }
            }
        });
        return v;
    }
}
