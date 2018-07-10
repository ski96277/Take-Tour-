package com.example.imransk.bitmproject.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.R;

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
    ArrayList<String> name;
    View context;


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

        name = new ArrayList<>();

        if (!name.isEmpty()) {
            add_event_TV.setVisibility(View.GONE);
        }

        add_event_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert();
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void Alert() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();


        final View dialogView;


        dialogView = inflater.inflate(R.layout.customdialogwithinput, null);
        dialogBuilder.setView(dialogView);


//        circleImageView = (CircleImageView) dialogView.findViewById(R.id.circle_image_view);


        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

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


        TextView place_name_et;
        final TextView start_journey_date_et;
        final TextView end_journey_date_et;
        final Button choose_image_btn;


        place_name_et = dialogView.findViewById(R.id.place_name_ET);
        start_journey_date_et = dialogView.findViewById(R.id.start_jurney_Date_ET);
        start_journey_date_et.setText("");
        end_journey_date_et = dialogView.findViewById(R.id.end_journey_date_ET);
        choose_image_btn = (Button) dialogView.findViewById(R.id.choose_image_btn);
        choose_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "hi", Toast.LENGTH_SHORT).show();
            }
        });

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
                start_journey_date_et .setText(sdf.format(myCalendar.getTime()));

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
                end_journey_date_et .setText(sdf.format(myCalendar.getTime()));

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

    }


}
