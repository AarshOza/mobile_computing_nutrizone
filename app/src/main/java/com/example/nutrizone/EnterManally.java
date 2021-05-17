package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class EnterManally extends AppCompatActivity implements View.OnClickListener{

    private Button submit;
    private EditText product_name, sodium_value, potassium_value, protein_value, fat_value, cholesterol_value, calories_value, carbohydrate_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_manally);

        sodium_value = (EditText) findViewById(R.id.sodium_value);
        potassium_value = (EditText) findViewById(R.id.potassium_value);
        protein_value = (EditText) findViewById(R.id.protein_value);
        fat_value = (EditText) findViewById(R.id.fat_value);
        cholesterol_value = (EditText) findViewById(R.id.cholesterol_value);
        calories_value = (EditText) findViewById(R.id.calories_value);
        carbohydrate_value = (EditText) findViewById(R.id.carbohydrates_value);
        product_name = (EditText) findViewById(R.id.product_value);

        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                this.add_product_to_firebase();
                break;
        }
    }

    private void add_product_to_firebase() {

        Map<String, Object> product = new HashMap<>();
        product.put("calories", calories_value.getText().toString());
        product.put("carbohydrate", carbohydrate_value.getText().toString());
        product.put("cholesterol", cholesterol_value.getText().toString());
        product.put("fat", fat_value.getText().toString());
        product.put("name", product_name.getText().toString());
        product.put("potassium", potassium_value.getText().toString());
        product.put("protein", protein_value.getText().toString());
        product.put("sodium", sodium_value.getText().toString());

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