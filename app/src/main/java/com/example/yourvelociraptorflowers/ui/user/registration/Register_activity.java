package com.example.yourvelociraptorflowers.ui.user.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.RegisterActivityBinding;
import com.example.yourvelociraptorflowers.model.user.User;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    public boolean validateFields(String imifam, String city, String email, String password) {
        boolean isValid = true;
        if (imifam.isEmpty()) {
            binding.imifam.setError("Имя не должно быть пустым");
            isValid = false;
        }

        if (city.isEmpty()) {
            binding.city.setError("Город не должен быть пустым");
            isValid = false;
        } else if (!isValidCity(city)) {
            binding.city.setError("Некорректный город");
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

    private boolean isValidCity(String city) {
        return city.matches("[a-zA-Zа-яА-Я\\s]+");
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
        String city = binding.city.getText().toString();
        String email = binding.emailField.getText().toString();
        String password = binding.passwordField.getText().toString();
        Boolean age = binding.radioButton.isChecked();
        if (validateFields(ime, city, email, password)) {
            signinFirebase(ime, city, email, password, age);
        }
    }

    private void signinFirebase(String ime, String city, String email, String password, Boolean age) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    ArrayList<String> moisFlowers = new ArrayList<>();
                    ArrayList<String> notifications = new ArrayList<>();
                    long currentTime = System.currentTimeMillis();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(currentTime);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM yyyy 'в' HH:mm:ss", Locale.getDefault());

                    String currentTimeNormalFormat = sdf2.format(calendar.getTime());

                    User newUser = new User(authResult.getUser().getUid(), authResult.getUser().getUid(), ime, email, password, age, moisFlowers, notifications, city, currentTimeNormalFormat);
                    firestore.collection("users")
                            .document(authResult.getUser().getUid())
                            .set(newUser);
                    Toast.makeText(this, "Вы успешно зарегистрировались!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка при регистрации: " + e.getMessage()+"\nПопробуйте ещё раз или напишите в нашу поддержку!🆘", Toast.LENGTH_SHORT).show();
                    Log.wtf("Login error", e.getMessage());
                });
    }
}
