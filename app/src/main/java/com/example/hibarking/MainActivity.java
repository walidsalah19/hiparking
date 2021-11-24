package com.example.hibarking;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.example.hibarking.google_map.MapsFragment;
import com.example.hibarking.user_acess.login;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolpar_intialize();
        firebase_tool_intialize();
        check_user_acess();
        start_google_maps();
    }

    @Override
    protected void onStart() {
        super.onStart();
        check_user_acess();
    }
    private void toolpar_intialize() {
        toolbar=findViewById(R.id.appbar_main);
        drawerLayout=(DrawerLayout) findViewById(R.id.drowerlayout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.close,R.string.open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_framelayout, new MapsFragment()).commit();


    }
}