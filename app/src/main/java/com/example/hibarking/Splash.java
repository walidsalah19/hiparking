package com.example.hibarking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.example.hibarking.garage_manager.main_garage_manager;
import com.example.hibarking.user_acess.login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Splash extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        language();
        darkmode();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (firebaseUser == null) {
                    startActivity(new Intent(Splash.this, login.class));
                }
                else {
                    startActivity(new Intent(Splash.this,MainActivity.class));

                }


                Splash.this.finish();
            }
        }, 4000);
    }
    private void darkmode()
    {
        SharedPref s=new SharedPref(Splash.this);
        boolean theme= s.loadThemeMode();
        if (theme)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void language()
    {
        SharedPref s=new SharedPref(Splash.this);
        String currentLang= s.loadlanguage();
        if (TextUtils.isEmpty(currentLang))
        {

        }
        else  if (currentLang.equals("ar"))
        {
            change_local_language("ar");
        }
        else if (currentLang.equals("en"))
        {
            change_local_language("en");
        }
    }
    private void change_local_language(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
