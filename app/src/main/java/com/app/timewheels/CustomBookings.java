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

public class CustomBookings extends BaseAdapter {
    String[] busname, date, stime, etime, seatlist, from, to;
    Context context;
    public CustomBookings(Context applicationContext, String[] busname, String[] date, String[] stime, String[] etime, String[] seatlist, String[] from, String[] to) {
        this.context = applicationContext;
        this.busname = busname;
        this.date = date;
        this.stime = stime;
        this.etime = etime;
        this.seatlist = seatlist;
        this.from = from;
        this.to = to;
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
            gridView = inflator.inflate(R.layout.activity_custom_bookings, null);//same class name

        } else {
            gridView = (View) view;
        }
        TextView tbusname = (TextView) gridView.findViewById(R.id.textView29);
        TextView tdate = (TextView) gridView.findViewById(R.id.textView30);
        TextView tseatlist = (TextView) gridView.findViewById(R.id.textView32);
        TextView tstime = (TextView) gridView.findViewById(R.id.textView33);
        TextView tetime = (TextView) gridView.findViewById(R.id.textView31);
        TextView tfrom = (TextView) gridView.findViewById(R.id.textView35);
        TextView tto = (TextView) gridView.findViewById(R.id.textView37);

        tbusname.setText(busname[i]);
        tdate.setText("On: "+date[i]);
        tseatlist.setText("Seat Numbers: "+seatlist[i]);
        tstime.setText(stime[i]);
        tetime.setText(etime[i]);
        tfrom.setText(from[i]);
        tto.setText(to[i]);

        return gridView;
    }
}