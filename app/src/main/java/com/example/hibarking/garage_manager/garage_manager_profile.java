package com.example.hibarking.garage_manager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hibarking.R;

import com.example.hibarking.driver.user_account.create_account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class garage_manager_profile extends Fragment {

    EditText name, id , email , phone;
    CircleImageView imageView;
    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization();
        getDateGarageManager();
    }
    private void initialization(){
        name = getActivity().findViewById(R.id.name_gar);
        id= getActivity().findViewById(R.id.id_gar);
        email = getActivity().findViewById(R.id.email_gar);
        phone = getActivity().findViewById(R.id.phone_gar);
        imageView = getActivity().findViewById(R.id.image_garage);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garage_manager_profile, container, false);
    }


    public void getDateGarageManager(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId= null;
        if (user != null) {
            currentUserId = user.getUid();
        }
        DocumentReference reference;
        FirebaseFirestore fireStore=FirebaseFirestore.getInstance();

        if (currentUserId != null) {
            reference = fireStore.collection("garage_manager").document(currentUserId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    try {
                        if (task.getResult().exists()) {
                            String names = task.getResult().getString("username");
                            String ids = task.getResult().getString("national_id");
                            String phones = task.getResult().getString("phone");
                            String urls;
                            if(task.getResult().contains("image")){
                                urls = task.getResult().getString("image");
                            }else {
                                urls="null";
                            }

                            String emails = task.getResult().getString("email");

                            if(!urls.equals("null"))
                                Picasso.get().load(urls).into(imageView);
                            else {
                                imageView.setImageResource(R.drawable.profile_image);
                            }
                            name.setText(names);
                            id.setText(ids);
                            email.setText(emails);
                            phone.setText(phones);


                        } else {
                            Intent intent = new Intent(getActivity(), AddGarageManData.class);
                            startActivity(intent);
                        }
                    } catch (NullPointerException nullPointerException) {
                        Toast.makeText(getActivity(), "" + nullPointerException.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            getActivity().finish();
            System.exit(0);
        }
    }

}

