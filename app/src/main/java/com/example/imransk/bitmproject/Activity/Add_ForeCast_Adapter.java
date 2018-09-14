package com.example.imransk.bitmproject.Activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imransk.bitmproject.ModelClass.ForeCast_Weather_Response;
import com.example.imransk.bitmproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Add_ForeCast_Adapter extends BaseAdapter {
    List<ForeCast_Weather_Response.List> lists;
    ForeCast_Weather_Response.City city;
    Context context;
    LayoutInflater layoutInflater=null;
    public Add_ForeCast_Adapter(FragmentActivity activity, List<ForeCast_Weather_Response.List> list, ForeCast_Weather_Response.City city) {
        this.lists = list;
        this.city = city;
        context=activity;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lists.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        view=layoutInflater.inflate(R.layout.fourcast_custom_view,null);

        TextView max_temp=view.findViewById(R.id.max_temp_id);
        TextView min_temp=view.findViewById(R.id.min_temp_id);
        ImageView imageView=view.findViewById(R.id.image_icon_forecast);

  /*      double max_Temp_st = lists.get(i).getTemp().getMax();
        double min_Temp_st = lists.get(i).getTemp().getMin();*/

        int max_Temp_st = (int) (lists.get(i).getTemp().getMax()-273);
        int min_Temp_st = (int) (lists.get(i).getTemp().getMin()-273);

        max_temp.setText("Max Temp : "+String.valueOf(max_Temp_st)+" °C");
        min_temp.setText("Max Temp : "+String.valueOf(min_Temp_st)+" °C");

        String image_irl = lists.get(i).getWeather().get(0).getIcon();

        String imageUrl = "https://openweathermap.org/img/w/"+image_irl+".png";
        Picasso.get().load(imageUrl).into(imageView);

        return view;
    }
}
