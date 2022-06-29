package com.example.hibarking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.example.hibarking.Fragments.ContactFragment;
import com.example.hibarking.Fragments.EmergancyFragment;
import com.example.hibarking.SendNotificationPack.APIService;
import com.example.hibarking.SendNotificationPack.Client;
import com.example.hibarking.SendNotificationPack.Data;
import com.example.hibarking.SendNotificationPack.MyResponse;
import com.example.hibarking.SendNotificationPack.NotificationSender;
import com.example.hibarking.driver.profile.ProfileFragment;
import com.example.hibarking.Fragments.SettingFragment;
import com.example.hibarking.driver.qr_scanner.scanner;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;

    CountDownTimer countDownTimer;
    long milliseconds , timeLeftInMilli;
    FirebaseFirestore db;
    String user_id;

    private FirebaseFirestore database;

    private DrawerLayout drawerLayout;
    TextView headerName ,timer;
    NavigationView navigationView;
    CircleImageView circleImageView;
    SharedPref sharedPref;
    String userToken;
    Map<String ,String> profile=new HashMap<>();
    private APIService apiService;

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
        toolpar_intialize();
        firebase_tool_intialize();
        start_google_maps("garage");
        navigation_items();
        timer=(TextView) findViewById(R.id.timer);
        get_timer_data();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }
    public void get_timer_data() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        long minutes =Long.parseLong(str.substring(3,5));
        long hours   = Long.parseLong(str.substring(0,2));
        long currentTime =(hours *60*60+minutes*60)*1000;


        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
        timer.setText("");
        final DocumentReference docRef = db.collection("booking").document(user_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    timer.setText("");
                    if(snapshot.getString("id").toString().equals(user_id) && snapshot.getString("status").toString().equals("not arrived")){

                        long timeToArrive = Long.parseLong(snapshot.getString("arrival_time").toString());
                        Toast.makeText(MainActivity.this, "to arrive : "+timeToArrive, Toast.LENGTH_SHORT).show();
                        timeLeftInMilli = timeToArrive - currentTime;
                        StartCountDownTimer();

                    }else if(snapshot.getString("arrival_time").toString().equals("0")){
                        countDownTimer.cancel();
                    }
                }
            }
        });

    }
    public void StartCountDownTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilli,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilli = l;
                int seconds = (int) (timeLeftInMilli / 1000) % 60 ;
                int minutes = (int) ((timeLeftInMilli / (1000*60)) % 60);
                int hours   = (int) ((timeLeftInMilli / (1000*60*60)) % 24);
                String timeRemain = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
                timer.setText(String.format(getString(R.string.arrival), timeRemain));
            }

            @Override
            public void onFinish() {
                timer.setText("");
                cancelBooking();
            }
        }.start();

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
                            profile.put("name",task.getResult().getString("name"));
                            profile.put("id",task.getResult().getString("id"));
                            profile.put("email",task.getResult().getString("email"));
                            profile.put("license",task.getResult().getString("license"));
                            profile.put("phone",task.getResult().getString("phone"));
                            profile.put("uid",task.getResult().getString("uid"));
                            profile.put("image",task.getResult().getString("image"));
                            profile.put("date",task.getResult().getString("date"));
                            if (task.getResult().contains("uri")) {
                                profile.put("uri",task.getResult().getString("uri"));
                                String urls = task.getResult().getString("uri");
                                Picasso.get().load(urls).into(circleImageView);
                            }
                            headerName.setText(names);
                            updateToken();

                        } else {

                            Intent intent = new Intent(MainActivity.this, create_account.class);
                            intent.putExtra("email", user.getEmail().toString());

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
        toolbar = findViewById(R.id.appbar_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drowerlayout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,  drawerLayout, toolbar, R.string.close, R.string.open);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void firebase_tool_intialize() {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


    }

    private void check_user_acess() {
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, login.class));
        }
    }


    private void start_google_maps(String type) {
       Bundle b=new Bundle();
       b.putString("type",type);
       MapsFragment m=new MapsFragment();
       m.setArguments(b);
       getSupportFragmentManager().beginTransaction()
                .add(R.id.main_framelayout, m).commit();

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
            start_google_maps("garage");
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(item.getItemId()==R.id.scann) {
            replace_fragment(new scanner());
        }
        else if(item.getItemId()==R.id.navigation_menu_profile) {
            replace_fragment(new ProfileFragment());
        }
        else if (item.getItemId() == R.id.navigation_menu_mechanical) {
            start_google_maps("mechanical");
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(item.getItemId()==R.id.navigation_menu_Emergency) {
            replace_fragment(new EmergancyFragment());
        }
        else if(item.getItemId()==R.id.navigation_menu_contact) {
             replace_fragment(new ContactFragment());
        }
        else if(item.getItemId()==R.id.navigation_menu_setting) {
            Bundle b=new Bundle();
            b.putString("type","user");
            SettingFragment f=new SettingFragment();
            f.setArguments(b);
            replace_fragment(f);
        }
        else if(item.getItemId()==R.id.navigation_logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this,login.class));

        }
    }

    private void replace_fragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).addToBackStack(null).commitAllowingStateLoss();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void cancelBooking() {

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid().toString();
        final DocumentReference docRef = db.collection("booking").document(user_id);
        docRef.delete();
        getToken(user_id,"Booking","we canceled your booking");
    }
    private void updateToken()
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    userToken = task.getResult();
                    System.out.println(userToken);
                    profile.put("token",userToken);
                    database.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(profile);
                   // FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(userToken);
                });
    }
    private void getToken(String userID, String title, String message) {
        database.collection("User").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    if (task.getResult().exists())
                    {
                        String usertoken = task.getResult().get("token").toString();
                        sendNotifications(usertoken, title, message);
                    }

                }
            }
        });
    }
    private void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null && response.body().success != 1) {
                        Toast.makeText(MainActivity.this, "Failed ", Toast.LENGTH_LONG);
                    } else {
                        Log.e("success", response.code() + " success ya Fashel " + response.body().success + " Token " + usertoken);
                    }
                } else {
                    Log.e("send Notifications", "Failed ya Fashel: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
            }
        });
    }
}