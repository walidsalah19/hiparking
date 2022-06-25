package com.example.hibarking.driver.user_account;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.hibarking.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_data_to_firebase {
    private DatabaseReference reference;
    private create_account_class create;
    private Context context;

    public add_data_to_firebase(create_account_class create, Context context) {
        this.create = create;
        this.context = context;
        put_data_in_firebase();
    }

    private void put_data_in_firebase()
    {
        reference= FirebaseDatabase.getInstance().getReference("user_data");
        reference.child(create.getUser_id()).setValue(create).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    got_to_main();
                }
            }
        });
    }

    private void got_to_main() {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
