package com.example.yourvelociraptorflowers.ui.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ActivityLoginBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Login_activity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginEmail.setOnClickListener(v -> loginwithEmail());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.resetpas.setOnClickListener(v -> {
            Intent intent = new Intent(this, PasswordResetActivity.class);
            startActivity(intent);
        });

    }
    public Boolean validateFields(String email, String password) {
        if (!email.contains("@")) {
            binding.emailField.setError("Некорректный email");
            return false;
        }
        if (password.isEmpty()) {
            binding.passwordField.setError("Некорректный пароль");
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
                    Toast.makeText(this, "Вы успешно вошли в аккаунт!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка входа: " + e.getMessage() + "\nПопробуйте ещё раз или напишите в нашу поддержку!🆘", Toast.LENGTH_SHORT).show();
                });


    }
}
