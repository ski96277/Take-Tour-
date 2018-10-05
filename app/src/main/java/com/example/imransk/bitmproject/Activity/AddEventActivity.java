package com.example.imransk.bitmproject.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.imransk.bitmproject.ModelClass.Add_Event;
import com.example.imransk.bitmproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user_ID;

    EditText place_name_ET;
    EditText start_Journey_Date_ET;
    EditText end_Journey_Date_ET;
    EditText journey_budget_ET;

    Button submit_btn;

    String place;
    String start_Date;
    String end_Date;
    String budget;
    String event_Create_Date;
    String event_type;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener start_Journey_Date;
    DatePickerDialog.OnDateSetListener end_Journey_Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        place_name_ET = findViewById(R.id.place_name_ET);
        start_Journey_Date_ET = findViewById(R.id.start_jurney_Date_ET);
        end_Journey_Date_ET = findViewById(R.id.end_journey_date_ET);
        journey_budget_ET = findViewById(R.id.budjet_journey);

        //set Date From Date picker
        PickDateForStartJourney();
        PickDateForEndJourney();

        start_Journey_Date_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddEventActivity.this, start_Journey_Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        end_Journey_Date_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddEventActivity.this, end_Journey_Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//End date picker work
        submit_btn = findViewById(R.id.submit_button);
        submit_btn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user_ID = firebaseUser.getUid();


    }

    private void PickDateForEndJourney() {
        end_Journey_Date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                end_Journey_Date_ET.setText(sdf.format(myCalendar.getTime()));

            }

        };
    }

    private void PickDateForStartJourney() {

        start_Journey_Date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                start_Journey_Date_ET.setText(sdf.format(myCalendar.getTime()));

            }

        };


    }

    @Override
    public void onClick(View view) {

        //get Text From EditText Field
        place = place_name_ET.getText().toString();
        start_Date = start_Journey_Date_ET.getText().toString();
        end_Date = end_Journey_Date_ET.getText().toString();
        budget = journey_budget_ET.getText().toString();


        if (TextUtils.isEmpty(place)) {
            place_name_ET.setError("Place Name");
            place_name_ET.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(start_Date)) {
            start_Journey_Date_ET.setError("Place Name");
            start_Journey_Date_ET.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(end_Date)) {
            end_Journey_Date_ET.setError("Place Name");
            end_Journey_Date_ET.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(budget)) {
            journey_budget_ET.setError("Place Name");
            journey_budget_ET.requestFocus();
            return;
        }


        event_Create_Date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        event_type= "information";
        Add_Event add_event = new Add_Event(place, start_Date, end_Date, budget, event_Create_Date,event_type);

        databaseReference.child("Event").child(user_ID).child(event_Create_Date).child("details").child(event_type).setValue(add_event);

        place_name_ET.setText("");
        start_Journey_Date_ET.setText("");
        end_Journey_Date_ET.setText("");
        journey_budget_ET.setText("");
        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }
}
