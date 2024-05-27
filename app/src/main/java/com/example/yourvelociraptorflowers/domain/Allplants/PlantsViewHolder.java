package com.example.yourvelociraptorflowers.domain.Allplants;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ItemVseBinding;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.ui.plants.podrobnie.OpisanieActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            intent.putExtra("koofesiant_poliva", item.getKoofesiant_poliva());

           itemView.getContext().startActivity(intent);
        });
        binding.addButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                // Создаем новый элемент для добавления в список
                String newElementId = item.getId();

                // Получаем текущий список из Firestore
                firestore.collection("users")
                        .document(currentUser.getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            // Проверяем, есть ли уже список moisFlowers
                            ArrayList<Map<String, Object>> moisFlowers = new ArrayList<>();
                            if (documentSnapshot.contains("moisFlowers")) {
                                List<?> moisFlowersRaw = (List<?>) documentSnapshot.get("moisFlowers");
                                for (Object rawItem : moisFlowersRaw) {
                                    if (rawItem instanceof Map) {
                                        moisFlowers.add((Map<String, Object>) rawItem);
                                    }
                                }
                            }

                            // Проверяем, есть ли уже элемент в списке
                            for (Map<String, Object> plantMap : moisFlowers) {
                                if (plantMap.get("id").equals(newElementId)) {
                                    // Если элемент уже есть в списке, выдаем сообщение об ошибке
                                    Toast.makeText(itemView.getContext(), "Цветок уже добавлен", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            Toast.makeText(itemView.getContext(), "Выберете дату и время последнего полива!", Toast.LENGTH_SHORT).show();

                            // Открываем DatePickerDialog для выбора даты
                            Calendar calendar = Calendar.getInstance();
                            DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(),
                                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                                        calendar.set(Calendar.YEAR, year);
                                        calendar.set(Calendar.MONTH, month);
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                        // Открываем TimePickerDialog для выбора времени
                                        TimePickerDialog timePickerDialog = new TimePickerDialog(itemView.getContext(),
                                                (TimePicker timeView, int hourOfDay, int minute) -> {
                                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                    calendar.set(Calendar.MINUTE, minute);

                                                    // Создаем карту с новым элементом и временем последнего полива
                                                    Map<String, Object> newElementMap = new HashMap<>();
                                                    newElementMap.put("id", newElementId);
                                                    newElementMap.put("lastWatered", calendar.getTimeInMillis());
                                                    int koofesiant_poliva = item.getKoofesiant_poliva(); // Замените это значение на нужное вам

                                                    // Вычисляем даты следующих поливов и добавляем их в ArrayList
                                                    ArrayList<Long> nextWateringDates = new ArrayList<>();
                                                    Calendar nextWateringCalendar = (Calendar) calendar.clone();
//                                                    for (int i = 1; i <= 10; i++) { // Например, добавим 10 следующих поливов
//                                                        nextWateringCalendar.add(Calendar.DAY_OF_YEAR, koofesiant_poliva);
//                                                        nextWateringDates.add(nextWateringCalendar.getTimeInMillis());
//                                                    }
                                                    newElementMap.put("nextWateringDates", nextWateringDates);

                                                    // Добавляем новый элемент в список
                                                    moisFlowers.add(newElementMap);

                                                    // Создаем карту для обновления данных пользователя
                                                    Map<String, Object> updateMap = new HashMap<>();
                                                    updateMap.put("moisFlowers", moisFlowers);

                                                    // Обновляем документ в Firestore
                                                    firestore.collection("users")
                                                            .document(currentUser.getUid())
                                                            .update(updateMap)
                                                            .addOnSuccessListener(unused -> {
                                                                Toast.makeText(itemView.getContext(), "Цветок добавлен", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(itemView.getContext(), "Цветок не добавлен", Toast.LENGTH_SHORT).show();
                                                            });
                                                },
                                                calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                true);
                                        timePickerDialog.show();
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.show();
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
