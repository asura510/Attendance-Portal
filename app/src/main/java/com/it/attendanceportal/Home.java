package com.it.attendanceportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private userdata udata;
    private fetch branch,subject,sem,student;
    private RequestQueue mQueue;
    ArrayAdapter adapter;
    ListView listView;
    SharedPreferences sharedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences=getSharedPreferences(Sign_In.MyPREFERENCES, Context.MODE_PRIVATE);
        sharedata= getSharedPreferences("selection", Context.MODE_PRIVATE);
        final String id = sharedPreferences.getString("id", "");
        mQueue= Volley.newRequestQueue(this);
        assert id != null;
        {
            udata = new userdata(id);
            udata.execute((Void) null);

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, id+"Replace with your own action "+udata.array[0], Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_attend) {


            branch=new fetch("branch",0);
            branch.execute((Void) null);
            listView =findViewById(R.id.branch_list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    sem=new fetch("semester",0);
                    sem.execute((Void) null);
                    listView =findViewById(R.id.branch_list);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            student=new fetch("student",2,"add_year",sem.resid[position],"semester","add_year");
                            SharedPreferences.Editor editor = sharedata.edit();
                            editor.putString("semester", sem.resid[position]);
                            editor.commit();
                            student.execute((Void) null);
                            listView =findViewById(R.id.branch_list);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Intent i=new Intent(getApplicationContext(),Attendance_view.class);
                                    SharedPreferences.Editor editor = sharedata.edit();
                                    editor.putString("student", student.resid[position]);
                                    editor.commit();
                                    startActivity(i);

                                }
                            });
                        }
                    });
                }
            });

        } else if (id == R.id.nav_initiate) {
            branch=new fetch("branch",0);
            branch.execute((Void) null);
            listView =findViewById(R.id.branch_list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    sem=new fetch("semester",0);
                    sem.execute((Void) null);
                    listView =findViewById(R.id.branch_list);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            subject = new fetch("subject", 1, "semester", sem.resid[position]);
                            subject.execute((Void) null);
                            SharedPreferences.Editor editor = sharedata.edit();
                            editor.putString("semester", sem.resid[position]);
                            editor.commit();
                            listView =findViewById(R.id.branch_list);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                    Intent i=new Intent(getApplicationContext(),initiate.class);
                                    SharedPreferences.Editor editor = sharedata.edit();
                                    editor.putString("mark_subject",subject.resid[position]);
                                    editor.commit();
                                    startActivity(i);

                                }
                            });
                        }
                    });
                }
            });




        } else if (id == R.id.nav_mark) {

            branch=new fetch("branch",0);
            branch.execute((Void) null);
            listView =findViewById(R.id.branch_list);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    sem=new fetch("semester",0);
                    sem.execute((Void) null);
                    listView =findViewById(R.id.branch_list);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            subject = new fetch("subject", 1, "semester", sem.resid[position]);
                            subject.execute((Void) null);
                            SharedPreferences.Editor editor = sharedata.edit();
                            editor.putString("semester", sem.resid[position]);
                            editor.commit();
                            listView =findViewById(R.id.branch_list);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Intent i=new Intent(getApplicationContext(),student_list.class);
                                    SharedPreferences.Editor editor = sharedata.edit();
                                    editor.putString("mark_subject",subject.resid[position]);
                                    editor.commit();
                                    startActivity(i);

                                }
                            });
                        }
                    });
                }
            });



        } else if (id == R.id.nav_logout) {

            finish();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @SuppressLint("StaticFieldLeak")
    public class userdata extends AsyncTask<Void, Void, Boolean> {
        private final String mUrl;
        TextView tx,ty;
        String[]  array;


        userdata(String id) {
            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/tdata.php?uid=" + id + "";
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            final String[] result = new String[4];
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("Data");
                        for (int i = 0; i < 1; i++) {
                            JSONObject login = jsonArray.getJSONObject(i);
                            result[0] = login.getString("name");
                            result[1] = login.getString("contact");
                            result[2] = login.getString("email");
                            result[3] = login.getString("dept");
                        }
                        tx=findViewById(R.id.email);
                        ty=findViewById(R.id.user_name);
                        array = result;
                        tx.setText(result[2]);
                        ty.setText(result[0]);

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
    public class fetch extends AsyncTask<Void, Void, Boolean> {
        private final String mUrl,tab;
        String[] resname;
        String[] resid;
        fetch(String tab, int casee) {
            this.tab=tab;
            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/fetch.php?tab="+ tab +"&case="+ casee+"";
        }
        fetch(String tab, int casee, String attr,String val) {
            this.tab=tab;
            mUrl = "https://abhisingh510.000webhostapp.com/eattendpbl/fetch.php?tab="+ tab +"&case="+ casee+"&attr="+ attr+"&val="+ val+"";
        }
        fetch(String tab, int casee, String attr,String val,String ref, String rattr) {
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
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.activity_listview, resname);
                        listView =findViewById(R.id.branch_list);
                        listView.setAdapter(adapter);
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
