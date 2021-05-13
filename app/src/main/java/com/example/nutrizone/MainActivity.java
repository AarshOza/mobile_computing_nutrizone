package com.example.nutrizone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {


    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    final Handler handler = new Handler();

    @Override
    protected void onStart() {
        super.onStart();
            handler.postDelayed(r, 1000);
    }

    final Runnable r = new Runnable() {
        public void run() {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null){
                Log.d("LOGIN_DETAILS", String.valueOf(user));
                startActivity(new Intent(MainActivity.this, Home.class));
            }
            else{
                startActivity(new Intent(MainActivity.this, Login.class));
                Log.d("LOGIN_DETAILS", "no users");
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }
}
