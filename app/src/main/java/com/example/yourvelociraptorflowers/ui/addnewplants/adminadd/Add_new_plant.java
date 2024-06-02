package com.example.yourvelociraptorflowers.ui.addnewplants.adminadd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.AddNewPlantBinding;
import com.example.yourvelociraptorflowers.model.plant.Plants;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_new_plant extends AppCompatActivity {

    private AddNewPlantBinding binding;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddNewPlantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        firestore = FirebaseFirestore.getInstance();

        binding.buttonAddPlant.setOnClickListener(v -> addPlant());
    }

    private void addPlant() {
        String name = binding.editTextName.getText().toString().trim();
        String descriptionKratkoe = binding.editTextDescriptionKratkoe.getText().toString().trim();
        String descriptionTemp = binding.editTextDescriptionTemp.getText().toString().trim();
        String descriptionVlag = binding.editTextDescriptionVlag.getText().toString().trim();
        String descriptionVrsh = binding.editTextDescriptionVrsh.getText().toString().trim();
        String descriptionUTUB = binding.editTextUTUB.getText().toString().trim();
        String descriptionGlavRis = binding.editTextGlavRis.getText().toString().trim();
        String descriptionRis2 = binding.editTextRis2.getText().toString().trim();
        String descriptionRis3 = binding.editTextRis3.getText().toString().trim();
        String descriptionRis4 = binding.editTextRis4.getText().toString().trim();

        if (name.isEmpty() || descriptionKratkoe.isEmpty() || descriptionTemp.isEmpty() ||
                descriptionVlag.isEmpty() || descriptionVrsh.isEmpty() || descriptionUTUB.isEmpty() ||
                descriptionGlavRis.isEmpty() || descriptionRis2.isEmpty() || descriptionRis3.isEmpty() ||
                descriptionRis4.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Проверяем, существует ли растение с таким именем
        firestore.collection("plants")
                .whereEqualTo("name", name)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // Растение с таким именем не существует, можно добавлять
                        Plants newPlant = new Plants(name,
                                descriptionKratkoe,
                                descriptionGlavRis,
                                descriptionRis2,
                                descriptionRis3,
                                descriptionRis4,
                                descriptionTemp,
                                descriptionVlag,
                                descriptionVrsh,
                                descriptionUTUB, 21, 2500
                                );
                        firestore.collection("plants")
                                .add(newPlant)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Растение добавлено", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Произошла ошибка при добавлении растения", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Растение с таким именем уже существует
                        Toast.makeText(this, "Растение с таким именем уже существует", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        binding.editTextName.setText("");
        binding.editTextDescriptionKratkoe.setText("");
        binding.editTextDescriptionTemp.setText("");
        binding.editTextDescriptionVlag.setText("");
        binding.editTextDescriptionVrsh.setText("");
        binding.editTextUTUB.setText("");
        binding.editTextGlavRis.setText("");
        binding.editTextRis2.setText("");
        binding.editTextRis3.setText("");
        binding.editTextRis4.setText("");
    }

}
