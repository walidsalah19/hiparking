package com.example.hibarking.driver.booking_package;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.garage_manager.main_garage_manager;
import com.example.hibarking.mechanical.main_mechanical;

import java.util.Calendar;

public class booking_fragment extends Fragment {


    private TextView text_date,text_time;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    final Calendar myCalendar = Calendar.getInstance();
    private RadioButton permenent,nonpermenent;
    private LinearLayout duration_layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v= inflater.inflate(R.layout.fragment_booking_fragment, container, false);
        text_date_method(v);
        text_time_method(v);
        catagories(v);
         return v;
    }
    private void catagories(View v)
    {
        permenent=v.findViewById(R.id.permenent_redio);
        nonpermenent=v.findViewById(R.id.nonpermenent_redio);
        duration_layout=v.findViewById(R.id.duration_layout);

        permenent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                duration_layout.setVisibility(View.INVISIBLE);
                duration_layout.getLayoutParams().height = 0;
                duration_layout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                duration_layout.requestLayout();
            }
        });
       nonpermenent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               duration_layout.setVisibility(View.VISIBLE);
               duration_layout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
               duration_layout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
               duration_layout.requestLayout();
           }
       });

    }
    private void text_date_method(View v)
    {
        text_date=v.findViewById(R.id.date_booking_driver);
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog_method();
            }
        });
    }
    private void datePickerDialog_method()
    {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                text_date.setText(year + "-" + monthOfYear+1 + "-" + dayOfMonth);
            }
        };
        datePickerDialog=  new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void text_time_method(View v)
    {
        text_time=v.findViewById(R.id.time_booking_driver);
        text_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog_method();
            }
        });
    }
    private void timePickerDialog_method()
    {

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                text_time.setText(hourOfDay + ":" + minute );

            }
        };
        timePickerDialog = new TimePickerDialog(getActivity(),
                timeSetListener, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),false);

        timePickerDialog.show();
    }
}