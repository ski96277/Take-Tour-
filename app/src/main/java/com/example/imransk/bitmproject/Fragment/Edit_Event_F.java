package com.example.imransk.bitmproject.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imransk.bitmproject.Activity.AddEventActivity;
import com.example.imransk.bitmproject.Activity.MainActivity;
import com.example.imransk.bitmproject.ModelClass.Add_Event;
import com.example.imransk.bitmproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class Edit_Event_F extends Fragment{

    Bundle bundle;


    String place="";
    String budget="";
    String st_Date="";
    String end_Date="";
    String event_create_date="";
    String event_type="";

    String place_new="";
    String budget_new="";
    String st_Date_new="";
    String end_Date_new="";
    
    
    EditText place_name_ET;
    EditText start_Date_ET;
    EditText end_date_ET;
    EditText budget_ET;
    
    Button submit_Edit_Btn;


    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String user_ID="";

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener start_Journey_Date;
    DatePickerDialog.OnDateSetListener end_Journey_Date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Edit Event");
        return inflater.inflate(R.layout.edit_event_f,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


         databaseReference=FirebaseDatabase.getInstance().getReference();
         firebaseAuth=FirebaseAuth.getInstance();
         firebaseUser=firebaseAuth.getCurrentUser();
         user_ID=firebaseUser.getUid();


//get value from bundle
        bundle=getArguments();
        place=bundle.getString("place");
        budget=bundle.getString("budget");
        st_Date=bundle.getString("st_Date");
        end_Date=bundle.getString("end_Date");
        event_create_date=bundle.getString("event_create_date");
        event_type=bundle.getString("event_type");
        Log.e("... ... ", "...event type ... : "+event_type );

//init the Weight
        place_name_ET=view.findViewById(R.id.edit_place_name_ET);
        start_Date_ET=view.findViewById(R.id.edit_start_jurney_Date_ET);
        end_date_ET=view.findViewById(R.id.edit_end_journey_date_ET);
        budget_ET=view.findViewById(R.id.edit_budjet_journey);
        place_name_ET.setText(place);

//set Bundle in Edit Text
        start_Date_ET.setText(st_Date);
        end_date_ET.setText(end_Date);
        budget_ET.setText(budget);



 //Select new start date
        PickDateForStartJourney();
        PickDateForEndJourney();
        start_Date_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), start_Journey_Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//select new end date
        end_date_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), end_Journey_Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        
        
        submit_Edit_Btn=view.findViewById(R.id.submit_button_edit);
        submit_Edit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Update Text from Edit Text
                place_new=place_name_ET.getText().toString();
                st_Date_new=start_Date_ET.getText().toString();
                end_Date_new=end_date_ET.getText().toString();
                budget_new=budget_ET.getText().toString();

                Log.e("... ... .. . ", "event type: "+event_type);
                Add_Event add_event=new Add_Event(place_new,st_Date_new,end_Date_new,budget_new,event_create_date,event_type);

                databaseReference.child("Event").child(user_ID).child(event_create_date).child("details").child(event_type).setValue(add_event);
                Toast.makeText(getContext(), "Edit Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });



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
                end_date_ET.setText(sdf.format(myCalendar.getTime()));

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
                start_Date_ET.setText(sdf.format(myCalendar.getTime()));

            }

        };


    }
}
