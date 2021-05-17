package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Review extends AppCompatActivity implements View.OnClickListener{

    private TextView sodium, potassium, protein, fat, cholesterol, calories, carbohydrate,
            sodium_value, potassium_value, protein_value, fat_value, cholesterol_value, calories_value, carbohydrate_value;
    private Button submit, rescan, manually_entry;
    private EditText product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        sodium = (TextView) findViewById(R.id.sodium_value);
        potassium = (TextView) findViewById(R.id.potassium_value);
        protein = (TextView) findViewById(R.id.protein_value);
        fat = (TextView) findViewById(R.id.fat_value);
        cholesterol = (TextView) findViewById(R.id.cholesterol_value);
        calories = (TextView) findViewById(R.id.calories_value);
        carbohydrate = (TextView) findViewById(R.id.carbohydrates_value);

        product_name = (EditText) findViewById(R.id.product_value);

        submit = (Button) findViewById(R.id.submit);
        rescan = (Button) findViewById(R.id.rescan);
        manually_entry = (Button) findViewById(R.id.manually);

        submit.setOnClickListener(this);
        rescan.setOnClickListener(this);
        manually_entry.setOnClickListener(this);

        Map<String, Float>product_details = (Map<String, Float>) getIntent().getSerializableExtra("product_details");

        for (Map.Entry<String, Float> entry : product_details.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
            this.setData(entry);
        }
    }

    private void setData(Map.Entry<String, Float> entry) {
        if (entry.getKey().equals("sodium")) {
            Log.d("LOGIN_DETAILS", "val calories ==> entry "+entry.getKey() + "and the value of user is ==> " + entry.getValue());
            sodium.setText(entry.getValue().toString());
        }
        if (entry.getKey().equals("potassium")) {
            potassium.setText(entry.getValue().toString());
        }
        if (entry.getKey().equals("protein")) {
            protein.setText(entry.getValue().toString());
        }
        if (entry.getKey().equals("fat")) {
            fat.setText(entry.getValue().toString());
        }
        if (entry.getKey().equals("cholesterol")) {
            cholesterol.setText(entry.getValue().toString());
        }
        if (entry.getKey().equals("calories")) {
            calories.setText(entry.getValue().toString());
        }
        if (entry.getKey().equals("carbohydrate")) {
            carbohydrate.setText(entry.getValue().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                this.add_product_to_firebase();
                break;
            case R.id.rescan:
                startActivity(new Intent(this, Extract.class));
                break;
            case R.id.manually:
                break;
        }
    }

    private void add_product_to_firebase() {

        Map<String, Object> product = new HashMap<>();
        product.put("calories", calories.getText().toString());
        product.put("carbohydrate", carbohydrate.getText().toString());
        product.put("cholesterol", cholesterol.getText().toString());
        product.put("fat", fat.getText().toString());
        product.put("name", product_name.getText().toString());
        product.put("potassium", potassium.getText().toString());
        product.put("protein", protein.getText().toString());
        product.put("sodium", sodium.getText().toString());

        FirebaseFirestore.getInstance().collection("products")
                .document(product_name.getText().toString())
                .set(product)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d("LOGIN_DETAILS", "DocumentSnapshot added with ID: " + o);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGIN_DETAILS", "Error adding document", e);
                    }
                });
    }
}