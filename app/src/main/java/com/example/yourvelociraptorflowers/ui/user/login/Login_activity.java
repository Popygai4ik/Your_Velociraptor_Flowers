package com.example.yourvelociraptorflowers.ui.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ActivityCustomBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login_activity extends AppCompatActivity {

    private ActivityCustomBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginEmail.setOnClickListener(v -> loginwithEmail());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    public Boolean validateFields(String email, String password) {
        if (!email.contains("@")) {
            binding.emailField.setError("Wrong email");
            return false;
        }
        if (password.isEmpty()) {
            binding.passwordField.setError("Password should not be empty");
            return false;
        }
        return true;
    }
    private void loginwithEmail() {
        String email = binding.emailField.getText().toString();
        String password = binding.passwordField.getText().toString();
        if (validateFields(email, password)) {
            signinFirebase(email, password);
        }
    }
    private void signinFirebase(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Login success: " + authResult.getUser().getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "SignIn error: " + email+" "+password, Toast.LENGTH_SHORT).show();

                    Toast.makeText(this, "SignIn error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }
}
