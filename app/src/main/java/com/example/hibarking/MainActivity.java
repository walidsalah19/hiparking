package com.example.hibarking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.hibarking.Fragments.AddGarageManData;
import com.example.hibarking.Fragments.ProfileFragment;
import com.example.hibarking.google_map.MapsFragment;
import com.example.hibarking.user_acess.login;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
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
    ImageButton imageButton;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create
        toolpar_intialize();
        firebase_tool_intialize();
        check_user_acess();
        start_google_maps();

        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        Menu menuNav = navigationView.getMenu();
        MenuItem logout=menuNav.findItem(R.id.navigation_logout);
        logout.setChecked(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        MenuItem profile = menuNav.findItem(R.id.navigation_menu_profile);
        profile.setChecked(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, new ProfileFragment()).addToBackStack(null).commit();
                //getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,new AddGarageManData()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        MenuItem parking = menuNav.findItem(R.id.parking);
        parking.setChecked(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                start_google_maps();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
        //getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout, new MapsFragment()).commit();

        getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,new AddGarageManData()).commit();

    }

}