package com.example.burgerdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = SignupActivity.class.getName();
    private static final String PREF_KEY = SignupActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    private SharedPreferences preferences;
    private FirebaseAuth auth;

    EditText userName;
    EditText userEmail;
    EditText userPass;
    EditText userPassAgain;
    EditText userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY");
        
        if (secret_key != 99){
            finish();
        }

        userName = findViewById(R.id.userNameEditText);
        userEmail = findViewById(R.id.userEmailEditText);
        userPass = findViewById(R.id.userPassEditText);
        userPassAgain = findViewById(R.id.userPassAgainEditText);
        userPhone = findViewById(R.id.userPhoneEditText);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userNameMain = preferences.getString("userName", "");
        String userPassMain = preferences.getString("userPass", "");

        userName.setText(userNameMain);
        userPass.setText(userPassMain);
        userPassAgain.setText(userPassMain);

        auth = FirebaseAuth.getInstance();

    }

    public void signup(View view) {
        String username = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPass.getText().toString();
        String passAgain = userPassAgain.getText().toString();
        String phoneNumber = userPhone.getText().toString();

        if(!password.equals(passAgain)) {
            Log.e(LOG_TAG, "A jelszavak nem egyeznek!");
            return;
        }


        Log.i(LOG_TAG, "Regisztrált: " + username + ", email: " + email + ".");

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG, "Sikeres regisztráció!");
                    startOrdering();
                }else{
                    Log.e(LOG_TAG, "Nem sikerült a regisztráció!");
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    private void startOrdering(){
        Intent intent = new Intent(this, MenuListActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String selectedItem = parent.getItemAtPosition(i).toString();
        Log.i(LOG_TAG, selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    //TODO
    }
}