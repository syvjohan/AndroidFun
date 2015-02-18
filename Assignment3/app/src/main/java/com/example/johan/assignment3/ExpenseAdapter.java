package com.example.johan.assignment3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by johan on 2/16/2015.
 */
public class ExpenseAdapter extends ArrayAdapter<Data> {
    private ArrayList<Data> lstExpense;
    private LayoutInflater layoutInflater;

    public ExpenseAdapter(Context context, ArrayList<Data> data) {
        super(context, 0, data);
        this.lstExpense = data;
        layoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public Data getItem(int position) {
        return lstExpense.get(position);
    }

    @Override
    public int getCount() {
        return lstExpense.size();
    }

    //Number of layouts
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.row_expense, parent, false);

            holder.expenseDate = (TextView) convertView.findViewById(R.id.txt_expense_date);
            holder.expenseAmount = (TextView) convertView.findViewById(R.id.txt_expense_amount);
            holder.expenseTitle = (TextView) convertView.findViewById(R.id.txt_expense_title);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Data data = lstExpense.get(position);

        // Populate the data into the template view using the data object
        holder.expenseDate.setText("Date: " + data.GetDate());
        holder.expenseTitle.setText("Title: " + data.GetTitle());
        holder.expenseAmount.setText("Amount: " + data.GetAmount());

        // Return the completed view to render on screen
        return convertView;
    }

    public static class ViewHolder {
        public TextView expenseDate;
        public TextView expenseAmount;
        public TextView expenseTitle;
    }
}