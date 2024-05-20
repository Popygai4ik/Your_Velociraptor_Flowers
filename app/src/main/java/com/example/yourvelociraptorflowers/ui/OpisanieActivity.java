package com.example.yourvelociraptorflowers.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.OpisanieActivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpisanieActivity extends AppCompatActivity {

    private TextView opisanieTextView;
    private OpisanieActivityBinding binding;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OpisanieActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });


        // Получаем данные из интента
        String Id = getIntent().getStringExtra("Id");
        String opisanie = getIntent().getStringExtra("opisanie");
        String name = getIntent().getStringExtra("name");
        String temp = getIntent().getStringExtra("temp");
        String vlag = getIntent().getStringExtra("vlag");
        String ysl = getIntent().getStringExtra("ysl");
        String resinok = getIntent().getStringExtra("resinok");
        String resinok2 = getIntent().getStringExtra("resinok2");
        String resinok3 = getIntent().getStringExtra("resinok3");
        String resinok4 = getIntent().getStringExtra("resinok4");
        String youtube = getIntent().getStringExtra("opisanie5");
//        Toast.makeText(getApplicationContext(), vlag, Toast.LENGTH_SHORT).show();

        // Отображаем данные
        binding.textViewTempInf.setText(temp);
        binding.textViewvlaginf.setText(vlag);
        binding.imageViewVarchInf.setText(ysl);
        binding.title.setText(name);
        binding.textViewOpisanie.setText(opisanie);

        Glide.with(this)
                .load(resinok)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewOpisanie);

        Glide.with(this)
                .load(resinok2)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewTemp);
        Glide.with(this)
                .load(resinok3)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewVlag);
        Glide.with(this)
                .load(resinok4)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewVarch);
        binding.addButton.setOnClickListener(v -> {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if (currentUser != null) {
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                        // Создаем новый элемент для добавления в список
                        String id = getIntent().getStringExtra("Id");

                        // Получаем текущий список из Firestore
                        firestore.collection("users")
                                .document(currentUser.getUid())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
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
                                        if (plantMap.get("id").equals(id)) {
                                            Toast.makeText(this, "Цветок уже добавлен", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    Toast.makeText(this, "Выберете дату и время последнего полива!", Toast.LENGTH_SHORT).show();

                                    // Открываем DatePickerDialog для выбора даты
                                    Calendar calendar = Calendar.getInstance();
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                                            (DatePicker view, int year, int month, int dayOfMonth) -> {
                                                calendar.set(Calendar.YEAR, year);
                                                calendar.set(Calendar.MONTH, month);
                                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                                // Открываем TimePickerDialog для выбора времени
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                                                        (TimePicker timeView, int hourOfDay, int minute) -> {
                                                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                            calendar.set(Calendar.MINUTE, minute);

                                                            // Создаем карту с новым элементом и временем последнего полива
                                                            Map<String, Object> newElementMap = new HashMap<>();
                                                            newElementMap.put("id", id);
                                                            newElementMap.put("lastWatered", calendar.getTimeInMillis());
                                                            // Получаем коэффициент полива
                                                            int koofesiant_poliva = getIntent().getIntExtra("koofesiant_poliva", 1); // Замените это значение на нужное вам

                                                            // Вычисляем даты следующих поливов и добавляем их в ArrayList
                                                            ArrayList<Long> nextWateringDates = new ArrayList<>();
                                                            Calendar nextWateringCalendar = (Calendar) calendar.clone();
                                                            for (int i = 1; i <= 10; i++) { // Например, добавим 10 следующих поливов
                                                                nextWateringCalendar.add(Calendar.DAY_OF_YEAR, koofesiant_poliva);
                                                                nextWateringDates.add(nextWateringCalendar.getTimeInMillis());
                                                            }
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
                                                                        Toast.makeText(this, "Цветок добавлен", Toast.LENGTH_SHORT).show();
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        Toast.makeText(this, "Ошибка при добавлении цветка", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                                });

            } else {
                // Пользователь не вошел, выводим сообщение
                Toast.makeText(this, "Войдите, чтобы добавить цветок", Toast.LENGTH_SHORT).show();
            }
        });
        binding.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создайте намерение для открытия YouTube
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Если приложение YouTube не установлено, вы можете предложить пользователю установить его
                    Toast.makeText(getApplicationContext(), "Приложение YouTube не установлено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}