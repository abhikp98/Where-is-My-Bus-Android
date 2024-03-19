package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TextView name = findViewById(R.id.textView2);
        TextView email = findViewById(R.id.textView6);
        ImageView imgv = findViewById(R.id.imageView5);

        String imgurl = sh.getString("url", "")+sh.getString("profilepic", "");
        Picasso.with(getApplicationContext()).load(imgurl).transform(new CircleTransform()).into(imgv);
        name.setText(sh.getString("name", ""));
        email.setText(sh.getString("email", ""));

    }
}