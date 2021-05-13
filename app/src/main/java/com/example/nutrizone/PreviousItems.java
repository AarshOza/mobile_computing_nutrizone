package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreviousItems extends AppCompatActivity implements View.OnClickListener {

    private Button back_button;
    LinearLayout linear_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_items);

        back_button = (Button) findViewById(R.id.back_button);
        linear_view_container = (LinearLayout) findViewById(R.id.linear_container);

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
                        Log.d("LOGIN_DETAILS", "here is "+ data.getClass());
                    }
                } else {
                    Log.d("LOGIN_DETAILS", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    private void show_products(Map<String, Object> data) {

        Log.d("LOGIN_DETAILS","called show_products");
        for (Map.Entry<String,Object> entry : data.entrySet()){
            Log.d("LOGIN_DETAILS","Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }


            LinearLayout parent = new LinearLayout(this);
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);
            TextView product =new TextView(this);
            Button plus = new Button(this);
            TextView counter_value = new TextView(this);
            Button minus = new Button(this);
            int dp_val = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, this.getResources().getDisplayMetrics());

            product.setLayoutParams(new ViewGroup.LayoutParams(dp_val, dp_val));
            product.setGravity(Gravity.START);
            product.setText("apple");


            minus.setLayoutParams(new ViewGroup.LayoutParams(dp_val, dp_val));
            minus.setText("-");
            minus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            minus.setBackgroundColor(0xFF6200EE);
            minus.setTextColor(Color.parseColor("#FFFFFF"));

            counter_value.setId(R.id.counter_value);
            counter_value.setLayoutParams(new ViewGroup.LayoutParams(dp_val, dp_val));
            counter_value.setGravity(Gravity.CENTER);
            counter_value.setText("4");

            plus.setLayoutParams(new ViewGroup.LayoutParams(dp_val, dp_val));
            plus.setText("+");
            plus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            plus.setBackgroundColor(0xFF6200EE);
            plus.setTextColor(Color.parseColor("#FFFFFF"));

            parent.addView(product);
            parent.addView(minus);
            parent.addView(counter_value);
            parent.addView(plus);

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counter_value.setText("0");
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counter_value.setText("1");
                }
            });

            parent.setBackground(Drawable.createFromPath("@drawable/border"));
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