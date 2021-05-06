package com.example.nutrizone.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nutrizone.Profile;
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
    public void register_email(Context context, String register_name, String register_email, String register_password, String[] id, Registration registration) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(register_email, register_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            id[0] = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(register_name)
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

                            add_user_to_firestore(register_name, register_email, id[0]);
                            String uid = user.getUid();
                            Toast.makeText(registration, "User registered succesfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(registration, Profile.class);
                            context.startActivity(i);
                        }
                        else {
                            Toast.makeText(registration, "User cannot be registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void add_user_to_firestore(String name, String email, String id){
        Log.d("LOGIN_DETAILS", "in firestore function");
        if (name != null && email != null) {

            Map<String, Object> user = new HashMap<>();
            user.put("id", id);
            user.put("DisplayName", name);
            user.put("email", email);

            FirebaseFirestore.getInstance().collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("LOGIN_DETAILS", "DocumentSnapshot added with ID: " + documentReference.getId());
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
