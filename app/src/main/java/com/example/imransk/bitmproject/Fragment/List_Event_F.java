package com.example.imransk.bitmproject.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.Adapter.Event_List_Adapter;
import com.example.imransk.bitmproject.ModelClass.Add_Event;
import com.example.imransk.bitmproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by imran sk on 5/25/2018.
 */

public class List_Event_F extends Fragment {

    TextView add_event_TV;
    ListView add_event_list;
    private ArrayList<Add_Event> add_event_pojo = null;
    View context;

    //Alert Diolag
    TextView place_name_et;
    TextView start_journey_date_et;
    TextView end_journey_date_et;
    TextView journey_budget_ET;


    String place;
    String start_Date;
    String end_Date;
    String budget;
    String event_Create_Date;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user_ID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Event List");
        return inflater.inflate(R.layout.list_event_f, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view;
        add_event_TV = view.findViewById(R.id.add_event);
        add_event_list = view.findViewById(R.id.event_list);

        add_event_pojo = new ArrayList<>();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user_ID = firebaseUser.getUid();


        Log.e(". . . .. .", "onViewCreated: "+"start it .   . .. . ." );
        databaseReference.addValueEventListener(new ValueEventListener() {
             Add_Event add_event = null;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                add_event_pojo.clear();
                for (DataSnapshot snapshot : dataSnapshot.child("Event").child(user_ID).getChildren()) {
                    add_event =snapshot.getValue(Add_Event.class);
                    add_event_pojo.add(add_event);
                    Log.e("TAG", "onDataChange: "+add_event.getPlace() );
//if name is not empty gone add event button
                    if (!add_event_pojo.isEmpty()) {
                        add_event_TV.setVisibility(View.GONE);
                        add_event_list.setVisibility(View.VISIBLE);
                    }
                }
                if (getActivity()!=null){
                    Event_List_Adapter event_list_adapter=new Event_List_Adapter(getActivity(),add_event_pojo);
                    add_event_list.setAdapter(event_list_adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        add_event_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert();
            }
        });


    }

    private void Alert() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();


        final View dialogView;


        dialogView = inflater.inflate(R.layout.customdialogwithinput, null);
        dialogBuilder.setView(dialogView);

        place_name_et = dialogView.findViewById(R.id.place_name_ET);
        start_journey_date_et = dialogView.findViewById(R.id.start_jurney_Date_ET);
        start_journey_date_et.setText("");
        end_journey_date_et = dialogView.findViewById(R.id.end_journey_date_ET);
        journey_budget_ET = dialogView.findViewById(R.id.budjet_journey);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener start_Journey_Date = new DatePickerDialog.OnDateSetListener() {

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
                start_journey_date_et.setText(sdf.format(myCalendar.getTime()));

            }

        };

        final DatePickerDialog.OnDateSetListener end_Journey_Date = new DatePickerDialog.OnDateSetListener() {

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
                end_journey_date_et.setText(sdf.format(myCalendar.getTime()));

            }

        };

        start_journey_date_et.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), start_Journey_Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        end_journey_date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), end_Journey_Date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //get Text From EditText Field
                place = place_name_et.getText().toString();
                start_Date = start_journey_date_et.getText().toString();
                end_Date = end_journey_date_et.getText().toString();
                budget = journey_budget_ET.getText().toString();
                if (place.isEmpty()) {
                    Toast.makeText(getContext(), "Place Name Can't empty", Toast.LENGTH_SHORT).show();
                } else if (start_Date.isEmpty()) {
                    Toast.makeText(getContext(), "Date Can't empty", Toast.LENGTH_SHORT).show();

                } else if (end_Date.isEmpty()) {
                    Toast.makeText(getContext(), "Date Can't empty", Toast.LENGTH_SHORT).show();

                } else if (budget.isEmpty()) {
                    Toast.makeText(getContext(), "Budget an't empty", Toast.LENGTH_SHORT).show();

                } else {
                    event_Create_Date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    Add_Event add_event = new Add_Event(place, start_Date, end_Date, budget, event_Create_Date);
                    databaseReference.child("Event").child(user_ID).child(event_Create_Date).setValue(add_event);

                    place_name_et.setText("");
                    start_journey_date_et.setText("");
                    end_journey_date_et.setText("");
                    journey_budget_ET.setText("");
                }


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        dialogBuilder.setCancelable(false);
        dialogBuilder.create();

        dialogBuilder.show();


    }


}
