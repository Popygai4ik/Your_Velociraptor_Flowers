package com.example.yourvelociraptorflowers.ui.user.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ActivityProvileBinding;
import com.example.yourvelociraptorflowers.databinding.ResetLocationActivityBinding;
import com.example.yourvelociraptorflowers.ui.user.viewing.User_profile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.yourvelociraptorflowers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResetLocationActivity extends AppCompatActivity {
    private ResetLocationActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ResetLocationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        binding.cityEditText.setText(city);


        // Set up button click listeners
        binding.ResetLocationButton.setOnClickListener((View.OnClickListener) v -> {
            String city1 = binding.cityEditText.getText().toString().trim();
            if (!city1.isEmpty()) {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                    // Обновляем поле "city"
                    firestore.collection("users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // Обновляем список
                                    Map<String, Object> update = new HashMap<>();
                                    update.put("city", city1);
                                    firestore.collection("users")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .update(update)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Город изменен на: " + city1, Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Ошибка обновления города", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                            });

               } else {
                Toast.makeText(ResetLocationActivity.this, "Пожалуйста, введите город", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
