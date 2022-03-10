package com.example.hibarking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    SharedPreferences preferences;
    Context context;
    public SharedPref(Context context){
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setThemeMode(boolean state){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("state",state);
        editor.apply();
    }

    public boolean loadThemeMode(){
        return preferences.getBoolean("state",false);
    }
}
