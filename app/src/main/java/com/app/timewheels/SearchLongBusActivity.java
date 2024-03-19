package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

public class SearchLongBusActivity extends AppCompatActivity {
    ImageView imgv;
    EditText edfrom, edto, edate;
    ImageButton btn;
    ProgressDialog pd;
    SharedPreferences sh;
    ListView ls;
    TextView tv, textView;
    String[] id, name, start, end, amount;
    String routename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_long_bus);

        edfrom = findViewById(R.id.editTextTextPersonName2);
        edto = findViewById(R.id.editTextTextPersonName3);
        imgv = findViewById(R.id.imageView8);
        btn = findViewById(R.id.imageButton2);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ls = findViewById(R.id.listViewSearch);
        tv = findViewById(R.id.textView17);
        textView = findViewById(R.id.textView11);
        edate = findViewById(R.id.editTextDate2);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int  mDay = c.get(Calendar.DAY_OF_MONTH);
        int a=mMonth+1;

        String mm=String.valueOf(a);
        String mn="0"+mm;

        if (mm.length()==1){

            edate.setText(mDay + "-" + mn + "-" + mYear);
        }
        else {

            edate.setText(mDay + "-" + mm + "-" + mYear);
        }

        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchLongBusActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int a=monthOfYear+1;

                                String mm=String.valueOf(a);
                                String mn="0"+mm;
                                String md = String.valueOf(dayOfMonth);
                                String day;
                                if (md.length()==1){
                                    day = "0"+md;
                                }
                                else {
                                    day = md;
                                }
                                if (mm.length()==1){

                                    edate.setText(day + "-" + mn + "-" + year);
                                }
                                else {

                                    edate.setText(day + "-" + mm + "-" + year);
                                }
                                edate.setError(null);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000*60*60*24*4);
                datePickerDialog.show();
            }
        });

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = edfrom.getText().toString();
                edfrom.setText(edto.getText().toString());
                edto.setText(temp);
                edfrom.setSelection(edfrom.length());
                edto.setSelection(edto.length());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tfrom = edfrom.getText().toString();
                String tto = edto.getText().toString();
                String tdate = edate.getText().toString();
                if (tfrom.isEmpty()){
                    edfrom.setError("Enter from");
                    return;
                }
                if (tto.isEmpty()){
                    edto.setError("Enter to");
                    return;
                }
                if (tdate.isEmpty()){
                    edate.setError("Enter Date");
                    return;
                }
                searchBus(tfrom, tto, tdate);

            }
        });

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), BusBookActivity.class).putExtra("scheduleid", id[i]));
            }
        });
    }
    public void searchBus(String qry1, String qry2, String qry3){

        String url = sh.getString("url", "")+"/long_buslist";
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

                                routename = jsonObj.getString("routename");
                                textView.setText(routename);
                                textView.setVisibility(View.VISIBLE);

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                id = new String[js.length()];
                                name = new String[js.length()];
                                start = new String[js.length()];
                                end = new String[js.length()];
                                amount = new String[js.length()];
                                if (js.length() == 0){
                                    ls.setVisibility(View.GONE);
                                    tv.setText("No Buses are available");
                                    tv.setVisibility(View.VISIBLE);
                                    Toast.makeText(SearchLongBusActivity.this, "No Buses are available", Toast.LENGTH_SHORT).show();
                                }


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    id[i] = u.getString("id");//dbcolumn name in double quotes
                                    name[i] = u.getString("name");
                                    start[i] = u.getString("start");
                                    end[i]= u.getString("end");
                                    amount[i]= u.getString("amount");


                                }
                                ls.setVisibility(View.INVISIBLE);
                                tv.setVisibility(View.GONE);
                                ls.setVisibility(View.VISIBLE);
                                ls.setAdapter(new CustomLongBusSearchView(getApplicationContext(), name, start, end, amount));//custom_view_service.xml and li is the listview object


                            } else {
                                ls.setVisibility(View.GONE);
                                tv.setText("Route is incorrect");
                                tv.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.INVISIBLE);

//                                ls.setAdapter(new CustomSearchView(getApplicationContext(), busid, busname, delay, runningStatus));
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
                params.put("from", qry1);
                params.put("to", qry2);
                params.put("date", qry3);
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
}