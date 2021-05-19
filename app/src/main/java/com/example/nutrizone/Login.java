package com.example.nutrizone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nutrizone.firebase.log_in_email;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    private EditText username_text, password_text;
    private Button login_button, register_button;
    log_in_email log_in_with_email = new log_in_email();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_text = (EditText) findViewById(R.id.username);
        password_text = (EditText) findViewById(R.id.password);

        login_button = (Button) findViewById(R.id.login);
        register_button = (Button) findViewById(R.id.register);

        login_button.setOnClickListener (this);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user_email, user_password;
        user_email = username_text.getText().toString().trim();
        user_password = password_text.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login:
                if (user_email.equals("") || user_password.equals("")) {
                    Toast.makeText(this, "Please enter valid login credentials", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("LOGIN_DETAILS","detail is " + user_email +  " and " + user_password);
                    log_in_with_email.log_in_email(null, user_email, user_password, this);
                }
                break;
            case R.id.register:
                startActivity(new Intent(this, Registration.class));
                break;
        }
    }
}