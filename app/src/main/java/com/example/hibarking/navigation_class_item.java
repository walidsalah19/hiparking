package com.example.hibarking;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.hibarking.user_acess.login;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class navigation_class_item {
     navigation_class_item(NavigationView navigationView, Context context)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        Menu menuNav = navigationView.getMenu();
        MenuItem logout=menuNav.findItem(R.id.navigation_logout);
        logout.setChecked(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                auth.signOut();
                context.startActivity(new Intent(context, login.class));
                return true;
            }
        });

    }
}
