package com.example.nutrizone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nutrizone.firebase.google_sign_in;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.example.nutrizone.firebase.log_in_email;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAnalytics mFirebaseAnalytics;
    private EditText username_text, password_text;
    private Button login_button, register_button;
    private SignInButton GoogleSignIn;
    log_in_email log_in_with_email = new log_in_email();
    google_sign_in sign_in_with_google = new google_sign_in(this);
    private GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        username_text = (EditText) findViewById(R.id.username);
        password_text = (EditText) findViewById(R.id.password);

        login_button = (Button) findViewById(R.id.login);
        register_button = (Button) findViewById(R.id.register);
//        GoogleSignIn = (SignInButton) findViewById(R.id.GoogleSignInButton);

        sign_in_with_google.configureSignIn();

        login_button.setOnClickListener (this);
        register_button.setOnClickListener(this);
//        GoogleSignIn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String user_email, user_password;
        user_email = username_text.getText().toString().trim();
        user_password = password_text.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login:
                log_in_with_email.log_in_email(null, user_email, user_password, this);
                break;
            case R.id.register:
                startActivity(new Intent(this, Registration.class));
                break;
//            case R.id.GoogleSignInButton:
//                Log.d("LOGIN_DETAILS", "logging in google");
//                sign_in_with_google.signInToGoogle(null, this);
//                Intent signInIntent = googleClient.getSignInIntent();
//                Log.d("LOGIN_DETAILS", "intent" + signInIntent.toString());
//                startActivityForResult(signInIntent, 1);
//
//                break;
        }
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("LOGIN_DETAILS", "in onActivityResult()");
//        if (requestCode == 1) {
//            Log.d("LOGIN_DETAILS", data.getExtras().toString());
////            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
////            sign_in_with_google.handleSignIn(task);
//        }
    }
