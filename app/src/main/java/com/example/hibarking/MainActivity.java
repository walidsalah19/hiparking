package com.example.hibarking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.hibarking.user_acess.login;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase_tool_intialize();
        check_user_acess();
        start_google_maps();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //
        // +check_user_acess();
    }

    private void firebase_tool_intialize()
    {
        databaseReference=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();


    }
    private void check_user_acess()
    {
        firebaseUser=auth.getCurrentUser();
        if(firebaseUser==null)
        {
            startActivity(new Intent(this, login.class));
        }
    }
    private void start_google_maps()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_framelayout,new MapFragment()).commit();

    }
}