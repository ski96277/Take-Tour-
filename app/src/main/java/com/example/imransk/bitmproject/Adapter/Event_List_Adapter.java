package com.example.imransk.bitmproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.Fragment.Event_Details;
import com.example.imransk.bitmproject.ModelClass.Add_Event;
import com.example.imransk.bitmproject.R;

import java.util.ArrayList;

public class Event_List_Adapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater=null;

    ArrayList<Add_Event>add_event_pojo;

    public Event_List_Adapter(FragmentActivity activity, ArrayList<Add_Event> add_event_pojo) {
        this.context=activity;
        this.add_event_pojo=add_event_pojo;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return add_event_pojo.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View event_item_list=layoutInflater.inflate(R.layout.custom_event_list,null);
        TextView place=event_item_list.findViewById(R.id.Place_id_TV);
        final TextView budget=event_item_list.findViewById(R.id.budget_id_TV);
        TextView start_date=event_item_list.findViewById(R.id.start_date_id_TV);
        TextView end_date=event_item_list.findViewById(R.id.end_date_id_TV);

        final Add_Event add_event = add_event_pojo.get(i);

        place.setText("Place: "+add_event.getPlace());
        budget.setText("Budget: "+add_event.getBudget()+"Tk");
        start_date.setText("Date: "+add_event.getStart_Date());
        end_date.setText(""+add_event.getEnd_Date());
        event_item_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putString("place",add_event.getPlace());
                bundle.putString("budget",add_event.getBudget());
                bundle.putString("st_Date",add_event.getStart_Date());
                bundle.putString("end_Date",add_event.getEnd_Date());
                bundle.putString("event_create_date",add_event.getEvent_Create_Date());
                bundle.putString("event_type",add_event.getEvent_type());
                Fragment fragment=new Event_Details();
                if (fragment!=null){
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.screen_Area_main, fragment);
                    fragmentTransaction.addToBackStack("");
                    fragment.setArguments(bundle);
                    fragmentTransaction.commit();
                }
            }
        });
        return event_item_list;
    }
}
