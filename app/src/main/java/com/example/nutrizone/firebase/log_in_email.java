package com.example.nutrizone.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nutrizone.Home;
import com.example.nutrizone.Login;
import com.example.nutrizone.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in_email {
    public void log_in_email(Context context, String user_email, String user_password, Login mainActivity) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(user_email, user_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOGIN_DETAILS", "logged in");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            mainActivity.startActivity(new Intent(mainActivity, Home.class));
                            Toast.makeText(mainActivity, "User logged in succesfully", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.d("LOGIN_DETAILS", "not logged in");
                            Toast.makeText(mainActivity, "User cannot be login", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
