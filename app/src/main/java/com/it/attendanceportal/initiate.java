package com.it.attendanceportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class initiate extends AppCompatActivity {
    private RequestQueue mQueue;
    TextView show_otp;
    fetch generate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mQueue= Volley.newRequestQueue(this);
        Button submit=findViewById(R.id.button2);
        show_otp=findViewById(R.id.textView2);
        SharedPreferences sharedPreference=getSharedPreferences(Sign_In.MyPREFERENCES, Context.MODE_PRIVATE);
        final String t_id = sharedPreference.getString("id", "");
        SharedPreferences sharedPreferences=getSharedPreferences("selection", Context.MODE_PRIVATE);
        //final String semester = sharedPreferences.getString("semester", "");
        //final String student = sharedPreferences.getString("mark_student", "");
        final String subject = sharedPreferences.getString("mark_subject", "");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate =new fetch(t_id,subject);
                generate.execute((Void) null);

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( generate.resotp!=null) {
                    del d = new del(generate.resotp);
                    d.execute((Void) null);
                }
                else finish();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class fetch extends AsyncTask<Void, Void, Boolean> {
        private final String mUrl;
        String resotp;

        fetch( String t_id, String sub_id) {

            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/initiate.php?tid="+ t_id +"&subid="+ sub_id+"";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonobj = response.getJSONObject("status");
                        final String stat,otp;

                        stat = jsonobj.getString("stat");
                        otp = jsonobj.getString("otp");
                        resotp=otp;
                        Toast.makeText(getApplicationContext(),stat,Toast.LENGTH_SHORT).show();
                        show_otp.setText(otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
            return  null;

        }
    }
    @SuppressLint("StaticFieldLeak")
    public class del extends AsyncTask<Void, Void, Boolean> {
        private final String mUrl;
        String resstat;

        del( String otp) {

            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/remove.php?otp="+ otp +"";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonobj = response.getJSONObject("status");
                        final String stat;

                        stat = jsonobj.getString("stat");

                        resstat=stat;
                        Toast.makeText(getApplicationContext(),resstat,Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
            return  null;

        }
    }
}
