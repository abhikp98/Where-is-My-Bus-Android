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

public class CustomViewComplaints extends BaseAdapter {
    String[] busname, complaint, complaintdate, reply, replydate;
    Context context;

    public CustomViewComplaints(Context applicationContext, String[] busname, String[] complaint, String[] complaintdate, String[] reply, String[] replydate) {
        this.context = applicationContext;
        this.busname = busname;
        this.complaint = complaint;
        this.complaintdate = complaintdate;
        this.reply = reply;
        this.replydate = replydate;

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
            gridView = inflator.inflate(R.layout.activity_custom_view_complaints, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tbusname = (TextView) gridView.findViewById(R.id.textView16);
        TextView tcomplaint = (TextView) gridView.findViewById(R.id.textView18);
        TextView tcomplaintdate = (TextView) gridView.findViewById(R.id.textView21);
        TextView treply = (TextView) gridView.findViewById(R.id.textView20);
        TextView treplydate = (TextView) gridView.findViewById(R.id.textView22);
        TextView rep = (TextView) gridView.findViewById(R.id.textView19);



//        tv1.setTextColor(Color.RED);//color setting
//        tv2.setTextColor(Color.BLACK);
//        tv3.setTextColor(Color.BLACK);

        tbusname.setText(busname[i]);
        tcomplaint.setText(complaint[i]);
        tcomplaintdate.setText(complaintdate[i]);
        if (!reply[i].equalsIgnoreCase("Pending")){
            rep.setVisibility(View.VISIBLE);
            treplydate.setVisibility(View.VISIBLE);
            treply.setText(reply[i]);
            treplydate.setText(replydate[i]);
        }
        else {
            rep.setVisibility(View.INVISIBLE);
            treplydate.setVisibility(View.INVISIBLE);
            treply.setText("No replies yet");
            treply.setTextColor(Color.RED);

        }


        return gridView;
    }
}