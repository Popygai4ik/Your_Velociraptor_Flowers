package com.example.yourvelociraptorflowers.ui.user.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.yourvelociraptorflowers.R;
import java.util.HashMap;
import java.util.Map;

public class SupportActivityNezaregan extends AppCompatActivity {

    private TextInputEditText emailField;
    private TextInputEditText problemField;
    private MaterialButton resetpas;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_activiti_nezaregan);
        emailField = findViewById(R.id.emailField);
        problemField = findViewById(R.id.problemField);
        resetpas = findViewById(R.id.resetpas);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
        resetpas.setOnClickListener(v -> sendProblemToFirestore());
    }

    private void sendProblemToFirestore() {
        String email = emailField.getText().toString().trim();
        String problem = problemField.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(this, "Пожалуйста, укажите вашу почту", Toast.LENGTH_SHORT).show();
            if (problem.isEmpty()){
                Toast.makeText(this, "Пожалуйста, укажите проблему", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (problem.isEmpty()){
            Toast.makeText(this, "Пожалуйста, укажите проблему", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> problemData = new HashMap<>();
        problemData.put("email", email);
        problemData.put("problem", problem);

        db.collection("problems")
                .add(problemData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(SupportActivityNezaregan.this, "Проблема успешно отправлена, ждите ответа на почту!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SupportActivityNezaregan.this, MainActivity.class));
                }).addOnFailureListener(e -> Toast.makeText(SupportActivityNezaregan.this, "Ошибка отправки данных: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
