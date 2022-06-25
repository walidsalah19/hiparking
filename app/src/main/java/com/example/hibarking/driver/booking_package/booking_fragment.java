package com.example.hibarking.driver.booking_package;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class booking_fragment extends Fragment {


    private TextView text_date,text_time,duration,name;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    final Calendar myCalendar = Calendar.getInstance();
    private RadioButton permenent,nonpermenent;
    private LinearLayout duration_layout;
    private Button booking;
    private  String garage_id;
    private  FirebaseAuth auth;
    private DatabaseReference database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v= inflater.inflate(R.layout.fragment_booking_fragment, container, false);
        text_date_method(v);
        text_time_method(v);
        catagories(v);
        booking_garage(v);
         return v;
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
                  check_date();
              }
          });
    }

    private void check_date() {
        if( name.getText().equals(" "))
        {
            Toast.makeText(getActivity(), "please enter you'r name", Toast.LENGTH_SHORT).show();
        }
        else if( text_time.getText().equals("time"))
        {
            Toast.makeText(getActivity(), "please choose time", Toast.LENGTH_SHORT).show();
        }
        else if( text_date.getText().equals("Date"))
        {
            Toast.makeText(getActivity(), "please choose date", Toast.LENGTH_SHORT).show();
        }
        else if(permenent.isChecked())
        {
            send_to_database("permanent");
        }
        else if(nonpermenent.isChecked())
        {
            if(duration.getText().equals(""))
            {
                Toast.makeText(getActivity(), "please enter duration", Toast.LENGTH_SHORT).show();
            }
            else
            {
                send_to_database(duration.getText().toString());
            }
        }
    }

    private void send_to_database(String p) {
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("booking");
        String userId=auth.getCurrentUser().getUid();
        HashMap<String, String> dataset=new HashMap<>();
        dataset.put("duration",p);
        dataset.put("id",userId);
        dataset.put("name",name.getText().toString());
        dataset.put("date",text_date.getText().toString());
        dataset.put("time",text_time.getText().toString());
        database.child(garage_id).child(userId).setValue(dataset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getActivity(), "successful booking", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "error occur", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
  /*  private boolean isMyServiceRunning() {
        // The ACTIVITY_SERVICE is needed to retrieve a
        // ActivityManager for interacting with the global system
        // It has a constant String value "activity".
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        // A loop is needed to get Service information that
        // are currently running in the System.
        // So ActivityManager.RunningServiceInfo is used.
        // It helps to retrieve a
        // particular service information, here its this service.
        // getRunningServices() method returns a list of the
        // services that are currently running
        // and MAX_VALUE is 2147483647. So at most this many services
        // can be returned by this method.
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            // If this service is found as a running,
            // it will return true or else false.
            if (floating_window.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void requestOverlayDisplayPermission() {
        // An AlertDialog is created
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // This dialog can be closed, just by taping
        // anywhere outside the dialog-box
        builder.setCancelable(true);

        // The title of the Dialog-box is set
        builder.setTitle("Screen Overlay Permission Needed");

        // The message of the Dialog-box is set
        builder.setMessage("Enable 'Display over other apps' from System Settings.");

        // The event of the Positive-Button is set
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The app will redirect to the 'Display over other apps' in Settings.
                // This is an Implicit Intent. This is needed when any Action is needed
                // to perform, here it is
                // redirecting to an other app(Settings).
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + new Settings()));

                // This method will start the intent. It takes two parameter, one is the Intent and the other is
                // an requestCode Integer. Here it is -1.
                startActivityForResult(intent, -1);
            }
        });
        dialog = builder.create();
        // The Dialog will
        // show in the screen
        dialog.show();
    }
    private boolean checkOverlayDisplayPermission() {
        // Android Version is lesser than Marshmallow or
        // the API is lesser than 23
        // doesn't need 'Display over other apps' permission enabling.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // If 'Display over other apps' is not enabled
            // it will return false or else true
            if (!Settings.canDrawOverlays(getActivity())) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }*/
}
