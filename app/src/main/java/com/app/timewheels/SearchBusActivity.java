package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.HashMap;
import java.util.Map;

public class SearchBusActivity extends AppCompatActivity {
    ListView ls;
    String[] busid, busname, delay, runningStatus;
    SharedPreferences sh;
    ProgressDialog pd;
    EditText edfrom, edto;
    ImageButton btn;
    String routeid;
    String routename;
    ArrayAdapter<String> adapter;
    TextView tv, textView;
    ImageView imgv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);
        ls = findViewById(R.id.listViewSearch);
        edfrom = findViewById(R.id.editTextTextPersonName2);
        edto = findViewById(R.id.editTextTextPersonName3);
        btn = findViewById(R.id.imageButton2);
        tv = findViewById(R.id.textView17);
        textView = findViewById(R.id.textView11);
        imgv = findViewById(R.id.imageView8);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edfrom.getText().toString().isEmpty()){
                    edfrom.setError("Enter from");
                    return;
                }
                if (edto.getText().toString().isEmpty()){
                    edto.setError("Enter to");
                    return;
                }
                searchBus(edfrom.getText().toString(), edto.getText().toString());

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

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent j = new Intent(getApplicationContext(), BusActivity.class);
                j.putExtra("busid", busid[i]);
                j.putExtra("routeid", routeid);
                j.putExtra("routename", routename);
                j.putExtra("running", runningStatus[i]);
                startActivity(j);
            }
        });
    }
    public void searchBus(String qry1, String qry2){

        String url = sh.getString("url", "")+"/buslist";
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

                                routeid = jsonObj.getString("routeid");
                                routename = jsonObj.getString("routename");
                                textView.setText(routename);
                                textView.setVisibility(View.VISIBLE);
                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                busid = new String[js.length()];
                                busname = new String[js.length()];
                                delay = new String[js.length()];
                                runningStatus = new String[js.length()];
                                if (js.length() == 0){
                                    ls.setVisibility(View.GONE);
                                    tv.setText("No Buses are available");
                                    tv.setVisibility(View.VISIBLE);
                                    Toast.makeText(SearchBusActivity.this, "No Buses are available", Toast.LENGTH_SHORT).show();
                                }


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    busid[i] = u.getString("id");//dbcolumn name in double quotes
                                    busname[i] = u.getString("name");
                                    delay[i] = u.getString("delay");
                                    runningStatus[i]= u.getString("running");


                                }
                                ls.setVisibility(View.INVISIBLE);
                                tv.setVisibility(View.GONE);
                                ls.setVisibility(View.VISIBLE);
                                ls.setAdapter(new CustomSearchView(getApplicationContext(), busid, busname, delay, runningStatus));//custom_view_service.xml and li is the listview object


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