package com.example.imransk.bitmproject.ModelClass;

public class Add_Expense {
    String price="";
    String type="";

    public Add_Expense() {
    }

    public Add_Expense(String price, String type) {

        this.price = price;
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}
