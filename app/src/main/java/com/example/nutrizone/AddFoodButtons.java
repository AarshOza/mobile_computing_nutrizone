package com.example.nutrizone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddFoodButtons extends AppCompatActivity implements View.OnClickListener {

    private Button previous_item, scan_new, back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_buttons);

        previous_item = (Button) findViewById(R.id.previous_item);
        scan_new = (Button) findViewById(R.id.scan_new);
        back_button = (Button) findViewById(R.id.back_button);

        previous_item.setOnClickListener(this);
        scan_new.setOnClickListener(this);
        back_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_item:
                startActivity(new Intent(this, PreviousItems.class));
                break;
            case R.id.scan_new:
                startActivity(new Intent(this, Extract.class));
                break;
            case R.id.back_button:
                onBackPressed();
                break;
        }
    }
}