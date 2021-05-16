package com.example.nutrizone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView profile_name, profile_email, profile_gender;
    private Button log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_name = (TextView) findViewById(R.id.name_value);
        profile_email = (TextView) findViewById(R.id.email_value);
        profile_gender = (TextView) findViewById(R.id.gender_value);
        log_out = (Button) findViewById(R.id.log_out_button);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        log_out.setOnClickListener(this);

        if (user != null) {
            // User is signed in
            String[] parts = user.getDisplayName().split("/");
            String name = parts[0];
            String gender = parts[1];
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            profile_name.setText(name);
            profile_email.setText(email);
            profile_gender.setText(gender);

        } else {
            Log.d("LOGIN_DETAILS", "no user");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "You are Logged out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}