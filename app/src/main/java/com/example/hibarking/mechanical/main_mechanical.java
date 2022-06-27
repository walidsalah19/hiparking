package com.example.hibarking.mechanical;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.hibarking.Fragments.ContactFragment;
import com.example.hibarking.Fragments.EmergancyFragment;
import com.example.hibarking.Fragments.SettingFragment;
import com.example.hibarking.MainActivity;
import com.example.hibarking.R;

import com.example.hibarking.driver.user_account.create_account;

import com.example.hibarking.SharedPref;

import com.example.hibarking.mechanical.Fragments.Mechanical_Profile;
import com.example.hibarking.mechanical.Fragments.ViewDrivers;
import com.example.hibarking.user_acess.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class main_mechanical extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    TextView headerName;
    CircleImageView  imageView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.activity_main_mechanical);


        toolpar_intialize();

        getData();

        navigation_items();

        StartDrivers();
    }
    private void toolpar_intialize() {
        navigationView = (NavigationView) findViewById(R.id.m_nav_view);
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
        else if(item.getItemId()== R.id.m_navigation_emergency) {
            replace_fragment(new EmergancyFragment());
        }
        else if(item.getItemId()== R.id.m_navigation_contact) {
            replace_fragment(new ContactFragment());
        }
        else if(item.getItemId()== R.id.m_navigation_setting) {
            Bundle b=new Bundle();
            b.putString("type","mechanical");
            SettingFragment f=new SettingFragment();
            f.setArguments(b);
            replace_fragment(f);
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

    private void getData(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId= null;
        if (user != null) {
            currentUserId = user.getUid();
        }
        DocumentReference reference;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();

        if (currentUserId != null) {
            View header = navigationView.getHeaderView(0);
            headerName = (TextView) header.findViewById(R.id.user_name);
            imageView =(CircleImageView) header.findViewById(R.id.circl_imag_navigation_head);
            reference = firestore.collection("Mechanical").document(currentUserId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        if (task.getResult().exists()) {
                            String names = task.getResult().getString("name");
                            String urls = task.getResult().getString("uri");

                            if(!urls.equals("null"))
                                Picasso.get().load(urls).into(imageView);
                            else {
                                imageView.setImageResource(R.drawable.profile_image);
                            }
                            headerName.setText(names);


                        } else {

                            Intent intent = new Intent(main_mechanical.this, create_account.class);
                            intent.putExtra("email", user.getEmail().toString());

                            startActivity(intent);
                        }
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(main_mechanical.this, "" + nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void check_user_acess() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, login.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        check_user_acess();
    }
}