package com.example.yourvelociraptorflowers.domain;

import android.content.Intent;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlantsViewHolderMoi extends ViewHolder {
    private ItemMoiBinding binding;

    public PlantsViewHolderMoi(ItemMoiBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public String getNextWateringDate(String lastWatered, int koofesiant_poliva, List<String> wateringDates) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date currentDate = new Date();

            Date nextWateringDate = null;
            for (String dateStr : wateringDates) {
                Date wateringDate = dateFormat.parse(dateStr);

                if (wateringDate.after(currentDate)) {
                    if (nextWateringDate == null || wateringDate.before(nextWateringDate)) {
                        nextWateringDate = wateringDate;
                    }
                }
            }

            if (nextWateringDate != null) {
                DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                return outputFormat.format(nextWateringDate);
            } else {
                return "∞";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }}

    public String getLastWateringDate(String lastWatered, List<String> wateringDates) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date currentDate = new Date();

            Date lastWateringDate = null;
            for (String dateStr : wateringDates) {
                Date wateringDate = dateFormat.parse(dateStr);

                if (wateringDate.before(currentDate)) {
                    if (lastWateringDate == null || wateringDate.after(lastWateringDate)) {
                        lastWateringDate = wateringDate;
                    }
                }
            }

            if (lastWateringDate != null) {
                DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                return outputFormat.format(lastWateringDate);
            } else {
                return lastWatered;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
    public void bind(Plants item) {
        binding.name.setText(item.getOpisanie());
        binding.opisanie.setText(item.getName());
        binding.lastWatered.setText("\uD83D\uDCA7Последний полив: " + getLastWateringDate(item.getLastWateredFormatted(), item.getLastWatered()));
        Log.wtf("lastWateredFormatted", String.valueOf(item.getLastWatered()));
        binding.nextWatered.setText("\uD83D\uDCA7Следующий полив: " + getNextWateringDate(item.getLastWateredFormatted(), item.getKoofesiant_poliva(), item.getLastWatered()));

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
                                // Получаем список moisFlowers как ArrayList<Map<String, Object>>
                                ArrayList<Map<String, Object>> moisFlowers = (ArrayList<Map<String, Object>>) documentSnapshot.get("moisFlowers");

                                // Проверяем, есть ли элемент в списке
                                if (moisFlowers != null) {
                                    boolean found = false;
                                    for (Map<String, Object> flower : moisFlowers) {
                                        if (elementId.equals(flower.get("id"))) {
                                            moisFlowers.remove(flower);
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (found) {
                                        // Создаем карту для обновления данных пользователя
                                        Map<String, Object> updateMap = new HashMap<>();
                                        updateMap.put("moisFlowers", moisFlowers);

                                        // Обновляем документ в Firestore
                                        firestore.collection("users")
                                                .document(currentUser.getUid())
                                                .update(updateMap)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(itemView.getContext(), "Цветок удален", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(itemView.getContext(), "Ошибка при удалении цветка", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Цветок уже удален", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(itemView.getContext(), "Список цветков пуст", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(itemView.getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                        });
                }

        });
}}
