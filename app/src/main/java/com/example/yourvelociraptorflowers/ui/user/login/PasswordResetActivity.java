package com.example.yourvelociraptorflowers.ui.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ResetPasswordActivityBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {
    private ResetPasswordActivityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ResetPasswordActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.resetpas.setOnClickListener(v -> {
            String email = binding.emailField.getText().toString();
            if (email.isEmpty()) {
                binding.emailField.setError("Пожалуста, введите вашу почту, для востановления пароля");
                return;
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordResetActivity.this, "Проверьте вашу почту для востановления пароля", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PasswordResetActivity.this, MainActivity.class));
                        }
                    });
        });
    }
}