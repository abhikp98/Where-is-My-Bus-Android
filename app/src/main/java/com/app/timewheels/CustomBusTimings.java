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

public class CustomBusTimings extends BaseAdapter {
    String[] stop, time, delaytime;
    String runningstatus;
    Context context;

    public CustomBusTimings(Context applicationContext, String[] pstop, String[] ptime, String[] delaytime, String runningstatus) {
        this.context = applicationContext;
        this.stop = pstop;
        this.time = ptime;
        this.delaytime = delaytime;
        this.runningstatus = runningstatus;
    }

    @Override
    public int getCount() {
        return stop.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_bus_timings, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView stopname = (TextView) gridView.findViewById(R.id.textView14);
        TextView bustime = (TextView) gridView.findViewById(R.id.textView15);
        TextView dela = (TextView) gridView.findViewById(R.id.textView10);

        if (runningstatus.equalsIgnoreCase("True")){
            stopname.setText(stop[i]);
            bustime.setText(time[i]);
            if (time[i].equalsIgnoreCase(delaytime[i])){
                dela.setTextColor(Color.GREEN);
                dela.setText(delaytime[i]);
            }
            else {
                dela.setTextColor(Color.RED);
                dela.setText(delaytime[i]);
            }


        }
        else {
            stopname.setText(stop[i]);
            bustime.setText(time[i]);
            dela.setVisibility(View.GONE);
        }



//        tv1.setTextColor(Color.RED);//color setting
//        tv2.setTextColor(Color.BLACK);
//        tv3.setTextColor(Color.BLACK);
        stopname.setText(stop[i]);
        bustime.setText(time[i]);

        return gridView;
    }
}