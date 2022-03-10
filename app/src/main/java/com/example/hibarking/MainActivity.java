package com.example.hibarking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.driver.profile.Fragments.ContactFragment;
import com.example.hibarking.driver.profile.Fragments.EmergancyFragment;
import com.example.hibarking.driver.profile.ProfileFragment;
import com.example.hibarking.driver.profile.Fragments.SettingFragment;
import com.example.hibarking.driver.user_account.create_account;
import com.example.hibarking.driver.google_map.MapsFragment;
import com.example.hibarking.user_acess.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    ImageButton imageButton;
    TextView headerName;
    NavigationView navigationView;
    CircleImageView circleImageView;
    private DrawerLayout drawerLayout;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create
        toolpar_intialize();
        firebase_tool_intialize();
        start_google_maps();
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
            reference = firestore.collection("User").document(currentUserId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        if (task.getResult().exists()) {
                            String names = task.getResult().getString("name");
                            String urls = task.getResult().getString("uri");

                            Picasso.get().load(urls).into(circleImageView);
                            headerName.setText(names);


                        } else {
                            Intent intent = new Intent(MainActivity.this, create_account.class);
                            startActivity(intent);
                        }
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(MainActivity.this, "" + nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
       replace_fragment(new MapsFragment());
    }
    private void navigation_items() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        if(item.getItemId()==R.id.parking) {
            start_google_maps();
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(item.getItemId()==R.id.navigation_menu_profile) {
            replace_fragment(new ProfileFragment());
        }
        else if (item.getItemId() == R.id.navigation_menu_mechanical) {
            replace_fragment(new MapsFragment());
        }
        else if(item.getItemId()==R.id.navigation_menu_Emergency) {
            replace_fragment(new EmergancyFragment());
        }
        else if(item.getItemId()==R.id.navigation_menu_contact) {
             replace_fragment(new ContactFragment());
        }
        else if(item.getItemId()==R.id.navigation_menu_setting) {
            replace_fragment(new SettingFragment());
        }
        else if(item.getItemId()==R.id.navigation_logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this,login.class));

        }
    }

    private void replace_fragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,fragment).addToBackStack(null).commitAllowingStateLoss();
        drawerLayout.closeDrawer(GravityCompat.START);

    }

}