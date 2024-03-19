package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BusBookActivity extends AppCompatActivity {
    TextView amount, seatCount;
    Button pay;
    SharedPreferences sh;
    GridView grid;
    ProgressDialog pd;
    String [] sid, seat, status;
    String amt;
    float am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_book);
        amount = findViewById(R.id.textView25);
        seatCount = findViewById(R.id.textView27);
        pay = findViewById(R.id.button5);
        grid = findViewById(R.id.gridview);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        amount.setText("0.0");
        seatCount.setText("0");

        String url = sh.getString("url", "")+"/get_seats";
        pd = new ProgressDialog(this);
        pd.setTitle("Wait...");
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            pd.dismiss();
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                amt = jsonObj.getString("amount");
                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                sid = new String[js.length()];
                                seat = new String[js.length()];
                                status = new String[js.length()];

                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    sid[i] = u.getString("sid");//dbcolumn name in double quotes
                                    seat[i] = u.getString("seat");
                                    status[i] = u.getString("status");

                                }
                                grid.setAdapter(new CustomSeat(getApplicationContext(), sid, seat, status));//custom_view_service.xml and li is the listview object


                            } else {
                                Toast.makeText(BusBookActivity.this, "Not found", Toast.LENGTH_SHORT).show();
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
                params.put("scheduleid", getIntent().getStringExtra("scheduleid"));
                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sh.getString("seatcount", "").equalsIgnoreCase("0")){
                    Toast.makeText(BusBookActivity.this, "Select minimum one Seat", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), OnlinePaymentActivity.class).putExtra("amount", Float.toString(am)).putExtra("reqid", getIntent().getStringExtra("scheduleid")));
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                am = Float.parseFloat(sh.getString("seatcount", "0"))* Float.parseFloat(amt);
                amount.setText(Float.toString(am));
                seatCount.setText(sh.getString("seatcount", "0"));

            }
        });



    }
}