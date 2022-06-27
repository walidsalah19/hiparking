package com.example.hibarking.driver.payment_package;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.SharedPref;
import com.fevziomurtekin.payview.Payview;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link payment_faragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class payment_faragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public payment_faragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment payment_faragment.
     */
    // TODO: Rename and change types and number of parameters
    public static payment_faragment newInstance(String param1, String param2) {
        payment_faragment fragment = new payment_faragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    SharedPref sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_faragment, container, false);
        Payview payview = v.findViewById(R.id.payview);

        // on below line we are setting pay on listener for our card.
        payview.setPayOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // after clicking on pay we are displaying toast message as card added.
                Toast.makeText(getContext(), "Card Added. ", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}