package com.example.yourvelociraptorflowers.ui.plants.question;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ActivityAskExpertAQuestionBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_Question_For_Expert extends AppCompatActivity {
    private ActivityAskExpertAQuestionBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAskExpertAQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();

        String plantName = intent.getStringExtra("name");
        if (plantName != null) {
            binding.plantsField.setText(plantName);
        }

        binding.submitButton.setOnClickListener(v -> submitQuestion());
        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    private void submitQuestion() {
        String plantName = binding.plantsField.getText().toString().trim();
        String question = binding.problemField.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (plantName.isEmpty() || question.isEmpty()) {
            Toast.makeText(Activity_Question_For_Expert.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = currentUser != null ? currentUser.getEmail() : "Почта не обнаружена";

        Map<String, Object> questionData = new HashMap<>();
        questionData.put("plantName", plantName);
        questionData.put("question", question);
        questionData.put("email", userEmail);

        db.collection("questions_for_experts")
                .add(questionData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Activity_Question_For_Expert.this, "Вопрос успешно отправлен. Наш эксперт свяжется с вами по почте! Для скорейшего решения вопроса или проблемы!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity_Question_For_Expert.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(Activity_Question_For_Expert.this, "Ошибка при отправке вопроса. Код ошибки: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
