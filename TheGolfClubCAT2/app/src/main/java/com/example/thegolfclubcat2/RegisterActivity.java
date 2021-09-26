package com.example.thegolfclubcat2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.thegolfclubcat2.App.Config;
import com.example.thegolfclubcat2.App.Controller;
import com.example.thegolfclubcat2.helper.SQLiteHandler;
import com.example.thegolfclubcat2.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnReg;
    private Button btnLinkLogin;
    private EditText inputFname;
    private EditText inputLname;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFname = findViewById(R.id.fname);
        inputLname = findViewById(R.id.lname);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.confirm_password);
        btnReg = findViewById(R.id.btnReg);
        btnLinkLogin = findViewById(R.id.btnLinkLogin);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String firstname = inputFname.getText().toString().trim();
                String lastname = inputLname.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirm_password = inputConfirmPassword.getText().toString().trim();
                String empty_details = getString(R.string.empty_details);
                String short_password = getString(R.string.short_password);

                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            empty_details, Toast.LENGTH_LONG)
                            .show();
                } else if (password.length() < 8){
                    Toast.makeText(getApplicationContext(),
                            short_password, Toast.LENGTH_LONG)
                            .show();
                }else {
                    registerUser(firstname, lastname, email, password, confirm_password);
                }
            }
        });

        // Link to Login Screen
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String firstname, final String lastname, final String email,
                              final String password, final String confirm_password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        String creating_account = getString(R.string.creating_account);

        pDialog.setMessage(creating_account);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                   // boolean error = jObj.getBoolean("error");
                    if (jObj.getString("error").equals("false")) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        //String unique_user_id = jObj.getString("unique_user_id");

                        JSONObject user = jObj.getJSONObject("user");
                        String uuid = user.getString("uuid");
                        String firstname = user.getString("fname");
                        String lastname = user.getString("lname");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addUser(uuid, firstname, lastname, email, created_at);

                        String success_signup = getString(R.string.success_signup);

                        Toast.makeText(getApplicationContext(), success_signup, Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String signup_error = getString(R.string.signup_error);
                        //String errorMsg = jObj.getString("error_message");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),
                                signup_error, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", firstname);
                params.put("lname", lastname);
                params.put("email", email);
                params.put("password", password);
                params.put("confirm_password", confirm_password);

                return params;
            }

        };

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}