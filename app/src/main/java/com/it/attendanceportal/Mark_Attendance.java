package com.it.attendanceportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Mark_Attendance extends AppCompatActivity {
    private RequestQueue mQueue;
    //ArrayAdapter adapter;

    fetch mark;

    Button submit,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark__attendance);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submit=findViewById(R.id.submit);
        exit=findViewById(R.id.exit);
        mQueue= Volley.newRequestQueue(this);
        SharedPreferences sharedPreference=getSharedPreferences(Sign_In.MyPREFERENCES, Context.MODE_PRIVATE);
        final String t_id = sharedPreference.getString("id", "");
        SharedPreferences sharedPreferences=getSharedPreferences("selection", Context.MODE_PRIVATE);
        final String semester = sharedPreferences.getString("semester", "");
        final String student = sharedPreferences.getString("mark_student", "");
        final String subject = sharedPreferences.getString("mark_subject", "");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mark =new fetch(t_id,student,subject);
                mark.execute((Void) null);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class fetch extends AsyncTask<Void, Void, Boolean> {
        private final String mUrl;
        String resstat;

        fetch( String t_id,String s_id, String sub_id) {

            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/mark.php?tid="+ t_id +"&sid="+s_id+"&subid="+ sub_id+"";
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
