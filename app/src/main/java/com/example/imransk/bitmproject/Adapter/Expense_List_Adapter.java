package com.example.imransk.bitmproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imransk.bitmproject.ModelClass.Add_Expense;
import com.example.imransk.bitmproject.R;

import java.util.ArrayList;

public class Expense_List_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Add_Expense> expenseArrayList;
    LayoutInflater layoutInflater=null;

    public Expense_List_Adapter(Context context, ArrayList<Add_Expense> expenseArrayList) {
        this.context = context;
        this.expenseArrayList = expenseArrayList;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return expenseArrayList.size();
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
        TextView title_extense_TV;
        TextView expense_TK_TV;
        Add_Expense add_expense;


        view=layoutInflater.inflate(R.layout.custom_expense_list,null);

        title_extense_TV=view.findViewById(R.id.expense_title_custom_ID_TV);
        expense_TK_TV=view.findViewById(R.id.expense_Tk_custom_ID_TV);
        add_expense=expenseArrayList.get(i);

        title_extense_TV.setText(add_expense.getType());
        expense_TK_TV.setText(add_expense.getPrice());

        return view;
    }
}
