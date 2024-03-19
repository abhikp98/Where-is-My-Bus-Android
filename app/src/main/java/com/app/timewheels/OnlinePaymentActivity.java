package com.app.timewheels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class OnlinePaymentActivity extends AppCompatActivity {
    TextView tamt;
    SharedPreferences sh;
    EditText bname, accn, ifsc;
    Button payButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tamt = findViewById(R.id.textView14);
        String total = getIntent().getStringExtra("amount");
        tamt.setText("Total amount to be paid: "+total);
        payButton = findViewById(R.id.button10);
        bname = findViewById(R.id.editTextTextPersonName6);
        accn = findViewById(R.id.editTextTextPersonName7);
        ifsc = findViewById(R.id.editTextTextPersonName8);

        StringBuilder seatNumbers = new StringBuilder();
        for (int i = 0; i<CustomSeat.count.length;i++){
            if (CustomSeat.count[i].equalsIgnoreCase("true")){
                seatNumbers.append(i).append(", ");
            }

        }

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tbname = bname.getText().toString();
                String taccn = accn.getText().toString();
                String tifsc = ifsc.getText().toString();

                if (tbname.isEmpty()){
                    bname.setError("Enter Bank name");
                    return;
                }
                if (taccn.isEmpty()){
                    accn.setError("Enter Accouny Number");
                    return;
                }
                if (tifsc.isEmpty()){
                    ifsc.setError("Enter IFSC Code");
                    return;
                }
                String url = sh.getString("url", "")+"/payment";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(getApplicationContext(), "Successfully Paid", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeUserActivity.class));
                                        finish();

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

                        params.put("reqid", getIntent().getStringExtra("reqid"));//passing to python
                        params.put("loginid", sh.getString("lid", ""));
                        params.put("bname", tbname);
                        params.put("accn", taccn);
                        params.put("ifsc", tifsc);
                        params.put("total", total);
                        params.put("seatnumbers", seatNumbers.toString());
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