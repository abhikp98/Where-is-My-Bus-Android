package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    RatingBar rtbr;
    ImageButton imgb;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        TextView tv = findViewById(R.id.textView12);
        rtbr = findViewById(R.id.ratingBar);
        imgb = findViewById(R.id.imageButton5);
        rtbr.setRating(Float.parseFloat("0.5"));
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
        String busid = intent.getStringExtra("busid");
        String busname = intent.getStringExtra("busname");
        getSupportActionBar().setTitle("Rate "+busname+" Bus");
        String currentrate = intent.getStringExtra("currentrate");
        tv.setText("Current Rating("+currentrate+")");

        rtbr.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 0.5){
                    tv.setText("Terrible");
                }
                else if (v <= 1){
                    tv.setText("Awful");
                }
                else if (v <= 1.5){
                    tv.setText("Bad");
                }
                else if (v <= 2){
                    tv.setText("Okay");
                }
                else if (v <= 2.5){
                    tv.setText("Good");
                }
                else if (v <= 3){
                    tv.setText("Great");
                }
                else if (v <= 3.5){
                    tv.setText("Fentastic");
                }
                else if (v <= 4){
                    tv.setText("Excelent");
                }
                else if (v <= 4.5){
                    tv.setText("Amazing");
                }
                else if (v <= 5){
                    tv.setText("Outstanding");
                    Toast.makeText(RatingActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Float rating = rtbr.getRating();
                String url = sh.getString("url", "")+"/user_add_rating";
                Toast.makeText(RatingActivity.this, url, Toast.LENGTH_SHORT).show();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(RatingActivity.this, "Thanks for the Submission", Toast.LENGTH_SHORT).show();
                                        finish();
                                        overridePendingTransition(0, 0);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {

                    //                value Passing android to python
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("rating", rating.toString());//passing to python
                        params.put("busid", busid);
                        params.put("userid", sh.getString("lid", ""));

                        return params;
                    }
                };


                int MY_SOCKET_TIMEOUT_MS = 100000;

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(postRequest);

            }
        });

    }
}