package com.example.nutrizone;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class products extends AppCompatDialogFragment {
    private Button minus,plus;
    private TextView counter_text;
    int counter=0;
    public String Product_name;
    public Map<String, Object> data_result;
    Float calories_v=0f;
    Float carbohydrate_v=0f;
    Float cholesterol_v=0f;
    Float fat_v=0f;
    Float potassium_v=0f;
    Float protein_v=0f;
    Float sodium_v=0f;

    public products(Map<String, Object> data, String finalProduct_name) {
        Product_name = finalProduct_name;
        data_result = data;
        getting_data();
    }

    public void getting_data() {
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
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cups_water, null);

        builder.setView(view)
                .setTitle("How many cups of "+Product_name+" ?")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        calculate();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

        minus = view.findViewById(R.id.minus_button);
        plus = view.findViewById(R.id.plus_button);
        counter_text = view.findViewById(R.id.counter_value);
        counter_text.setText(toString().valueOf(counter));

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 0) {
                    counter --;
                    counter_text.setText(toString().valueOf(counter));
                }
                else {
                    Toast.makeText(getActivity(), "You cannot eat less than 0 quantity!!", Toast.LENGTH_LONG).show();
                }

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter ++;
                counter_text.setText(toString().valueOf(counter));
            }
        });

        return builder.create();
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

    private void calculate() {
        for (Map.Entry<String,Object> entry : data_result.entrySet()) {
            if (entry.getKey().equals("name") || entry.getKey().equals("gender")
                    || entry.getKey().equals("water") || entry.getKey().equals("DisplayName")
                    || entry.getKey().equals("id") || entry.getKey().equals("email") || entry.getKey().equals("date")) {

            }
            else {
                StringBuilder sb = new StringBuilder();
                float val = nutri(entry.getKey()) + (Float.parseFloat((String) entry.getValue()) * counter);
                sb.append(val);
                String s = sb.toString();
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
            }
        }
    }
}

