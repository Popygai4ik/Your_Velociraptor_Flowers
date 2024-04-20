package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.yourvelociraptorflowers.domain.plants_generate;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.domain.PlantsAdapter;
import com.example.yourvelociraptorflowers.databinding.FragmentVseTvitiBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Vse_tviti_Fragment extends Fragment {
    private FragmentVseTvitiBinding binding;
    private PlantsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVseTvitiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PlantsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);
        binding.profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), User_profil.class);
            startActivity(intent);
        });


        // Отображаем ProgressBar при начале загрузки данных
        binding.progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("plants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Plants> plants = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Plants plant = document.toObject(Plants.class);
                        plants.add(plant);
                    }
                    adapter.setItems(plants);
                    // Скрываем ProgressBar после загрузки данных
                    binding.progressBar.setVisibility(View.GONE);
                    // Отображаем RecyclerView с данными
                    binding.recycler.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents.", e);
                    // Добавьте здесь обработку ошибки, например, вывод сообщения об ошибке пользователю
                    // Скрываем ProgressBar в случае ошибки
                    binding.progressBar.setVisibility(View.GONE);
                });
    }

}

