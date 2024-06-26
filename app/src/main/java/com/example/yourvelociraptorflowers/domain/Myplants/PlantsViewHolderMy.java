package com.example.yourvelociraptorflowers.domain.Myplants;

import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ItemMyBinding;
import com.example.yourvelociraptorflowers.model.plant.Plants;
import com.example.yourvelociraptorflowers.ui.plants.illumination.IlluminationActivity;
import com.example.yourvelociraptorflowers.ui.plants.podrobnie.MoreDetailedActivity;
import com.example.yourvelociraptorflowers.ui.plants.question.Activity_Question_For_Expert;
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
import java.util.Locale;
import java.util.Map;

public class PlantsViewHolderMy extends ViewHolder {
    private ItemMyBinding binding;

    public PlantsViewHolderMy(ItemMyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public static String getNextWateringDate(String lastWatered, int koofesiant_poliva) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date lastWateredDate = dateFormat.parse(lastWatered);
            Date currentDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastWateredDate);

            while (calendar.getTime().before(currentDate)) {
                calendar.add(Calendar.DAY_OF_YEAR, koofesiant_poliva);
            }

            Date nextWateringDate = calendar.getTime();
            DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            return outputFormat.format(nextWateringDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }

    // Функция для расчета последней даты полива
    public static String getLastWateringDate(String lastWatered, int koofesiant_poliva) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            Date lastWateredDate = dateFormat.parse(lastWatered);
            Date currentDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastWateredDate);

            Date lastValidWateringDate = lastWateredDate;

            while (calendar.getTime().before(currentDate)) {
                lastValidWateringDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_YEAR, koofesiant_poliva);
            }

            DateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            return outputFormat.format(lastValidWateringDate);

        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
    public void bind(Plants item) {

        binding.name.setText(item.getOpisanie());
        binding.opisanie.setText(item.getName());
        binding.lastWatered.setText("\uD83D\uDCA7Последний полив: " + getLastWateringDate(item.getLastWateredFormatted(), item.getKoofesiant_poliva()));

        binding.nextWatered.setText("\uD83D\uDCA7Следующий полив: " + getNextWateringDate(item.getLastWateredFormatted(), item.getKoofesiant_poliva()));
        // Установка уведомления
//        try {

//            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
////            Date test = dateFormat.parse("24.05.2024 00:15");
//            Date nextWateringDate = dateFormat.parse(getNextWateringDate(item.getLastWateredFormatted(), item.getKoofesiant_poliva()));
////            setWateringAlarm(itemView.getContext(), "test",  test);
//            setWateringAlarm(itemView.getContext(), item.getName(), nextWateringDate);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
            // Load image
            Glide.with(itemView)
                    .load(item.getResinok())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.resinok);
            binding.illuminationButton.setText("Проверить освещенность для " + item.getName() + " \uD83D\uDE0E");
            // Set Listener
            binding.moreButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), MoreDetailedActivity.class);
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
            binding.illuminationButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), IlluminationActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("resinok", item.getResinok());
                intent.putExtra("coefficient_illumination", item.getCoefficient_illumination());
//                Log.wtf("LOGG", String.valueOf(item.getCoefficient_illumination()));
                itemView.getContext().startActivity(intent);

            });
            binding.questionButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), Activity_Question_For_Expert.class);
                intent.putExtra("name", item.getName());
                itemView.getContext().startActivity(intent);
            });
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
            // Метод для установки будильника на уведомление

        }
//    private void setWateringAlarm(Context context, String plantName, Date nextWateringDate) {
//        try {
//            // Create an Intent for the alarm
//            Intent intent = new Intent(context, PolivAlarmReceiver.class);
//            intent.putExtra("plant_name", plantName);
//            // Create a PendingIntent with FLAG_IMMUTABLE
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                    context,
//                    0,
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//            );
//
//            // Get the AlarmManager service
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//            if (alarmManager != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setExactAndAllowWhileIdle(
//                            AlarmManager.RTC_WAKEUP,
//                            nextWateringDate.getTime(),
//                            pendingIntent
//                    );
//                } else {
//                    alarmManager.setExact(
//                            AlarmManager.RTC_WAKEUP,
//                            nextWateringDate.getTime(),
//                            pendingIntent
//                    );
//                }
//                Log.d("setWateringAlarm", "Alarm set for: " + nextWateringDate);
//            } else {
//                Log.e("setWateringAlarm", "AlarmManager is null");
//            }
//        } catch (Exception e) {
//            Log.e("setWateringAlarm", "Error setting alarm", e);
//        }
////    }
//@SuppressLint("ScheduleExactAlarm")
//private void setWateringAlarm(Context context, String plantName, Date nextWateringDate) {
//    Intent notifyIntent = new Intent(context, MyReceiver.class);
//    notifyIntent.putExtra("plant_name", plantName);
//
//    PendingIntent pendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            notifyIntent,
//            PendingIntent.FLAG_MUTABLE
//    );
//
//    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//    if (alarmManager != null) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
//                alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP,
//                        nextWateringDate.getTime(),
//                        pendingIntent
//                );
//            } else {
//                Log.e("LOGG", "Missing SCHEDULE_EXACT_ALARM permission");
//            }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    nextWateringDate.getTime(),
//                    pendingIntent
//            );
//        } else {
//            alarmManager.setExact(
//                    AlarmManager.RTC_WAKEUP,
//                    nextWateringDate.getTime(),
//                    pendingIntent
//            );
//        }
//        Log.d("LOGG", "Alarm set for: " + nextWateringDate);
//    } else {
//        Log.e("LOGG", "AlarmManager is null");
//    }
//}




}