package com.example.yourvelociraptorflowers.domain;

import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ItemMoiBinding;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.ui.OpisanieActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantsViewHolderMoi extends ViewHolder {
    private ItemMoiBinding binding;

    public PlantsViewHolderMoi(ItemMoiBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Plants item) {
        binding.name.setText(item.getOpisanie());
        binding.opisanie.setText(item.getName());
        // Load image
        Glide.with(itemView)
                .load(item.getResinok())
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.resinok);
        // Set Listener
        binding.moreButton.setOnClickListener(v -> {
            Intent intent = new Intent(itemView.getContext(), OpisanieActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("opisanie", item.getOpisanie());
            intent.putExtra("temp", item.getOpisanie2());
            intent.putExtra("vlag", item.getOpisanie3());
            intent.putExtra("ysl", item.getOpisanie4());
            intent.putExtra("resinok", item.getResinok());
            intent.putExtra("resinok2", item.getResinok2());
            intent.putExtra("resinok3", item.getResinok3());
            intent.putExtra("resinok4", item.getResinok4());
            intent.putExtra("opisanie5", item.getOpisanie5());
            itemView.getContext().startActivity(intent);
    } );
        binding.delitButton.setOnClickListener(v -> {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                    // Получаем ID элемента, который нужно удалить
                    String elementId = item.getId();

                    // Получаем текущий список из Firestore
                    firestore.collection("users")
                            .document(currentUser.getUid())
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // Получаем список moisFlowers
                                    ArrayList<String> moisFlowers = (ArrayList<String>) documentSnapshot.get("moisFlowers");

                                    // Проверяем, есть ли элемент в списке
                                    if (moisFlowers != null && moisFlowers.contains(elementId)) {
                                        // Удаляем элемент из списка
                                        moisFlowers.remove(elementId);

                                        // Создаем карту для обновления данных пользователя
                                        HashMap<String, Object> updateMap = new HashMap<>();
                                        updateMap.put("moisFlowers", moisFlowers);

                                        // Обновляем документ в Firestore
                                        firestore.collection("users")
                                                .document(currentUser.getUid())
                                                .update(updateMap)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(itemView.getContext(), "Элемент удален", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(itemView.getContext(), "Ошибка при удалении элемента", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Элемент уже удален", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(itemView.getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                            });
                }

        });
}}
