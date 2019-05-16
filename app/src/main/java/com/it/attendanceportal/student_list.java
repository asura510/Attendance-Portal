package com.it.attendanceportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class student_list extends AppCompatActivity {
    private RequestQueue mQueue;
    ArrayAdapter adapter;
    fetch student;
    ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView1 =findViewById(R.id.student_list);
        mQueue= Volley.newRequestQueue(this);
        final SharedPreferences sharedPreferences=getSharedPreferences("selection", Context.MODE_PRIVATE);
        final String semester = sharedPreferences.getString("semester", "");
        student=new fetch("student",2,"add_year",semester,"semester","add_year");
        student.execute((Void) null);
        FloatingActionButton fab = findViewById(R.id.fab);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i=new Intent(getApplicationContext(),Mark_Attendance.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mark_student", student.resid[position]);
                editor.commit();
                startActivity(i);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class fetch extends AsyncTask<Void, Void, Boolean> {
        private final String mUrl,tab;
        String[] resname;
        String[] resid;

        fetch(String tab, int casee, String attr,String val,String ref,String rattr) {
            this.tab=tab;
            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/fetch.php?tab="+ tab +"&case="+ casee+"&attr="+ attr+"&val="+ val+"&ref="+ ref+"&rattr="+ rattr+"";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray(tab);
                        final String[] id = new String[jsonArray.length()];
                        final String[] name = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject br = jsonArray.getJSONObject(i);
                            id[i] = br.getString("id");
                            name[i] = br.getString("name");
                        }
                        resname=name;
                        resid=id;
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.activity_listview,resname);
                        listView1.setAdapter(adapter);

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
