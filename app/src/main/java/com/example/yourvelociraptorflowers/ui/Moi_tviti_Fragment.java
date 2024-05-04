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

import java.util.ArrayList;
import java.util.List;

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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Проверяем, авторизован ли пользователь
        if (mAuth.getCurrentUser() != null) {
            // Если пользователь авторизован, отображаем контент

            // Отображаем ProgressBar до загрузки данных
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.imageView.setVisibility(View.GONE);
            binding.textView.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.GONE);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            adapter = new PlantsAdapterMoi();
            binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recycler.setAdapter(adapter);

            binding.textView2.setVisibility(View.GONE);

            // Получаем список цветов из Firestore
            DocumentReference userRef = firestore.collection("users").document(mAuth.getCurrentUser().getUid());
            userRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.e("Firestore", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    List<String> moisFlowers = (List<String>) documentSnapshot.get("moisFlowers");

                    // Получаем данные из коллекции "plants"
                    firestore.collection("plants")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<Plants> plants = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    Plants plant = document.toObject(Plants.class);
                                    // Проверяем, содержится ли идентификатор растения в списке
                                    if (moisFlowers != null && moisFlowers.contains(plant.getId())) {
                                        plants.add(plant);
                                    }
                                }
                                if (plants.isEmpty()) {
                                    binding.textView2.setVisibility(View.VISIBLE);
                                } else {
                                    binding.textView2.setVisibility(View.GONE);
                                }
                                // Устанавливаем данные в адаптер
                                adapter.setItems(plants);
                                // Скрываем ProgressBar после загрузки данных
                                binding.progressBar.setVisibility(View.GONE);
                                // Отображаем RecyclerView с данными
                                binding.recycler.setVisibility(View.VISIBLE);
                            })
                            .addOnFailureListener(e1 -> {
                                Log.e("Firestore", "Error getting documents.", e1);
                                // Добавьте здесь обработку ошибки, например, вывод сообщения об ошибке пользователю
                                // Скрываем ProgressBar в случае ошибки
                                binding.progressBar.setVisibility(View.GONE);
                            });
                } else {
                    Log.d("Firestore", "Current data: null");
                }
            });
        } else {
            // Если пользователь не авторизован, скрываем содержимое и показываем сообщение
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
            // Показать сообщение о том, что пользователь не авторизован
            // Можно также добавить кнопку для перехода на экран регистрации или входа
        }


    }}