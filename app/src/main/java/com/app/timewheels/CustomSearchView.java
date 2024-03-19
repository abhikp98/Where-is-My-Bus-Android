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

public class CustomSearchView extends BaseAdapter {
    Context context;
    String[] busid, busname, delay, runningstatus;

    public CustomSearchView(Context applicationContext, String[] busid, String[] busname, String[] delay, String[] runningstatus) {
        this.context = applicationContext;
        this.busid = busid;
        this.busname = busname;
        this.delay = delay;
        this.runningstatus = runningstatus;
    }

    @Override
    public int getCount() {
        return busname.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_search_view, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView bsname = (TextView) gridView.findViewById(R.id.textView4);
        TextView bsdelay = (TextView) gridView.findViewById(R.id.textView5);



        bsname.setText(busname[i]);
        if (runningstatus[i].equalsIgnoreCase("True")){
            if (!delay[i].equalsIgnoreCase("0")){
                bsdelay.setTextColor(Color.RED);
                bsdelay.setText(delay[i]+"minutes delay");

            }
            else {
                bsdelay.setTextColor(Color.GREEN);
                bsdelay.setText("On Time");
            }
        }
        else {
            bsdelay.setText("Check timings");
        }

        return gridView;
    }
}