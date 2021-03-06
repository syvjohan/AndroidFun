package com.example.johan.assignment3;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by johan on 2/15/2015.
 */
public class IncomeAdapter extends ArrayAdapter<Data> {
    private ArrayList<Data> lstIncome;
    private LayoutInflater layoutInflater;

    public IncomeAdapter(Context context, ArrayList<Data> data) {
        super(context, 0, data);
        this.lstIncome = data;
        layoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public Data getItem(int position) {
        return lstIncome.get(position);
    }

    @Override
    public int getCount() {
        return lstIncome.size();
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
        Resources res = parent.getResources();

        if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.row_income, parent, false);

                holder.incomeDate = (TextView) convertView.findViewById(R.id.txt_income_date);
                holder.incomeAmount = (TextView) convertView.findViewById(R.id.txt_income_amount);
                holder.incomeTitle = (TextView) convertView.findViewById(R.id.txt_income_title);

                convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Data data = lstIncome.get(position);

        // Populate the data into the template view using the data object
        holder.incomeDate.setText("Date: " + data.GetDate());
        holder.incomeTitle.setText("Title: " + data.GetTitle());
        holder.incomeAmount.setText("Amount: " + data.GetAmount());

        if (position % 2 == 1) {
            convertView.setBackgroundColor(res.getColor(R.color.odd_color));
        } else {
            convertView.setBackgroundColor(res.getColor(R.color.even_color));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public static class ViewHolder {
        public TextView incomeDate;
        public TextView incomeAmount;
        public TextView incomeTitle;
    }
}

