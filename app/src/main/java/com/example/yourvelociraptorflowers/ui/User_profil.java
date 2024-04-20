package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ActivityProvileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_profil extends AppCompatActivity {

    private ActivityProvileBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProvileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Пользователь залогинен
            String email = currentUser.getEmail();
            binding.profileEmail.setText(email);
            binding.profileTitle.setVisibility(View.VISIBLE);
            binding.profileEmail.setVisibility(View.VISIBLE);
            binding.logoutButton.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.GONE);
            binding.ne.setVisibility(View.GONE);
            binding.ine.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.GONE);
            binding.logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    // После выхода из аккаунта переходите на экран входа или любой другой экран
                    // Здесь можно использовать Intent для перехода на нужный экран
                    // Например:
                    Intent intent = new Intent(User_profil.this, User_profil.class);
                    startActivity(intent);
                    finish(); // Закрываем текущую активность

                }
            });

        } else {
            // Пользователь не залогинен
            binding.profileName.setVisibility(View.GONE);
            binding.ne.setVisibility(View.VISIBLE);
            binding.ine.setVisibility(View.VISIBLE);
            binding.profileTitle.setVisibility(View.GONE);
            binding.profileEmail.setVisibility(View.GONE);
            binding.logoutButton.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.registerButton.setVisibility(View.VISIBLE);
            binding.loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(User_profil.this, Login_activity.class);
                    startActivity(intent);
                }
            });
            binding.registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(User_profil.this, Register_activity.class);
                    startActivity(intent);
                }
            });
        }



    }

}
