package com.example.yourvelociraptorflowers.domain;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ItemVseBinding;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.ui.OpisanieActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantsViewHolder extends ViewHolder {

    private ItemVseBinding binding;


    public PlantsViewHolder(ItemVseBinding binding) {
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
           intent.putExtra("Id", item.getId());
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
        });
        binding.addButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                // Создаем новый элемент для добавления в список
                String newElement = item.getId();

                // Получаем текущий список из Firestore
                firestore.collection("users")
                        .document(currentUser.getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            // Проверяем, есть ли уже список moisFlowers
                            if (documentSnapshot.contains("moisFlowers")) {
                                ArrayList<String> moisFlowers = (ArrayList<String>) documentSnapshot.get("moisFlowers");
                                if (moisFlowers.contains(newElement)) {
                                    // Если элемент уже есть в списке, выдаем сообщение об ошибке
                                    Toast.makeText(itemView.getContext(), "Цветок уже добавлен", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // Добавляем новый элемент в список
                                moisFlowers.add(newElement);

                                // Создаем карту для обновления данных пользователя
                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("moisFlowers", moisFlowers);

                                // Обновляем документ в Firestore
                                firestore.collection("users")
                                        .document(currentUser.getUid())
                                        .update(updateMap)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(itemView.getContext(), "Добавлено", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(itemView.getContext(), "Не добавлено", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Если список еще не существует, создаем новый список с новым элементом
                                ArrayList<String> moisFlowers = new ArrayList<>();
                                moisFlowers.add(newElement);

                                // Создаем карту для обновления данных пользователя
                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("moisFlowers", moisFlowers);

                                // Обновляем документ в Firestore
                                firestore.collection("users")
                                        .document(currentUser.getUid())
                                        .set(updateMap)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(itemView.getContext(), "Добавлено", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(itemView.getContext(), "Не добавлено", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(itemView.getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Пользователь не вошел, выводим сообщение
                Toast.makeText(itemView.getContext(), "Войдите, чтобы добавить цветок", Toast.LENGTH_SHORT).show();
            }
        });
//        binding.addButton.setOnClickListener(v -> {});
    }
}
