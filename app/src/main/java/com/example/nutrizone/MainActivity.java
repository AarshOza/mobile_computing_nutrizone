package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity  {


    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth auth;
    final Handler handler = new Handler();
    private String created_date, today_date;
    Integer days;
    Boolean temp=false;
    private Map<String, Object> user_data;

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(r, 1000);
    }


    final Runnable r = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            FirebaseUser user = auth.getCurrentUser();

            if (user != null){
                getDate(user);
                Log.d("LOGIN_DETAILS", "correct ==> ");
                startActivity(new Intent(MainActivity.this, Home.class));

            }

            else{
                Log.d("LOGIN_DETAILS", "wrong ==> ");
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        startService( new Intent( this, NotificationService. class )) ;

    }

    private void getDate(FirebaseUser user) {

        Log.d("LOGIN_DETAILS", "in getDate Function ==> ");

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("LOGIN_DETAILS", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Map<String, Object> data = snapshot.getData();
                            Log.d("LOGIN_DETAILS", "the data is ==> " + data);
                            for (Map.Entry<String, Object> entry: data.entrySet()) {
                                user_data  = data;
                                if (entry.getKey().equals("date")) {
                                    Log.d("LOGIN_DETAILS", "the created date is ==> " + entry.getValue().toString());
                                    created_date = (entry.getValue().toString());
                                    temp = true;

                                    onDataChange(data);
                                }
                            }

                        } else {
                            Log.d("LOGIN_DETAILS", "Current data: null");
                        }
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onDataChange(Map<String, Object> data) {

        Log.d("LOGIN_DETAILS", "in DataChange function with data ==> " + data);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        today_date = df.format(c);
        Log.d("LOGIN_DETAILS", "Today's date is ==>" + df.format(c));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = sdf.parse(today_date);
            Date date2 = sdf.parse(created_date);
            long diff = date1.getTime() - date2.getTime();
            days = Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            Log.d("LOGIN_DETAILS", "days here is updated: " + days);

            if (days!=0){
                changeDate(data);
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    private void changeDate(Map<String, Object> data) {

        for (Map.Entry<String,Object> entry : data.entrySet()){
            if (entry.getKey().equals("name") || entry.getKey().equals("gender")
                    || entry.getKey().equals("DisplayName")
                    || entry.getKey().equals("id") || entry.getKey().equals("email")) {

            }
            else {
                if (entry.getKey().equals("date")) {
                    Log.d("LOGIN_DETAILS", "in user to be updated ==> " + entry.getKey() + "and value is " + entry.getValue());
                    FirebaseFirestore.getInstance().collection("users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update(entry.getKey(), today_date)
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
                } else {
                    Log.d("LOGIN_DETAILS", "in user data updated ==> " + entry.getKey() + "and value is " + entry.getValue());
                    FirebaseFirestore.getInstance().collection("users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update(entry.getKey(), "0")
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
}
