package com.example.imransk.bitmproject.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.Adapter.ExpandableListAdapter;
import com.example.imransk.bitmproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event_Details extends Fragment {

    TextView placeTv;
    TextView budgetTv;
    TextView expance_persentage_TV;
    ProgressBar progressBar;
    String expence="4000";
    int expence_pesentage;


    private ExpandableListView listView;


    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;

    private HashMap<String,List<String>> listHash;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Event Details");
        return inflater.inflate(R.layout.event_details,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle=getArguments();
        String place=bundle.getString("place");
        String budget=bundle.getString("budget");
        String st_Date=bundle.getString("st_Date");
        String end_Date=bundle.getString("end_Date");
        String event_create_date=bundle.getString("event_create_date");

        expence_pesentage=(Integer.valueOf(expence)*100)/Integer.valueOf(budget);



        placeTv=view.findViewById(R.id.place_Name_event_details_TV);
        budgetTv=view.findViewById(R.id.budget_event_details_TV);
        expance_persentage_TV=view.findViewById(R.id.expence_persentage);
        progressBar=view.findViewById(R.id.expence_progress);

        progressBar.setProgress(expence_pesentage);
        if (expence_pesentage<=50){

            progressBar.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);

        }if (expence_pesentage>50 && expence_pesentage<=80){

            progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        }
        if (expence_pesentage>100){
            AlertShowing();
        }

        placeTv.setText(""+place);
        budgetTv.setText("Budget Status: "+expence+" / "+budget+" Tk.");
        expance_persentage_TV.setText(String.valueOf(expence_pesentage)+" %");

        listView = view.findViewById(R.id.lvExp);

        initData();

        listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash);

        listView.setAdapter(listAdapter);

// Listview on child click listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
// TODO Auto-generated method stub
                String item=listHash.get(listDataHeader.get(groupPosition)).get(childPosition);

                if (item.equals("Add New Expense")){
                    Toast.makeText(getContext(), "Add", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("View All Expense")){
                    Toast.makeText(getContext(), "View", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("Add More Budget")){
                    Toast.makeText(getContext(), "Budget", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("Take a photo")){
                    Toast.makeText(getContext(), "photo", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("View Gallery")){
                    Toast.makeText(getContext(), "gallery", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("View All Moments")){
                    Toast.makeText(getContext(), "moment", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("Edit Event")){
                    Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("Delete Event")){
                    Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                }/*
                Toast.makeText( getContext(),listDataHeader.get(groupPosition)+ " : "+ listHash.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });

    }

    private void initData() {
        listDataHeader = new ArrayList<>();

        listHash = new HashMap<>();



        listDataHeader.add("Expenditure");

        listDataHeader.add("Moments");

        listDataHeader.add("More On Event");




        List<String> expend_Tk = new ArrayList<>();

        expend_Tk.add("Add New Expense");
        expend_Tk.add("View All Expense");
        expend_Tk.add("Add More Budget");



        List<String> moment_take = new ArrayList<>();

        moment_take.add("Take a photo");

        moment_take.add("View Gallery");

        moment_take.add("View All Moments");



        List<String> edit_or_delete = new ArrayList<>();

        edit_or_delete.add("Edit Event");

        edit_or_delete.add("Delete Event");



        listHash.put(listDataHeader.get(0),expend_Tk);

        listHash.put(listDataHeader.get(1),moment_take);

        listHash.put(listDataHeader.get(2),edit_or_delete);

    }

    //show alert if Budget Tk is over
    private void AlertShowing() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Budget Alert");
        builder.setMessage("Your Budget Exist\nAdd More Tk");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }
}
