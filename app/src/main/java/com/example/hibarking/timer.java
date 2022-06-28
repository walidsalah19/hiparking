package com.example.hibarking;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

public class timer {
    MainActivity main;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String user_id;
    TextView timer;
    CountDownTimer countDownTimer;
    long milliseconds , timeLeftInMilli;
    boolean timerunning;

    public timer(MainActivity main,TextView timer) {
        this.main = main;
        this.timer =timer;
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
