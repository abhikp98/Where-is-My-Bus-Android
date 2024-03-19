package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomLongBusSearchView extends BaseAdapter {
    String[] name, start, end, amount;
    Context context;

    public CustomLongBusSearchView(Context applicationContext, String[] name, String[] start, String[] end, String[] amount) {
        this.context = applicationContext;
        this.name = name;
        this.start = start;
        this.end = end;
        this.amount = amount;
    }

    @Override
    public int getCount() {
        return name.length;
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
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_long_bus_search_view, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView bsname = gridView.findViewById(R.id.textView4);
        TextView bstart = gridView.findViewById(R.id.textView13);
        TextView bend = gridView.findViewById(R.id.textView23);
        TextView bamount = gridView.findViewById(R.id.textView24);


        bsname.setText(name[i]);
        bstart.setText("Starting at: "+start[i]);
        bend.setText("Ending at: "+end[i]);
        bamount.setText(amount[i]+" ₹");

        return gridView;
    }
}