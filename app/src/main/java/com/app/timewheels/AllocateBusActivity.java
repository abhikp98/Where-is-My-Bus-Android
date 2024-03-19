package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AllocateBusActivity extends AppCompatActivity {
    SharedPreferences sh;
    TextView t1, t2;
    ProgressDialog pd;
    Button add;
    EditText ed;
    LinearLayout lntab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_bus);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        t2 = findViewById(R.id.textView7);
        add = findViewById(R.id.button3);
        ed = findViewById(R.id.editTextTime);
        lntab = findViewById(R.id.allocatetab);
        ImageView imgv = findViewById(R.id.imageView10);
        TextView textView = findViewById(R.id.textView8);
        String url = sh.getString("url", "")+"/allocated_bus";
        pd = new ProgressDialog(this);
        pd.setTitle("wait");
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            pd.dismiss();
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                if (jsonObj.getString("busstatus").equalsIgnoreCase("Yes")){
                                    Toast.makeText(AllocateBusActivity.this, "Bus got blocked by Admin, Please contact Owner!", Toast.LENGTH_SHORT).show();
                                    add.setVisibility(View.INVISIBLE);
                                    ed.setVisibility(View.INVISIBLE);
                                    t2.setText("Bus got blocked Please contact Owner!");
                                    return;


                                }

                                String delay = jsonObj.getString("delay");
                                if (!delay.equalsIgnoreCase("0")){
                                    t2.setText(jsonObj.getString("busname")+" ("+delay+" minutes late)");
                                }
                                else {
                                    t2.setText(jsonObj.getString("busname")+" (On time)");
                                }



                            } else {
                                imgv.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);
                                lntab.setVisibility(View.INVISIBLE);

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
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            //                                value Passing android to python
            @Override
            protected Map<String, String> getParams() {
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sh.getString("lid", ""));
                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed.getText().toString().isEmpty()){
                    Toast.makeText(AllocateBusActivity.this, "Cant be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = sh.getString("url", "")+"/add_delay";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        finish();
                                        Intent i = new Intent(getApplicationContext(), AllocateBusActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(0, 0);



                                    } else {
                                        Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();

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
                                Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {

                    //                                value Passing android to python
                    @Override
                    protected Map<String, String> getParams() {
                        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", sh.getString("lid", ""));
                        params.put("delay", ed.getText().toString());
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