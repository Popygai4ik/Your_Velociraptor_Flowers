package com.example.yourvelociraptorflowers.ui.user.support;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.SupportActivitiRegisteredBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SupportActivityRegistered extends AppCompatActivity {

    private SupportActivitiRegisteredBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SupportActivitiRegisteredBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.problembut.setOnClickListener(v -> sendProblemToFirestore());
    }

    private void sendProblemToFirestore() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String problem = binding.problemField.getText().toString().trim();

        if (email.isEmpty() || problem.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, укажите проблему+", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> problemData = new HashMap<>();
        problemData.put("email", email);
        problemData.put("problem", problem);

        db.collection("problems")
                .add(problemData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SupportActivityRegistered.this, "Проблема успешно отправлена, ждите ответа на почту!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SupportActivityRegistered.this, MainActivity.class));
                }).addOnFailureListener(e -> Toast.makeText(SupportActivityRegistered.this, "Ошибка отправки данных: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
