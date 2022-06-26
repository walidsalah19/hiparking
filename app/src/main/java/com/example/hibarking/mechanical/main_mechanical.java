package com.example.hibarking.mechanical;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.hibarking.Fragments.ContactFragment;
import com.example.hibarking.Fragments.EmergancyFragment;
import com.example.hibarking.Fragments.SettingFragment;
import com.example.hibarking.R;
import com.example.hibarking.mechanical.Fragments.Mechanical_Profile;
import com.example.hibarking.mechanical.Fragments.ViewDrivers;
import com.example.hibarking.user_acess.login;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class main_mechanical extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mechanical);

            toolpar_intialize();
            navigation_items();
            StartDrivers();
    }
    private void toolpar_intialize() {
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.mechanical_appbar_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.mechanical_drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,  drawerLayout, toolbar, R.string.close, R.string.open);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void navigation_items() {
        navigationView = (NavigationView) findViewById(R.id.m_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigation_item_click(item);
                return false;
            }
        });
    }
    private void navigation_item_click(MenuItem item)
    {
        if(item.getItemId()== R.id.m_navigation_customer) {
           replace_fragment(new ViewDrivers());
        }
        else if(item.getItemId()== R.id.m_navigation_profile) {
            replace_fragment(new Mechanical_Profile());
        }
        else if (item.getItemId() == R.id.m_navigation_rating) {
            //replace_fragment(new MapsFragment());
        }
        else if(item.getItemId()== R.id.m_navigation_emergency) {
            replace_fragment(new EmergancyFragment());
        }
        else if(item.getItemId()== R.id.m_navigation_contact) {
            replace_fragment(new ContactFragment());
        }
        else if(item.getItemId()== R.id.m_navigation_setting) {
            replace_fragment(new SettingFragment());
        }
        else if(item.getItemId()== R.id.m_navigation_logout) {
            auth.signOut();
            startActivity(new Intent(main_mechanical.this, login.class));

        }
    }
    private void replace_fragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.machanical_framelayout,fragment).addToBackStack(null).commitAllowingStateLoss();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void StartDrivers(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.machanical_framelayout, new ViewDrivers()).commit();
    }

}