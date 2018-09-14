package com.example.imransk.bitmproject.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.Activity.MainActivity;
import com.example.imransk.bitmproject.Adapter.ExpandableListAdapter;
import com.example.imransk.bitmproject.Adapter.Expense_List_Adapter;
import com.example.imransk.bitmproject.ModelClass.Add_Expense;
import com.example.imransk.bitmproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Event_Details extends Fragment{

    TextView placeTv;
    TextView budgetTv;
    TextView expance_persentage_TV;
    ProgressBar progressBar;
    int expence;
    int expence_pesentage;

    String TAG = "Event Details";

    private ExpandableListView listView;


    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;

    private HashMap<String, List<String>> listHash;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user_ID;

    String place;
    String budget;
    String st_Date;
    String end_Date;
    String event_create_date;
    String event_type;

    Bundle bundle;

    //Alert dialog
    EditText expense_Title_TV;
    EditText expense_Tk_TV;
    Button cancel_btn;
    Button submit_btn;
    String expense_title;
    String expense_tk;

    //Expense_List_AlertDialog
    ListView expense_List;
    Button cancel_expense_btn;
    ArrayList<Add_Expense> expenseArrayList;
    Add_Expense add_expense = null;
    TextView total_tk_TV;
    int total_Cost = 0;

    //Add_More_Budget_AlertDialog
    TextView current_budget_TV;
    EditText new_Budget_ET;
    Button budget_Cancel_btn;
    Button budget_Add_btn;
    String new_budget;

    //capture Image
    int image_Request_Code = 100;

   static Uri capturedImageUri=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Event Details");
        return inflater.inflate(R.layout.event_details, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user_ID = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        expenseArrayList = new ArrayList<>();


        placeTv = view.findViewById(R.id.place_Name_event_details_TV);
        budgetTv = view.findViewById(R.id.budget_event_details_TV);
        expance_persentage_TV = view.findViewById(R.id.expence_persentage);
        progressBar = view.findViewById(R.id.expence_progress);

        bundle = getArguments();
        place = bundle.getString("place");
        budget = bundle.getString("budget");
        st_Date = bundle.getString("st_Date");
        end_Date = bundle.getString("end_Date");
        event_create_date = bundle.getString("event_create_date");
        event_type = bundle.getString("event_type");


        placeTv.setText("" + place);
        Log.e(TAG, "....." + event_type);

        //get All Total Cost and show  Persentage
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expence = 0;
                for (DataSnapshot snapshot : dataSnapshot.child("Event").child(user_ID).child(event_create_date)
                        .child("expense").getChildren()) {
                    add_expense = snapshot.getValue(Add_Expense.class);
                    expence += Integer.valueOf(add_expense.getPrice());
                    Log.e(TAG, "getPrice..... . . ." + add_expense.getPrice());

                }

                Log.e(TAG, "expense" + expence);
                expence_pesentage = (expence * 100) / Integer.valueOf(budget);

                budgetTv.setText("Budget Status: " + expence + " / " + budget + " Tk.");
                expance_persentage_TV.setText(String.valueOf(expence_pesentage) + " %");
                progressBar.setProgress(expence_pesentage);
                if (expence_pesentage <= 50) {

                    progressBar.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);

                }
                if (expence_pesentage > 50 && expence_pesentage <= 80) {

                    progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

                }
                if (expence_pesentage > 100) {
                    Expense_Over_AlertShowing();

                    progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView = view.findViewById(R.id.lvExp);

        initData();

        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listHash);

        listView.setAdapter(listAdapter);

// Listview on child click listener
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
// TODO Auto-generated method stub
                String item = listHash.get(listDataHeader.get(groupPosition)).get(childPosition);

                if (item.equals("Add New Expense")) {
                    AddNewExpense_Alert();
                }
                if (item.equals("View All Expense")) {
                    Expense_List_AlertDialog();
                }
                if (item.equals("Add More Budget")) {
                    Add_More_Budget_AlertDialog();
                }
                if (item.equals("Take a photo")) {
                    Take_Image();
                    Toast.makeText(getContext(), "photo", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("View Gallery")) {
                    Toast.makeText(getContext(), "gallery", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("View All Moments")) {
                    Toast.makeText(getContext(), "moment", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("Edit Event")) {
                    Fragment fragment = new Edit_Event_F();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack("");
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.screen_Area_main, fragment);
                        fragmentTransaction.commit();
                    }

                    Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
                }
                if (item.equals("Delete Event")) {
                    Delete_Event_AlertShowing();
                }

                return false;
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==image_Request_Code&&resultCode==RESULT_OK){
            if (data!=null){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                Log.e(TAG, "Image bitmap --- : --- :  "+bitmap );


            }
        }
    }

    //take image method call from expendable list view
    private void Take_Image() {
        Intent image_capture_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(image_capture_intent,image_Request_Code);
    }

    //Add More budget
    private void Add_More_Budget_AlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater layoutInflater = this.getLayoutInflater();

        View dialogView;
        dialogView = layoutInflater.inflate(R.layout.add_more_budget_alert, null);
        builder.setView(dialogView);
        final AlertDialog alertDialog;
        alertDialog = builder.create();


        current_budget_TV = dialogView.findViewById(R.id.current_budget_ID_TV);
        new_Budget_ET = dialogView.findViewById(R.id.new_budget_ID_TV);
        budget_Cancel_btn = dialogView.findViewById(R.id.cancel_budget_btn_id);
        budget_Add_btn = dialogView.findViewById(R.id.add_budget_btn_id);

        //set old budget in textView
        current_budget_TV.setText(budget);

        budget_Cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        budget_Add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_budget = new_Budget_ET.getText().toString();

                if (new_budget.isEmpty()) {
                    alertDialog.dismiss();
                    return;
                }
                int final_budget = Integer.valueOf(new_budget) + Integer.valueOf(budget);
                Log.e(TAG, "New budget" + final_budget);
                databaseReference.child("Event").child(user_ID).child(event_create_date).child("details")
                        .child("information").child("budget").setValue(String.valueOf(final_budget));
//set final budget in Text View
                budget = String.valueOf(final_budget);

                alertDialog.dismiss();

            }
        });


        alertDialog.show();


    }

    private void Expense_List_AlertDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = this.getLayoutInflater();

        View dialogView;
        dialogView = layoutInflater.inflate(R.layout.view_all_expense, null);

        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();

        expense_List = dialogView.findViewById(R.id.expense_list_ID_listView);
        total_tk_TV = dialogView.findViewById(R.id.total_expense_Tk_ID_TV);

        cancel_expense_btn = dialogView.findViewById(R.id.cancel_expense_ID_btn);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expenseArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.child("Event").child(user_ID).child(event_create_date)
                        .child("expense").getChildren()) {
                    add_expense = snapshot.getValue(Add_Expense.class);
                    total_Cost += Integer.valueOf(add_expense.getPrice());
                    expenseArrayList.add(add_expense);
                }
                if (getActivity() != null) {
                    Expense_List_Adapter expense_list_adapter = new Expense_List_Adapter(getContext(), expenseArrayList);
                    expense_List.setAdapter(expense_list_adapter);
                    total_tk_TV.setText("= " + String.valueOf(total_Cost) + "Tk");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cancel_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    //Add new Expense alart
    private void AddNewExpense_Alert() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();


        final View dialogView;


        dialogView = inflater.inflate(R.layout.add_expense_alert, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.create();

        expense_Title_TV = dialogView.findViewById(R.id.expense_Title_ID_ET);
        expense_Tk_TV = dialogView.findViewById(R.id.expense_Tk_ID_ET);
        cancel_btn = dialogView.findViewById(R.id.cancel_ID_btn);
        submit_btn = dialogView.findViewById(R.id.submit_ID_btn);

        //create alert dialog for cancel alert dialog

        final AlertDialog alertDialog = dialogBuilder.create();
        //Button action
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expense_tk = expense_Tk_TV.getText().toString();
                expense_title = expense_Title_TV.getText().toString();

                Log.e(TAG, "expense_title....... . . .. ." + expense_title);
                Log.e(TAG, "expense_tk....... . . .. ." + expense_tk);

                Add_Expense add_expense;
                add_expense = new Add_Expense(expense_tk, expense_title);
                if (expense_title.isEmpty() || expense_tk.isEmpty()) {
                    alertDialog.cancel();
                    return;
                }
                databaseReference.child("Event").child(user_ID).child(event_create_date).child("expense")
                        .child(expense_title).setValue(add_expense);
                alertDialog.cancel();
            }
        });
        alertDialog.show();
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


        listHash.put(listDataHeader.get(0), expend_Tk);

        listHash.put(listDataHeader.get(1), moment_take);

        listHash.put(listDataHeader.get(2), edit_or_delete);

    }

    //show alert if Budget Tk is over
    private void Expense_Over_AlertShowing() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Budget Alert");
        builder.setMessage("Your Budget Exist\nAdd More Tk");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }

    //show alert to Delete Event
    private void Delete_Event_AlertShowing() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete This Event ...!");
        builder.setMessage("If you Click Delete than this will be deleted");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("Event").child(user_ID).child(event_create_date).removeValue();
                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });
        builder.show();

    }


}
