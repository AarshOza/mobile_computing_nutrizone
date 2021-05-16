package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class Home extends AppCompatActivity implements View.OnClickListener, cups_water.cups_water_listener  {

    private Button progress, food_intake, water_intake;
    private TextView calories_value, cups;
    private Slider slider;
    private Image profile;
    public int cups_of_water = 0;
    public Float calories = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getData();
        food_intake = (Button) findViewById(R.id.add_intake);
        water_intake = (Button) findViewById(R.id.water_intake);
        progress = (Button) findViewById(R.id.progress);
        slider = (Slider) findViewById(R.id.slider);
        calories_value = (TextView) findViewById(R.id.calories_value);
        cups = (TextView) findViewById(R.id.cups_value);
        food_intake.setOnClickListener(this);
        water_intake.setOnClickListener(this);
        progress.setOnClickListener(this);

    }

    private void getData() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("LOGIN_DETAILS", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> data = snapshot.getData();

                            for (Map.Entry<String, Object> entry: data.entrySet()) {
                                if (entry.getKey().equals("water")) {
                                    cups_of_water = Integer.parseInt(entry.getValue().toString());
                                    cups.setText(cups_of_water + "/ 15 cups");
                                }
                                if (entry.getKey().equals("calories")) {
                                    calories = Float.parseFloat(entry.getValue().toString());
                                    calories_value.setText(Math.round(calories) + "/ 1650 calories");
                                }
                            }

                        } else {
                            Log.d("LOGIN_DETAILS", "Current data: null");
                        }
                    }
                });
    }

    public void viewProfile(View v)
    {
        startActivity(new Intent(this, Profile.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress:
                slider.setValue(50);
                break;
            case R.id.add_intake:
                startActivity(new Intent(this, AddFoodButtons.class));
                break;
            case R.id.water_intake:
                this.dialog();
                break;
        }
    }

    public void dialog() {
        cups_water cups = new cups_water();
        cups.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void cups_of_water(String cups_water) {
        cups_of_water = cups_of_water + Integer.parseInt(cups_water);
        cups.setText(cups_water + "/15 cups");

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("water", cups_of_water)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("LOGIN_DETAILS", "Document is updated." );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGIN_DETAILS", "Error updating document", e);
                    }
                });
    }
}