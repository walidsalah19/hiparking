package com.example.hibarking.driver.booking_package;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hibarking.R;
import com.example.hibarking.SendNotificationPack.APIService;
import com.example.hibarking.SendNotificationPack.Client;
import com.example.hibarking.SendNotificationPack.Data;
import com.example.hibarking.SendNotificationPack.MyResponse;
import com.example.hibarking.SendNotificationPack.NotificationSender;
import com.example.hibarking.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class booking_fragment extends Fragment {


    private TextView text_date,text_time,duration,name;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    final Calendar myCalendar = Calendar.getInstance();
    private RadioButton permenent,nonpermenent;
    private LinearLayout duration_layout;
    private Button booking;
    private  String garage_id,userId;
    private  FirebaseAuth auth;
    private FirebaseFirestore database;
    private SweetAlertDialog pDialogLoading,pDialogSuccess,pDialogerror;

    private APIService apiService;
    SharedPref sharedPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(getActivity());
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v= inflater.inflate(R.layout.fragment_booking_fragment, container, false);
        auth=FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
         sweetalert();
        text_date_method(v);
        text_time_method(v);
        catagories(v);
        booking_garage(v);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
         return v;
    }
    private void sweetalert()
    {
        //loading

        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setCancelable(false);

        //error
        pDialogerror= new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        pDialogerror.setConfirmText(getString(R.string.dialog_ok));
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(true);

        //Success
        pDialogSuccess= new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.setConfirmText(getString(R.string.dialog_ok));
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(true);
    }
    private void catagories(View v)
    {
        permenent=v.findViewById(R.id.permenent_redio);
        nonpermenent=v.findViewById(R.id.nonpermenent_redio);
        duration_layout=v.findViewById(R.id.duration_layout);
        duration=v.findViewById(R.id.duration_booking_driver);
        name=v.findViewById(R.id.booking_driver_name);
        permenent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                duration_layout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                duration_layout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                duration_layout.requestLayout();
                duration_layout.setVisibility(View.VISIBLE);
            }
        });
       nonpermenent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               duration_layout.getLayoutParams().height = 0;
               duration_layout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
               duration_layout.requestLayout();
               duration_layout.setVisibility(View.INVISIBLE);

           }
       });

    }
    private void text_date_method(View v)
    {
        text_date=v.findViewById(R.id.date_booking_driver);
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog_method();
            }
        });
    }
    private void datePickerDialog_method()
    {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                text_date.setText(year + "-" + monthOfYear+1 + "-" + dayOfMonth);
            }
        };
        datePickerDialog=  new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void text_time_method(View v)
    {
        text_time=v.findViewById(R.id.time_booking_driver);
        text_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog_method();
            }
        });
    }
    private void timePickerDialog_method()
    {

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                text_time.setText(hourOfDay + ":" + minute );

            }
        };
        timePickerDialog = new TimePickerDialog(getActivity(),
                timeSetListener, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),false);

        timePickerDialog.show();
    }
    private void booking_garage(View v)
    {
          Bundle b=getArguments();
          garage_id=b.getString("id");
          booking=v.findViewById(R.id.drivre_booking_garage);
          booking.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  check_if_exixt();
              }
          });
    }

   private void check_if_exixt()
   {
       userId=auth.getCurrentUser().getUid();
       database.collection("booking").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   boolean found=false;
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       String id=document.get("id").toString();
                       if (userId.equals(id))
                       {
                           pDialogerror.setTitleText(getString(R.string.old_booking));
                           pDialogerror.show();
                           garage_id=document.get("garage_id").toString();
                           found=true;
                           break;
                       }
                   }
                   if (!found)
                   {
                       check_date();
                   }
               }
           }
       });
   }
    private void check_date() {
        if( name.getText().equals(" "))
        {
            Toast.makeText(getActivity(), R.string.enter_your_name, Toast.LENGTH_SHORT).show();
        }
        else if( text_time.getText().equals("time"))
        {
            Toast.makeText(getActivity(), R.string.choose_time, Toast.LENGTH_SHORT).show();
        }
        else if( text_date.getText().equals("Date"))
        {
            Toast.makeText(getActivity(), R.string.choose_date, Toast.LENGTH_SHORT).show();
        }
        else if(permenent.isChecked())
        {
            send_to_database("permanent");
        }
        else if(nonpermenent.isChecked())
        {
            if(duration.getText().equals(""))
            {
                Toast.makeText(getActivity(), R.string.enter_duration, Toast.LENGTH_SHORT).show();
            }
            else
            {
                send_to_database(duration.getText().toString());
            }
        }
    }
    private void send_to_database(String p) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        long minutes =Long.parseLong(str.substring(3,5));
        long second =Long.parseLong(str.substring(6,8));
        long hours   = Long.parseLong(str.substring(0,2));
        long currentTime =((hours *60*60+(minutes)*60)+second)*1000;


        HashMap<String, String> dataset=new HashMap<>();
        dataset.put("duration",p);
        dataset.put("id",userId);
        dataset.put("name",name.getText().toString());
        dataset.put("date",text_date.getText().toString());
        dataset.put("time",text_time.getText().toString());
        dataset.put("garage_id",garage_id);
        dataset.put("status","not arrived");
        dataset.put("arrival_time", String.valueOf(currentTime+60000));
        database.collection("booking").document(userId).set(dataset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    getToken(userId,"booking","you booking");
                    pDialogSuccess.setTitleText(getString(R.string.booking));
                    pDialogSuccess.show();
                }
                else
                {
                    pDialogerror.setTitleText(getString(R.string.error));
                    pDialogerror.show();
                }
            }
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
                        Toast.makeText(getActivity(), "Failed ", Toast.LENGTH_LONG);
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
