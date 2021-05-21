package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EnterManally extends AppCompatActivity implements View.OnClickListener{

    private Button submit;
    private EditText product_name, sodium_value, potassium_value, protein_value, fat_value, cholesterol_value, calories_value, carbohydrate_value;
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
        getting_data();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                this.add_product_to_firebase();
                this.add_product_to_user_firebase();
                break;
        }
    }

    private Float nutri(String key){
        if (key.equals("calories")) {
            return Float.valueOf(calories_value.getText().toString());
        }
        if (key.equals("carbohydrate")) {
            return Float.valueOf(carbohydrate_value.getText().toString());
        }
        if (key.equals("cholesterol")) {
            return Float.valueOf(cholesterol_value.getText().toString());
        }
        if (key.equals("fat")) {
            return Float.valueOf(fat_value.getText().toString());
        }
        if (key.equals("potassium")) {
            return Float.valueOf(potassium_value.getText().toString());
        }
        if (key.equals("protein")) {
            return Float.valueOf(protein_value.getText().toString());
        }
        if (key.equals("sodium")) {
            return Float.valueOf(sodium_value.getText().toString());
        }
        else {
            return null;
        }
    }


    private void add_product_to_user_firebase(){

        Map<String, Float> product_details = new HashMap<String, Float>();
        product_details.put("calories",calories_v );
        product_details.put("carbohydrate",carbohydrate_v );
        product_details.put("cholesterol", cholesterol_v);
        product_details.put("fat", fat_v);
        product_details.put("potassium", potassium_v);
        product_details.put("protein", protein_v);
        product_details.put("sodium", sodium_v);


        for (Map.Entry<String, Float> entry : product_details.entrySet()) {
//here
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