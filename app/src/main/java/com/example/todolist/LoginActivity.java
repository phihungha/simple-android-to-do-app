package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null)
            openMainActivity();

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            firebaseAuth.signInWithEmailAndPassword("hung@gmail.com", "12345678")
                    .addOnSuccessListener(authResult -> openMainActivity());
        });
    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}