package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class CustomSeat extends BaseAdapter {
    Context context;
    String[] sid, seat, status;
    public static String[] count;
    Integer seatCount = 0;


    public CustomSeat(Context applicationContext, String[] sid, String[] seat, String[] status) {
        this.context = applicationContext;
        this.sid = sid;
        this.seat = seat;
        this.status = status;
        count = new String[sid.length];
        for(int i = 0; i< sid.length; i++){
            if (status[i].equalsIgnoreCase("pending")){
                count[i] = "false";
            }
            else if(status[i].equalsIgnoreCase("approved")) {
                count[i] = "booked";
            }
            else {
                count[i] = "true";
            }
        }

    }


    @Override
    public int getCount() {
        return seat.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_seat, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView bseat = gridView.findViewById(R.id.button8);
        bseat.setText(seat[i]);
        if (status[i].equalsIgnoreCase("approved")){
            bseat.setBackgroundColor(bseat.getContext().getResources().getColor(R.color.my_dark_primary));
            bseat.setText("Booked");
            bseat.setTextColor(Color.WHITE);
            bseat.setEnabled(false);
        }
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);

        bseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatCount>=5 && count[i].equalsIgnoreCase("false")){
                    Toast.makeText(context, "Max seat can be selected is 5", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (count[i].equalsIgnoreCase("false")){
                    bseat.setBackgroundResource(R.color.green);
                    bseat.setTextColor(Color.WHITE);
                    seatCount++;
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("seatcount", seatCount.toString());
                    editor.commit();
                    ((AdapterView) viewGroup).performItemClick(view, i, getItemId(i));
                }
                else {
                    bseat.setBackgroundResource(R.color.white);
                    bseat.setTextColor(Color.BLACK);
                    seatCount--;
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("seatcount", seatCount.toString());
                    editor.commit();
                    ((AdapterView) viewGroup).performItemClick(view, i, getItemId(i));
                }
                if (count[i].equalsIgnoreCase("false")){
                    count[i] = "true";
                }
                else {
                    count[i] = "false";
                }


            }
        });

        return gridView;
    }
}