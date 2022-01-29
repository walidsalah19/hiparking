package com.example.hibarking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hibarking.bottom_sheet_package.booking_fragment;
import com.example.hibarking.chating.chating;
import com.example.hibarking.google_map.MapsFragment;
import com.example.hibarking.user_acess.login;
import com.example.hibarking.user_mechanical.mechanical_user;
import com.google.android.material.navigation.NavigationView;
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
    private View bottomsheet;
    private Button booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolpar_intialize();
        firebase_tool_intialize();
        intialization_tool();
        //check_user_acess();
        start_google_maps();
        navigation_items();
        booking_buttom_method();

    }

    @Override
    protected void onStart() {
        super.onStart();

       // check_user_acess();
    }

    private void toolpar_intialize() {
        toolbar = findViewById(R.id.appbar_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drowerlayout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.close, R.string.open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void firebase_tool_intialize() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


    }
  private void intialization_tool()
  {
     bottomsheet =(View ) findViewById(R.id.bottomsheet);
     booking=(Button) findViewById(R.id.user_main_booking);
  }
    private void check_user_acess() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, login.class));
        }
    }

    private void start_google_maps() {

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_framelayout, new MapsFragment()).commit();

    }

    private void navigation_items() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        if(item.getItemId()==R.id.navigation_menu_setting) {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.navigation_menu_contact) {
                    replace_fragment(new chating());
         }
        else if(item.getItemId()==R.id.navigation_menu_Emergency) {
            Toast.makeText(this, "emergency", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()==R.id.navigation_menu_mechanical) {
            replace_fragment(new mechanical_user());
        }
        else if(item.getItemId()==R.id.navigation_menu_profile) {
            Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
            bottomsheet.setVisibility(View.VISIBLE);
        }
        else if(item.getItemId()==R.id.navigation_logout) {
                 auth.signOut();
                 startActivity(new Intent(MainActivity.this,login.class));

        }
    }
    private void booking_buttom_method()
    {

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        replace_fragment(new booking_fragment());
            }
        });
    }
    private void replace_fragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,fragment).addToBackStack(null).commitAllowingStateLoss();
        bottomsheet.setVisibility(View.INVISIBLE);

    }
}