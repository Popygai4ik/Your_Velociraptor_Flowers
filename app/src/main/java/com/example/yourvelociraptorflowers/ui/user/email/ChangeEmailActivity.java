package com.example.yourvelociraptorflowers.ui.user.email;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailActivity extends AppCompatActivity {

    private EditText editTextCurrentPassword, editTextEmail;
    private Button buttonChangeEmail;
    private ImageButton backButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> onBackPressed());

        editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonChangeEmail = findViewById(R.id.buttonChangeEmail);
        mAuth = FirebaseAuth.getInstance();
        buttonChangeEmail.setOnClickListener(v -> changeEmail());
    }

    private void changeEmail() {
        String currentPassword = editTextCurrentPassword.getText().toString().trim();
        String newEmail = editTextEmail.getText().toString().trim();

        if (!isValidPassword(currentPassword)) {
            Toast.makeText(this, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!containsDigit(currentPassword)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну цифру", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!containsUpperCase(currentPassword)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну заглавную букву", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!containsLowerCase(currentPassword)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы одну строчную букву", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!containsSpecialChar(currentPassword)) {
            Toast.makeText(this, "Пароль должен содержать хотя бы один специальный символ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newEmail)) {
            editTextEmail.setError("Введите новую почту");
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(ChangeEmailActivity.this, "Почта изменена на " + newEmail, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ChangeEmailActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
//                                            Toast.makeText(ChangeEmailActivity.this, "Не удалось изменить почту", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(e -> {
                                Toast.makeText(ChangeEmailActivity.this, "Не удалось изменить почту! Код ошибки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.wtf("LOGG", e.getMessage());
                                    });
                        } else {
                            Toast.makeText(ChangeEmailActivity.this, "Аутентификация не удалась", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                Toast.makeText(ChangeEmailActivity.this, "Не удалось изменить почту! Код ошибки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.wtf("LOGG", e.getMessage());
                    });
        }
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

}