package com.example.nutrizone.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nutrizone.Home;
import com.example.nutrizone.Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

public class sign_up_email {
    public void register_email(Context context, String register_name, String register_email, String register_password, String[] id, String formattedDate, String gender, Registration registration) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(register_email, register_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            id[0] = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(register_name + " /"+ gender)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("LOGIN_DETAILS", "User profile updated.");
                                            }
                                        }
                                    });

                            add_user_to_firestore(register_name, register_email, id[0], gender, formattedDate);
                            String uid = user.getUid();
                            Toast.makeText(registration, "User registered successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(registration, Home.class);
                            registration.startActivity(i);
                        }
                        else {
                            Toast.makeText(registration, "Registration Failed:Please enter correct email address", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void add_user_to_firestore(String name, String email, String id, String gender, String formattedDate){
        if (name != null && email != null) {

            Map<String, Object> user = new HashMap<>();
            user.put("id", id);
            user.put("DisplayName", name);
            user.put("email", email);
            user.put("gender", gender);
            user.put("calories", "0");
            user.put("carbohydrate", "0");
            user.put("cholesterol", "0");
            user.put("fat", "0");
            user.put("potassium", "0");
            user.put("protein", "0");
            user.put("sodium", "0");
            user.put("water", "0");
            user.put("date", formattedDate);


            FirebaseFirestore.getInstance().collection("users")
                    .document(id)
                    .set(user)
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

}
