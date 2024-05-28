package com.example.yourvelociraptorflowers.ui.addnewplants.useradd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.AddNewPlaintUserBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_new_plant_user extends AppCompatActivity {

    private AddNewPlaintUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddNewPlaintUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.resetpas.setOnClickListener(v -> sendPlantToFirestore());
    }

    private void sendPlantToFirestore() {
        String email = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        String name = binding.nameField.getText().toString().trim();
        String description = binding.problemField.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> plantData = new HashMap<>();

        plantData.put("email", email);
        plantData.put("name", name);
        plantData.put("description", description);

        db.collection("user_plants")
                .add(plantData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Цветок успешно отправлен! После проверки модератором вы сможете увидеть его!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Add_new_plant_user.this, MainActivity.class));
                })
                .addOnFailureListener(e -> Toast.makeText(Add_new_plant_user.this, "Ошибка отправки данных: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
