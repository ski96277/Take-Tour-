package com.example.imransk.bitmproject.ModelClass;

/**
 * Created by imran sk on 7/11/2018.
 */

public class Add_Event {
    String place;
    String start_Date;
    String end_Date;
    String budget;
    String event_Create_Date;

    public Add_Event(String place, String start_Date, String end_Date, String budget,String event_Create_Date) {
        this.place = place;
        this.start_Date = start_Date;
        this.end_Date = end_Date;
        this.budget = budget;
        this.event_Create_Date=event_Create_Date;
    }
}
