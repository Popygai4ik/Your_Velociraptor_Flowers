package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.RegisterActivityBinding;
import com.example.yourvelociraptorflowers.model.User;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Register_activity extends AppCompatActivity {

    private RegisterActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginEmail.setOnClickListener(v -> loginwithEmail());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    public boolean validateFields(String imifam, String email, String password) {
        boolean isValid = true;
        if (imifam.isEmpty()) {
            binding.imifam.setError("Имя не должно быть пустым");
            isValid = false;
        }

        if (!isValidEmail(email)) {
            binding.emailField.setError("Некорректный email");
            isValid = false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (!containsDigit(password)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну цифру", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!containsUpperCase(password)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну заглавную букву", Toast.LENGTH_SHORT).show();

            isValid = false;
        }

        if (!containsLowerCase(password)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну строчную букву", Toast.LENGTH_SHORT).show();

            isValid = false;
        }

        if (!containsSpecialChar(password)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы один специальный символ", Toast.LENGTH_SHORT).show();

            binding.passwordField.setError("Пароль должен содержать хотя бы один специальный символ");
            isValid = false;
        }


        return isValid;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private boolean containsDigit(String s) {
        return s.matches(".*\\d.*");
    }

    private boolean containsUpperCase(String s) {
        return !s.equals(s.toLowerCase());
    }

    private boolean containsLowerCase(String s) {
        return !s.equals(s.toUpperCase());
    }

    private boolean containsSpecialChar(String s) {
        return !s.matches("[A-Za-z0-9 ]*");
    }

    private void loginwithEmail() {
        String ime = binding.imifam.getText().toString();
        String email = binding.emailField.getText().toString();
        String password = binding.passwordField.getText().toString();
        Boolean vozrst = binding.radioButton.isChecked();
        if (validateFields(ime, email, password)) {
            signinFirebase(ime, email, password, vozrst);
        }
    }
    private void signinFirebase(String ime, String email, String password, Boolean vozrst) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    ArrayList<String> moisFlowers = new ArrayList<>();
                    User newUser = new User(authResult.getUser().getUid(),authResult.getUser().getUid(), ime, email, password, vozrst, moisFlowers);
                    firestore.collection("users")
                            .document(authResult.getUser().getUid())
                            .set(newUser);
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