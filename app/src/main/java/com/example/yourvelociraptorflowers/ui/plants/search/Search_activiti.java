package com.example.yourvelociraptorflowers.ui.plants.search;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.yourvelociraptorflowers.databinding.SearchActivitiBinding;
import com.example.yourvelociraptorflowers.domain.Allplants.PlantsAdapter;
import com.example.yourvelociraptorflowers.model.Plants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
public class Search_activiti extends AppCompatActivity {
    private PlantsAdapter adapter;
    private SearchActivitiBinding binding;
    private List<Plants> allPlants = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchActivitiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new PlantsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
        binding.recycler.setVisibility(View.GONE);
        binding.backButton.setOnClickListener(v -> onBackPressed());
        binding.progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("plants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Plants plant = document.toObject(Plants.class);
                        allPlants.add(plant);
                    }
                    // Скрываем ProgressBar после загрузки данных
                    binding.progressBar.setVisibility(View.GONE);
                    adapter.setItems(allPlants);
                    // Отображаем RecyclerView с данными
                    binding.recycler.setVisibility(View.VISIBLE);
                    binding.searchButton.setOnClickListener(onSearchButtonClick());
                    binding.searchEditText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                            performSearch();
                            return true;
                        } else if (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                performSearch();
                                return true;
                            }
                        }
                        return false;
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error getting documents.", e);
                    // Добавьте здесь обработку ошибки, например, вывод сообщения об ошибке пользователю
                    // Скрываем ProgressBar в случае ошибки
                    binding.progressBar.setVisibility(View.GONE);
                });
    }

    private View.OnClickListener onSearchButtonClick() {
        return v -> performSearch();
    }

    private void performSearch() {
        String searchText = binding.searchEditText.getText().toString().trim().toLowerCase();
        List<Plants> filteredPlants = new ArrayList<>();
        for (Plants plant : allPlants) {
            if (plant.getName().toLowerCase().contains(searchText)) {
                filteredPlants.add(plant);
            }
        }

        if (filteredPlants.isEmpty()) {
            // Если результаты не найдены, показываем сообщение
            adapter.setItems(allPlants);
            Toast.makeText(Search_activiti.this, "Растения не найдены", Toast.LENGTH_SHORT).show();
        } else {
            // Если найдены результаты, обновляем адаптер для RecyclerView
            adapter.setItems(filteredPlants);
            adapter.notifyDataSetChanged();
        }
    }
}