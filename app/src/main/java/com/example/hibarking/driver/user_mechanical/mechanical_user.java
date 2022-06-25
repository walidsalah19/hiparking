package com.example.hibarking.driver.user_mechanical;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hibarking.R;
import com.example.hibarking.chating.chating;

public class mechanical_user extends Fragment {

    private Button user_chat;
    public mechanical_user() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_mechanical_user, container, false);
        user_chat_method(v);
        return v;
    }
    private void user_chat_method(View v)
    {
        user_chat=v.findViewById(R.id.user_chat_mechanicl);
        user_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_mechanical,new chating()).addToBackStack(null).commitAllowingStateLoss();
            }
        });
    }
}