package com.example.burgerdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    private static final int SIGNIN_KEY = 123;
    EditText userName;
    EditText userPass;

    private SharedPreferences preferences;
    private FirebaseAuth auth;
    private GoogleSignInClient googleClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        userPass = findViewById(R.id.userPass);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userName.getText().toString());
        editor.putString("userPass", userPass.getText().toString());
        editor.apply();
    }

    public void login(View view) {
        String userNameStr = userName.getText().toString();
        String userPassStr = userPass.getText().toString();
        
        Log.i(LOG_TAG, "Bejelentkezett: " + userNameStr + ", jelszó: " + userPassStr + ".");

        auth.signInWithEmailAndPassword(userNameStr, userPassStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG, "SIkeres bejelentkezés!");
                    startOrdering();
                }else{
                    Log.i(LOG_TAG, "Sikertelen bejelentkezés");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNIN_KEY){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.w(LOG_TAG, "account id: " + account.getId());
                firebaseAuthWithGoogle(account.getId());
            } catch (ApiException e){
                Log.w(LOG_TAG, e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String id) {
        AuthCredential credential = GoogleAuthProvider.getCredential(id, null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.i(LOG_TAG, "Sikeres bejelentkezés");
                startOrdering();
            }else{
                Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
            }
        });
    }

    public void signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("SECRET_KEY", 99);
        startActivity(intent);
    }
    private void startOrdering(){
        Intent intent = new Intent(this, MenuListActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void googleSignin(View view) {
        Intent signinIntent = googleClient.getSignInIntent();
        startActivityForResult(signinIntent, SIGNIN_KEY);
    }
}

