package com.example.hibarking.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.hibarking.MainActivity;
import com.example.hibarking.R;
import com.example.hibarking.SharedPref;
import com.example.hibarking.driver.profile.ProfileFragment;
import com.example.hibarking.garage_manager.garage_manager_profile;
import com.example.hibarking.garage_manager.main_garage_manager;
import com.example.hibarking.mechanical.Fragments.Mechanical_Profile;
import com.example.hibarking.mechanical.main_mechanical;
import com.example.hibarking.user_acess.forget_password;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingFragment extends Fragment {

    SwitchCompat switchCompat;
    private TextView forget_text,edit_text, language;
    private String type;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_setting, container, false);
        type=getArguments().getString("type").toString();
        darkmode(v);
        forgit_password(v);
         language(v);
         edit_profile(v);
        return v;
    }
    private void language(View v)
    {
        language=v.findViewById(R.id.change_language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_language();
            }
        });
    }
    private  void change_language()
    {
        SweetAlertDialog dialog=   new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog .setTitleText(getString(R.string.change_language));
        dialog  .setCustomImage(R.drawable.ic_baseline_language_24);
        dialog .setConfirmText(getString(R.string.arabic));
        dialog  .getProgressHelper().setBarColor(Color.RED);
        dialog   .setConfirmClickListener(nDialog -> {

            change_local_language( "ar");
            nDialog.dismiss();

        }).setCancelText(getString(R.string.english))
                .setCancelClickListener(sweetAlertDialog -> {

                    change_local_language("en");
                    sweetAlertDialog.dismiss();
                })
                .show();
    }
    private void change_local_language(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getActivity().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        SharedPref s=new SharedPref(getActivity());
        s.setlanguage(lang);
        reset();
    }
    private void forgit_password(View v)
    {
        forget_text=v.findViewById(R.id.change_password);
        forget_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(getActivity(), forget_password.class);
                startActivity(notificationIntent);
            }
        });
    }
    private void darkmode(View v)
    {
        SharedPref s=new SharedPref(getActivity());
        switchCompat =(SwitchCompat) v.findViewById(R.id.check1);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            switchCompat.setChecked(true);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    s.setThemeMode(true);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    s.setThemeMode(false);
                }
                reset();
            }
        });

    }
    private void edit_profile(View v)
    {
        edit_text=v.findViewById(R.id.edit_profile_setting);
        edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment notificationIntent;
                if (type.equals("user")) {
                   notificationIntent=new ProfileFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, notificationIntent).addToBackStack(null).commitAllowingStateLoss();
                }
                else if (type.equals("manager")) {
                    notificationIntent=new Mechanical_Profile();
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.machanical_framelayout,notificationIntent).addToBackStack(null).commitAllowingStateLoss();
                }
                else if (type.equals("mechanical")) {
                    notificationIntent=new garage_manager_profile();
                   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.grarage_manager_frameLayout,notificationIntent).addToBackStack(null).commitAllowingStateLoss();
                }

            }
        });
    }
    public void reset(){
        if (type.equals("user")) {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
        else if (type.equals("manager")) {
            startActivity(new Intent(getContext(), main_garage_manager.class));
        }
        else if (type.equals("mechanical")) {
            startActivity(new Intent(getContext(), main_mechanical.class));
        }

    }
}