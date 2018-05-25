package com.example.imransk.bitmproject.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imransk.bitmproject.R;

import java.util.ArrayList;

/**
 * Created by imran sk on 5/25/2018.
 */

public class List_Event_F extends Fragment {

    TextView add_event_TV;
    ListView add_event_list;
    ArrayList<String> name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Event List");
        return inflater.inflate(R.layout.list_event_f,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add_event_TV =view.findViewById(R.id.add_event);
        add_event_list=view.findViewById(R.id.event_list);

        name=new ArrayList<>();

        if (!name.isEmpty()){
            add_event_TV.setVisibility(View.GONE);
        }

        add_event_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alert();
            }
        });

    }

    private void Alert() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = this.getLayoutInflater();


        final View dialogView;


//        dialogView = inflater.inflate(R.layout.customdialogwithinput, null);
//        dialogBuilder.setView(dialogView);



//        circleImageView = (CircleImageView) dialogView.findViewById(R.id.circle_image_view);

//set Action on Image view
//        circleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*")
//                        .setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "select image"), profile_image_Code);
//                select_img_ET.setVisibility(View.GONE);
//            }
//        });
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        }).create();

        dialogBuilder.show();
    }
}
