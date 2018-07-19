package com.example.imransk.bitmproject.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView budget=event_item_list.findViewById(R.id.budget_id_TV);
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
                Toast.makeText(context, ""+i, Toast.LENGTH_SHORT).show();
            }
        });
        return event_item_list;
    }
}
