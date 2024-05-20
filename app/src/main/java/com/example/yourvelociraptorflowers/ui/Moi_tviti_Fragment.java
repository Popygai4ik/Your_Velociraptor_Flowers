package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yourvelociraptorflowers.databinding.FragmentMoiTvitiBinding;
import com.example.yourvelociraptorflowers.domain.PlantsAdapterMoi;
import com.example.yourvelociraptorflowers.model.Plants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Moi_tviti_Fragment extends Fragment {
    private PlantsAdapterMoi adapter;
    private FragmentMoiTvitiBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoiTvitiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), User_profil.class);
            startActivity(intent);
        });

        binding.searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Search_activiti.class);
            startActivity(intent);
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.imageView.setVisibility(View.GONE);
            binding.textView.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.GONE);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            adapter = new PlantsAdapterMoi();
            binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recycler.setAdapter(adapter);

            binding.textView2.setVisibility(View.GONE);

            DocumentReference userRef = firestore.collection("users").document(mAuth.getCurrentUser().getUid());
            userRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Firestore", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    List<Map<String, Object>> moisFlowers = (List<Map<String, Object>>) documentSnapshot.get("moisFlowers");

                    firestore.collection("plants")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<Plants> plants = new ArrayList<>();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    Plants plant = document.toObject(Plants.class);
                                    if (moisFlowers != null) {
                                        for (Map<String, Object> moisFlower : moisFlowers) {
                                            String id = (String) moisFlower.get("id");
                                            if (id.equals(plant.getId())) {
                                                long lastWateredTimestamp = (long) moisFlower.get("lastWatered");
                                                String lastWateredFormatted = sdf.format(new Date(lastWateredTimestamp));
                                                plant.setLastWateredFormatted(lastWateredFormatted);
                                                ArrayList<Long> nextWateringDates = (ArrayList<Long>) moisFlower.get("nextWateringDates");

                                                // Массив для хранения строковых представлений дат
                                                ArrayList<String> formattedDates = new ArrayList<>();

                                                // Формат для преобразования даты в строку

                                                for (Long dateInt : nextWateringDates) {

                                                    Date date = new Date(dateInt); // умножаем на 1000 для преобразования в миллисекунды
                                                    // Форматируем дату и добавляем в массив
                                                    formattedDates.add(sdf.format(date));
                                                }

                                                // Пример вывода результатов
                                                for (String formattedDate : formattedDates) {
                                                    System.out.println(formattedDate);
                                                }

                                                // Теперь можно использовать массив строк для других нужд
                                                plant.setLastWatered(formattedDates);
                                                plants.add(plant);
                                            }
                                        }
                                    }
                                }
                                if (plants.isEmpty()) {
                                    binding.textView2.setVisibility(View.VISIBLE);
                                } else {
                                    binding.textView2.setVisibility(View.GONE);
                                }
                                adapter.setItems(plants);
                                binding.progressBar.setVisibility(View.GONE);
                                binding.recycler.setVisibility(View.VISIBLE);
                            })
                            .addOnFailureListener(e1 -> {
                                Log.e("Firestore", "Error getting documents.", e1);
                                binding.progressBar.setVisibility(View.GONE);
                            });
                } else {
                    Log.d("Firestore", "Current data: null");
                }
            });
        } else {
            binding.recycler.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            binding.imageView.setVisibility(View.VISIBLE);
            binding.textView.setVisibility(View.VISIBLE);
            binding.registerButton.setVisibility(View.VISIBLE);
            binding.textView2.setVisibility(View.GONE);
            binding.registerButton.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), User_profil.class);
                startActivity(intent);
            });
        }
    }
}
