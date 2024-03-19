package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    EditText ed;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (!sh.getString("url", "").isEmpty()){
                    String url = sh.getString("url", "")+"/check";
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                    if (sh.getString("type", "").equalsIgnoreCase("driver")){
                                        finish();
                                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(0,0);
                                    }
                                    else if (sh.getString("type", "").equalsIgnoreCase("user")){
                                        finish();
                                        Intent i = new Intent(getApplicationContext(),HomeUserActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(0,0);
                                    }
                                    else {
                                        finish();
                                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(0,0);
                                    }

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Not found, Try Again", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
//                                    Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();

                            finish();
                            overridePendingTransition(0, 0);
                            Intent i = new Intent(getApplicationContext(), IpActivity.class);
                            startActivity(i);
                            overridePendingTransition(0,0);
                        }
                    }
                    ) {

                        //                                value Passing android to python
                        @Override
                        protected Map<String, String> getParams() {
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Map<String, String> params = new HashMap<String, String>();

//                        params.put("u", username);//passing to python
//                        params.put("p", password);
                            return params;
                        }
                    };


                    int MY_SOCKET_TIMEOUT_MS = 1000;

                    postRequest.setRetryPolicy(new DefaultRetryPolicy(
                            MY_SOCKET_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(postRequest);


                }
                else {
                    finish();
                    overridePendingTransition(0, 0);
                    Intent i = new Intent(getApplicationContext(), IpActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }
            }
        }, 3000);
    }

}