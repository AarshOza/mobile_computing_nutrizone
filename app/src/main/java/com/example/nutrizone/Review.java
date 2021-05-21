package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Review extends AppCompatActivity implements View.OnClickListener{

    private TextView sodium, potassium, protein, fat, cholesterol, calories, carbohydrate;
    private Button submit, rescan, manually_entry;
    private EditText product_name;
    private Map<String, Float> products;
    Float calories_v=0f;
    Float carbohydrate_v=0f;
    Float cholesterol_v=0f;
    Float fat_v=0f;
    Float potassium_v=0f;
    Float protein_v=0f;
    Float sodium_v=0f;

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
        getting_data();

        Map<String, Float>product_details = (Map<String, Float>) getIntent().getSerializableExtra("product_details");
        products = product_details;

        for (Map.Entry<String, Float> entry : product_details.entrySet()) {
            this.setData(entry);
        }
    }

    private void getting_data() {
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
                            Log.d("LOGIN_DETAILS", "val calories ==> entry "+data);

                            for (Map.Entry<String, Object> entry: data.entrySet()) {
                                if (entry.getKey().equals("calories")) {
                                    Log.d("LOGIN_DETAILS", "val calories ==> entry "+entry.getKey() + "and the value of user is ==> " + entry.getValue());
                                    calories_v = Float.parseFloat((String) entry.getValue());
                                }
                                if (entry.getKey().equals("carbohydrate")) {
                                    carbohydrate_v = Float.parseFloat((String) entry.getValue());
                                }
                                if (entry.getKey().equals("cholesterol")) {
                                    cholesterol_v = Float.parseFloat((String) entry.getValue());
                                }
                                if (entry.getKey().equals("fat")) {
                                    fat_v = Float.parseFloat((String) entry.getValue());
                                }
                                if (entry.getKey().equals("potassium")) {
                                    potassium_v = Float.parseFloat((String) entry.getValue());
                                }
                                if (entry.getKey().equals("protein")) {
                                    protein_v = Float.parseFloat((String) entry.getValue());
                                }
                                if (entry.getKey().equals("sodium")) {
                                    sodium_v = Float.parseFloat((String) entry.getValue());
                                }
                            }

                        } else {
                            Log.d("LOGIN_DETAILS", "Current data: null");
                        }
                    }
                });
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
                if (product_name.getText().toString().trim().matches("")) {
                    Toast.makeText(this, "Please enter the product name", Toast.LENGTH_LONG).show();
                }
                else {
                    this.add_product_to_firebase();
                    this.add_product_to_user_firebase(products);
                }
                break;
            case R.id.rescan:
                startActivity(new Intent(this, Extract.class));
                break;
            case R.id.manually:
                startActivity(new Intent(this, EnterManally.class));
                break;
        }
    }
    private Float nutri(String key){
        if (key.equals("calories")) {
            return calories_v;
        }
        if (key.equals("carbohydrate")) {
            return carbohydrate_v;
        }
        if (key.equals("cholesterol")) {
            return cholesterol_v;
        }
        if (key.equals("fat")) {
            return fat_v;
        }
        if (key.equals("potassium")) {
            return potassium_v;
        }
        if (key.equals("protein")) {
            return protein_v;
        }
        if (key.equals("sodium")) {
            return sodium_v;
        }
        else {
            return null;
        }
    }


    private void add_product_to_user_firebase(Map<String, Float> products){

        for (Map.Entry<String, Float> entry : products.entrySet()) {

            StringBuilder sb = new StringBuilder();
            float val = nutri(entry.getKey()) + (entry.getValue());
            sb.append(val);
            String s = sb.toString();
            Log.d("LOGIN_DETAILS", "s is " + s);

            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .update(entry.getKey(), s)
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

//            if (entry.getKey().equals("sodium")) {
//
//            }
//            if (entry.getKey().equals("potassium")) {
//
//            }
//            if (entry.getKey().equals("protein")) {
//
//            }
//            if (entry.getKey().equals("fat")) {
//
//            }
//            if (entry.getKey().equals("cholesterol")) {
//
//            }
//            if (entry.getKey().equals("calories")) {
//
//            }
//            if (entry.getKey().equals("carbohydrate")) {
//
//            }
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
                        submit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGIN_DETAILS", "Error adding document", e);
                    }
                });
    }

    private void submit() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}