package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreviousItems extends AppCompatActivity implements View.OnClickListener {

    private Button back_button;
    LinearLayout linear_view_container;
    private EditText search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_items);

        back_button = (Button) findViewById(R.id.back_button);
        linear_view_container = (LinearLayout) findViewById(R.id.linear_container);
        search_view = (EditText) findViewById(R.id.search_bar);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("LOGIN_DETAILS","text is " + search_view.getText());
                if(search_view.getText().toString().matches("")) {
                    linear_view_container.removeAllViews();
                    get_products();
                }
                if(!search_view.getText().equals("")) {
                    try {
                        FirebaseFirestore.getInstance()
                                .collection("products")
                                .whereEqualTo("__name__", search_view.getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, Object> data = document.getData();
                                                linear_view_container.removeAllViews();
                                                show_products(data);
                                            }
                                        } else {
                                            Log.d("LOGIN_DETAILS", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        search_view.addTextChangedListener(tw);

        this.get_products();

        back_button.setOnClickListener(this);
    }


    private void get_products() {
        Log.d("LOGIN_DETAILS", "in getProducts");

        FirebaseFirestore.getInstance()
                .collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                show_products(data);
                            }
                        } else {
                            Log.d("LOGIN_DETAILS", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void show_products(Map<String, Object> data) {
        String product_name = null;

        for (Map.Entry<String,Object> entry : data.entrySet()){
            if (entry.getKey().equals("name")){
                product_name = (String) entry.getValue();
            }
        }


        LinearLayout parent = new LinearLayout(this);
        int sp_val = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, this.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(sp_val, sp_val, sp_val, sp_val);
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.HORIZONTAL);
//        parent.setGravity(Gravity.CENTER);
        parent.setBackgroundResource(R.drawable.border);

        TextView product =new TextView(this);
        Button add_button = new Button(this);
        int dp_val = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50, this.getResources().getDisplayMetrics());

        int sp_val_1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(2*dp_val, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params1.setMargins(0, 0, sp_val_1-10, 0);
        product.setLayoutParams(params1);
        product.setText(product_name);
        product.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(dp_val-10, dp_val-10, 1);
        add_button.setLayoutParams(params2);
        add_button.setText("Add");
        add_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        add_button.setBackgroundColor(0xFF6200EE);
        add_button.setTextColor(Color.parseColor("#FFFFFF"));


        parent.addView(product);
        parent.addView(add_button);


        String finalProduct_name = product_name;
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products products_dialog = new products( data, finalProduct_name);
                products_dialog.show(getSupportFragmentManager(), "example dialog");
            }
        });

        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, this.getResources().getDisplayMetrics());

        parent.setPadding(value, value, value, value);

        linear_view_container.addView(parent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
        }
    }
}