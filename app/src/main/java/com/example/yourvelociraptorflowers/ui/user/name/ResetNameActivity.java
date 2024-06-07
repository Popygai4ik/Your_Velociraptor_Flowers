package com.example.yourvelociraptorflowers.ui.user.name;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ResetNameActivityBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResetNameActivity extends AppCompatActivity {

    private ResetNameActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ResetNameActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        binding.cityEditText.setText(name);


        // Set up button click listeners
        binding.ResetNameButton.setOnClickListener((View.OnClickListener) v -> {
            String name1 = binding.cityEditText.getText().toString().trim();
            if (!name1.isEmpty()) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                // Обновляем поле "city"
                firestore.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Обновляем список
                                Map<String, Object> update = new HashMap<>();
                                update.put("name", name1);
                                firestore.collection("users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update(update)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Имя изменено на: " + name1, Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(ResetNameActivity.this, MainActivity.class);
                                            startActivity(intent1);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Ошибка обновления имени", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(this, "Пожалуйста, введите имя", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
