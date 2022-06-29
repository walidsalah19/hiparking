package com.example.hibarking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hibarking.SendNotificationPack.APIService;
import com.example.hibarking.SendNotificationPack.Data;
import com.example.hibarking.SendNotificationPack.MyResponse;
import com.example.hibarking.SendNotificationPack.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class timer {
    MainActivity main;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String user_id;
    TextView timer;
    CountDownTimer countDownTimer;
    long milliseconds , timeLeftInMilli;
    boolean timerunning;
    private FirebaseFirestore database;
    private APIService apiService;

    public timer(MainActivity main,TextView timer) {
        this.main = main;
        this.timer =timer;
    }

    public void get_timer_data()
    {
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
        final DocumentReference docRef = db.collection("booking").document(user_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    start_timer();
                    timer.setText( snapshot.getString("arrival_time").toString());
                    start_timer();

                } else {
                }
            }
        });
    }
    void start_timer()
    {
        if (timerunning)
        {
            stoptimer();
        }
        else {
            starttimer();
        }
    }

    private void starttimer() {
        countDownTimer=new CountDownTimer(milliseconds,1000) {
            @Override
            public void onTick(long l) {
              milliseconds=l;
              updatetimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerunning=true;
    }

    private void updatetimer() {
        int minute=(int)milliseconds/ 60000;
        int seconds=(int) milliseconds % 60000 / 1000;
        String timetext= minute +"";
        timetext+=":";
        if (seconds<10)
        {
            timetext+="0";
        }
        timetext+=seconds;
        timer.setText(timetext);
    }

    private void stoptimer() {
        countDownTimer.cancel();
        timerunning=false;
    }

}
