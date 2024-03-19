package com.app.timewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sh;
    ImageView loc;
    Boolean eyeClosed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usrnm = findViewById(R.id.editTextTextEmailAddress);
        EditText passw = findViewById(R.id.editTextTextPassword);
        Button lbtn = findViewById(R.id.button2);
        TextView tv = findViewById(R.id.textView3);
        loc = findViewById(R.id.imageView6);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eyeClosed){
                    loc.setImageResource(R.drawable.ic_eyeclosed);
                    passw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passw.setText(passw.getText().toString());
                    passw.setSelection(passw.getText().length());
                }
                else {
                    passw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passw.setText(passw.getText().toString());
                    loc.setImageResource(R.drawable.ic_eyeopen);
                    passw.setSelection(passw.getText().length());
                }
                eyeClosed = !eyeClosed;
            }
        });
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sh.getString("lid", "").isEmpty()){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
        }
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usrnm.getText().toString();
                String password = passw.getText().toString();
                if (username.equalsIgnoreCase("")){
                    usrnm.setError("Enter username");
                    return;
                }
                if (password.equalsIgnoreCase("")){
                    passw.setError("Enter username");
                    return;
                }

                String url = sh.getString("url", "")+"/andlogin";
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        SharedPreferences.Editor editor= sh.edit();
                                        editor.putString("lid", jsonObj.getString("lid"));
                                        editor.putString("name", jsonObj.getString("name"));
                                        editor.putString("email", jsonObj.getString("email"));
                                        editor.putString("type", jsonObj.getString("type"));
                                        if (jsonObj.getString("type").equalsIgnoreCase("user")){
                                            editor.putString("profilepic", jsonObj.getString("profilepic"));
                                            editor.putString("logincount", "no");
                                            editor.commit();
                                            Intent i = new Intent(getApplicationContext(),HomeUserActivity.class);
                                            startActivity(i);
                                        }
                                        if (jsonObj.getString("type").equalsIgnoreCase("driver")){
                                            editor.putString("busid", jsonObj.getString("busid"));
                                            if (jsonObj.getString("busstatus").equalsIgnoreCase("Yes")){

                                                Toast.makeText(LoginActivity.this, "Bus got blocked by Admin, Please contact Owner!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            else {
                                                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                                editor.putString("logincount", "no");
                                                editor.commit();
                                                startActivity(i);
                                            }

                                        }
                                        finish();
                                        overridePendingTransition(0,0);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Email or Password is Incorrect, Try again", Toast.LENGTH_LONG).show();

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
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Map<String, String> params = new HashMap<String, String>();

                    params.put("username", username);//passing to python
                    params.put("password", password);
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

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}