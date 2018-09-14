package com.example.imransk.bitmproject.ModelClass;

/**
 * Created by imran sk on 7/11/2018.
 */

public class Add_Event {
    String place = "";
    String start_Date = "";
    String end_Date = "";
    String budget = "";
    String event_Create_Date = "";
    String event_type = "";

    public Add_Event() {

    }

    public Add_Event(String place, String start_Date, String end_Date, String budget, String event_Create_Date,
                     String event_type) {
        this.place = place;
        this.start_Date = start_Date;
        this.end_Date = end_Date;
        this.budget = budget;
        this.event_Create_Date = event_Create_Date;
        this.event_type=event_type;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getPlace() {
        return place;
    }

    public String getStart_Date() {
        return start_Date;
    }

    public String getEnd_Date() {
        return end_Date;
    }

    public String getBudget() {
        return budget;
    }

    public String getEvent_Create_Date() {
        return event_Create_Date;
    }
}
