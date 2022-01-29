package com.example.hibarking.user_mechanical;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hibarking.R;
import com.example.hibarking.chating.chating;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mechanical_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mechanical_user extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button user_chat;
    public mechanical_user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mechanical_user.
     */
    // TODO: Rename and change types and number of parameters
    public static mechanical_user newInstance(String param1, String param2) {
        mechanical_user fragment = new mechanical_user();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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