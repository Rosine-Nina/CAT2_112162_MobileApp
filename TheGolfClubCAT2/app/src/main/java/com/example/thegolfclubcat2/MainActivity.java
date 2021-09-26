package com.example.thegolfclubcat2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thegolfclubcat2.helper.SQLiteHandler;
import com.example.thegolfclubcat2.helper.SessionManager;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView txtFname;
    private TextView txtLname;
    private TextView txtEmail;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;
    private Button btnClubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtFname = findViewById(R.id.fname);
        txtLname = findViewById(R.id.lname);
        txtEmail = findViewById(R.id.email);
        btnClubs = findViewById(R.id.btnClubs);
        btnLogout = findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String fname = user.get("fname");
        String lname = user.get("lname");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtFname.setText(fname);
        txtLname.setText(lname);
        txtEmail.setText(email);

        btnClubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ClubActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}