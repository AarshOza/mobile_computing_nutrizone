package com.example.nutrizone.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutrizone.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class google_sign_in extends Activity {
    public static void activityResult(int requestCode, int resultCode, Intent data){
    Log.d("LOGIN_DETAILS", "in activityResult");
}

    Activity activity;
    private static final int SIGN_IN = 1;

    public google_sign_in(Activity activity){
        this.activity=activity;
    }

    private GoogleSignInClient googleClient;

    public void signInToGoogle(Context context, MainActivity main_activity) {
        Intent signInIntent = googleClient.getSignInIntent();
        main_activity.startActivityForResult(signInIntent, SIGN_IN);
    }

    public void configureSignIn() {
        Log.d("LOGIN_DETAILS", "in configureSignIn()");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1039477981472-qsfjv5t91jjg6v2e54suqnk98bp6pkbg.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(activity, gso);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        activity.onActivityResult(requestCode, resultCode, data);
//        Log.d("LOGIN_DETAILS", "in onActivityResult()");
//        if (requestCode == SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignIn(task);
//        }
//    }

    public void handleSignIn(Task<GoogleSignInAccount> task) {
        try {
            Log.d("LOGIN_DETAILS", "in HandleSignIn Function");
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseauthwithGoogle(account.getIdToken());
            Log.d("LOGIN_DETAILS", "getIdToken ==>" + account.getIdToken());
            Log.d("LOGIN_DETAILS", "getId ==>" + account.getId());

        }
        catch (ApiException e) {
            Log.w("LOGIN_DETAILS", "Google sign in failed", e);
            e.printStackTrace();
        }
    }

    private void firebaseauthwithGoogle(String idToken) {
        Log.d("LOGIN_DETAILS", "In firebaseAuth function");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGIN_DETAILS", "task" + task);
                        Log.d("LOGIN_DETAILS", "In onComplete function");
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "User registered succesfully", Toast.LENGTH_LONG).show();
                            Log.d("LOGIN_DETAILS", "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        }
                        else {
                            Toast.makeText(activity, "User cannot be registered", Toast.LENGTH_LONG).show();
                            Log.w("LOGIN_DETAILS", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

}
