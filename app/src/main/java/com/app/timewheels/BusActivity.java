package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BusActivity extends AppCompatActivity {
    Button complaint, liveloc;
    ImageButton rating;
    SharedPreferences sh;
    ListView ls;
    String[] pstop, ptime, delaytime;
    String busname, currentrating, latitude, longitude;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        Intent i = getIntent();
        String busid = i.getStringExtra("busid");
        String route = i.getStringExtra("routeid");
        String runningstatus = i.getStringExtra("running");
        getSupportActionBar().setTitle(i.getStringExtra("routename"));
        complaint = findViewById(R.id.button6);
        rating = findViewById(R.id.imageButton);
        ls = findViewById(R.id.buslist);
        liveloc = findViewById(R.id.button7);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sh.getString("url", "")+"/bustimings";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                busname = jsonObj.getString("busname");
                                currentrating = jsonObj.getString("currentrating");

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                pstop = new String[js.length()];
                                ptime = new String[js.length()];
                                delaytime = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    pstop[i] = u.getString("stop");//dbcolumn name in double quotes
                                    ptime[i] = u.getString("time");
                                    delaytime[i] = u.getString("delaytime");


                                }
                                ls.setAdapter(new CustomBusTimings(getApplicationContext(), pstop, ptime, delaytime, runningstatus));//custom_view_service.xml and li is the listview object


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
                params.put("busid", busid);
                params.put("routeid", route);
                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ComplaintActivity.class);
                i.putExtra("busid", busid);
                i.putExtra("busname", busname);
                startActivity(i);
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RatingActivity.class);
                i.putExtra("busid", busid);
                i.putExtra("busname", busname);
                i.putExtra("currentrate", currentrating);
                startActivity(i);
            }
        });

        liveloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = sh.getString("url", "")+"/user_bus_track";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        latitude = jsonObj.getString("latitude");
                                        longitude = jsonObj.getString("longitude");

                                        Uri mapurl = Uri.parse("http://maps.google.com/maps?q=loc:"+latitude+", "+longitude);
                                        Intent i = new Intent(Intent.ACTION_VIEW, mapurl);
                                        i.setPackage("com.google.android.apps.maps");
                                        startActivity(i);


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Wait for Driver to update Location", Toast.LENGTH_LONG).show();
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
                        params.put("busid", busid);
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