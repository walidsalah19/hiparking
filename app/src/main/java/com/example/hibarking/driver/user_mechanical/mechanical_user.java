package com.example.hibarking.driver.user_mechanical;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hibarking.R;
import com.example.hibarking.chating.chating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class mechanical_user extends Fragment {

    private SweetAlertDialog pDialogLoading;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private String mechanical_id,phone="",driver_id;
    private Button user_chat,call;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_mechanical_user, container, false);
        mechanical_id=getArguments().getString("id").toString();
        auth=FirebaseAuth.getInstance();
        driver_id=auth.getCurrentUser().getUid().toString();
        sweetalert();
        user_chat_method(v);
        get_data();
        mack_call( v);
        return v;
    }

    private void mack_call(View v) {
        call=v.findViewById(R.id.user_call_mechanicl);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     call(phone);
                     check_if_ther();
            }
        });
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
    private void check_if_ther()
    {
        database.collection("mechanical_divers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    boolean found = true;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id=document.get("driver_id").toString();
                        if (driver_id.equals(id))
                        {
                            found=false;
                            break;
                        }
                    }
                    if (found)
                    {
                        add_to_database();
                    }
                }
            }
        });
    }
    private void add_to_database()
    {
        String id= UUID.randomUUID().toString();
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("driver_id",driver_id);
        map.put("mechanical_id",mechanical_id);
        map.put("date",get_date());

        database.collection("mechanical_divers").document(id).set(map);
    }
    private void user_chat_method(View v)
    {
        user_chat=v.findViewById(R.id.user_chat_mechanicl);
        user_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_if_ther();
                chating t=new chating();
                Bundle b=new Bundle();
                b.putString("id",mechanical_id);
                b.putString("type","User");
                t.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_mechanical,t).addToBackStack(null).commitAllowingStateLoss();
            }
        });
    }
    private void sweetalert()
    {
        //loading

        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setCancelable(false);
        pDialogLoading.setTitleText("loading");
        pDialogLoading.show();
    }
    private void get_data()
    {
        database=FirebaseFirestore.getInstance();
        database.collection("Mechanical").document(mechanical_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().exists())
                    {
                        phone=task.getResult().get("phone").toString();
                        pDialogLoading.dismiss();
                    }
                }
            }
        });
    }
    private String get_date()
    {
        return  new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }
}