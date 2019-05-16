package com.it.attendanceportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Sign_In extends AppCompatActivity {
    private RequestQueue mQueue;
    public static final String MyPREFERENCES = "credential" ;
    private UserLoginTask mAuthTask = null;
    private EditText mEmailView,mPasswordView;
    //View view;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);
        mEmailView=findViewById(R.id.email);
        mPasswordView=findViewById(R.id.password);
        mQueue= Volley.newRequestQueue(this);
        Button mEmailSignInButton =findViewById(R.id.button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public  class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String mUrlString;
        private final String mEmail;
        private final String mPassword;
        Intent i= new Intent(getApplicationContext(), Home.class);



        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;

            mUrlString="https://abhisingh510.000webhostapp.com/eattendpbl/login.php?uname="+mEmail+"&password="+mPassword+"";
        }
        @Override
        protected void onPreExecute() {

            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                Toast.makeText(getApplicationContext(),"no Network",Toast.LENGTH_LONG).show();
                cancel(true);
            }
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            final String[] result = new String[3];
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlString, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("Login");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject login = jsonArray.getJSONObject(i);
                            result[0] = login.getString("id");
                            result[1] = login.getString("username");
                            result[2] = login.getString("password");

                        }
                        mAuthTask = null;
                        //showProgress(false);
                        if (Objects.equals(result[0], "nuull") && !mEmail.equals(result[1])) {
                            mEmailView.setError(getString(R.string.error_invalid_email));
                            mEmailView.requestFocus();
                        } else if (!mPassword.equals(result[2]))  {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();


                        }else {

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("id", result[0]);
                            editor.commit();
                            //i.putExtra("id", result[0]);
                            startActivity(i);
                        }

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
            return null;//.equals("");
        }
        /* @Override
         protected void onPostExecute(final Boolean success) {

         }

         */@Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }

    }

}
