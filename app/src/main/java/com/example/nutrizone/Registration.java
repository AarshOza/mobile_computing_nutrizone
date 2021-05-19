package com.example.nutrizone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.nutrizone.firebase.sign_up_email;
import com.google.android.gms.vision.L;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    sign_up_email sign_up_without_email = new sign_up_email();
    private EditText name_register, email_register, password_register;
    private RadioGroup gender_group;
    private RadioButton gender;
    private Button register_me_button;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name_register = (EditText) findViewById(R.id.name_register);
        email_register = (EditText) findViewById(R.id.username_register);
        password_register = (EditText) findViewById(R.id.password_register);
        gender_group = (RadioGroup) findViewById(R.id.gender);


        register_me_button = (Button) findViewById(R.id.register_button);

        register_me_button.setOnClickListener (this);

        Date c = Calendar.getInstance().getTime();
        Log.d("Current time => " , String.valueOf(c));
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formattedDate = df.format(c);

    }

    @Override
    public void onClick(View v) {
        final String[] id = new String[1];
        String register_name, register_email, register_password;
        register_name = name_register.getText().toString().trim();
        register_email = email_register.getText().toString().trim();
        register_password = password_register.getText().toString().trim();
        switch (v.getId()) {
            case R.id.register_button:

                if (register_name.equals("") || register_email.equals("") || register_password.equals("")) {
                    Toast.makeText(this, "Please enter valid credentials to register yourself", Toast.LENGTH_LONG).show();
                }
                else {
                    int selectedId = gender_group.getCheckedRadioButtonId();
                    gender = (RadioButton) findViewById(selectedId);
                    sign_up_without_email.register_email(null, register_name, register_email, register_password, id, formattedDate,(String) gender.getText(),this);
                }
                break;
        }
    }
}