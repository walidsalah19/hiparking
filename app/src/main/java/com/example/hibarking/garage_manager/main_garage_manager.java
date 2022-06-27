package com.example.hibarking.garage_manager;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.hibarking.R;
import com.example.hibarking.Fragments.ContactFragment;
import com.example.hibarking.Fragments.EmergancyFragment;
import com.example.hibarking.Fragments.SettingFragment;
import com.example.hibarking.SharedPref;
import com.example.hibarking.driver.user_account.create_account;
import com.example.hibarking.user_acess.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class main_garage_manager extends AppCompatActivity {
    private String user_id;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private  TextView headerName;
    private CircleImageView circleImageView;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    NavigationView navigationView;
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
        setContentView(R.layout.activity_main_garage);
        firebase_describtion();
        toolpar_intialize();
        navigation_items();
    }
    @Override
    protected void onStart() {
        super.onStart();
        check_user_acess();

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
            circleImageView =(CircleImageView) header.findViewById(R.id.circl_imag_navigation_head);
            reference = firestore.collection("garage_manager").document(currentUserId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        if (task.getResult().exists()) {

                            String names = task.getResult().getString("username");
                            if (task.getResult().contains("image")) {
                                String urls = task.getResult().getString("image");
                                Picasso.get().load(urls).into(circleImageView);
                            }
                            headerName.setText(names);


                        } else {

                            Intent intent = new Intent(main_garage_manager.this, create_account.class);
                            intent.putExtra("email", user.getEmail().toString());

                            startActivity(intent);
                        }
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(main_garage_manager.this, "" + nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private void firebase_describtion()
    {
        database=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
    }
    private void toolpar_intialize() {
        toolbar = findViewById(R.id.garage_manager_appbar_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.garage_manager_drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,  drawerLayout, toolbar, R.string.close, R.string.open);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void navigation_items() {
        navigationView = (NavigationView) findViewById(R.id.g_nav_view);
        move_fragment(new display_garages());
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

      if(item.getItemId()==R.id.g_navigation_logout) {
            auth.signOut();
            startActivity(new Intent(main_garage_manager.this, login.class));

        }
      else if(item.getItemId()==R.id.g_navigation_home) {
          move_fragment(new display_garages());
        }
      else if(item.getItemId()==R.id.g_navigation_profile) {
          move_fragment(new garage_manager_profile());
      }
      else if(item.getItemId()==R.id.g_navigation_emergency) {
          move_fragment(new EmergancyFragment());
      }
      else if(item.getItemId()==R.id.g_navigation_contact) {
          move_fragment(new ContactFragment());
      }
      else if(item.getItemId()==R.id.g_navigation_setting) {
          move_fragment(new SettingFragment());
      }

    }
    private void move_fragment(Fragment Fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.grarage_manager_frameLayout,Fragment).addToBackStack(null).commitAllowingStateLoss();
    }
    private void check_user_acess() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, login.class));
        }
    }
}
