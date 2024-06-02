package com.example.yourvelociraptorflowers.ui.fragment;

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
import android.widget.Toast;

import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.FragmentMoiTvitiBinding;
import com.example.yourvelociraptorflowers.domain.Moiplants.PlantsAdapterMoi;
import com.example.yourvelociraptorflowers.model.plant.Plants;
import com.example.yourvelociraptorflowers.ui.notifications.NotificationsActivity;
import com.example.yourvelociraptorflowers.ui.plants.search.Search_activity;
import com.example.yourvelociraptorflowers.ui.user.viewing.User_profile;
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
            Intent intent = new Intent(requireContext(), User_profile.class);
            startActivity(intent);
        });

        binding.searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Search_activity.class);
            startActivity(intent);
        });
        binding.notificationButton.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(requireContext(), NotificationsActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(requireContext(), "Для просмотра уведомлений войдите в аккаунт", Toast.LENGTH_SHORT).show();
            }

        });
        checkNotifications();

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

//                                        NotificationChannel channel = null;
//                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            channel = new NotificationChannel("my_channel_for_poliv", "My Channel poliv", NotificationManager.IMPORTANCE_LOW);
//                                            channel.setSound(null, null); // Отключение звука для канала
//                                        }
//                                        if (isAdded()) {
//                                            NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                                            // Остальной код
//                                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            notificationManager.createNotificationChannel(channel);
//                                        }
//
//                                        ArrayList<String> notificationLines = new ArrayList<>();
//                                        for (Plants plant1 : plants) {
//                                            notificationLines.add(plant1.getName() + " - " + getNextWateringDate(plant1.getLastWateredFormatted(), plant1.getKoofesiant_poliva()));
//                                        }
//
//                                        if (notificationLines.size() > 0) {
//                                            StringBuilder notificationText = new StringBuilder();
//                                            for (String line : notificationLines) {
//                                                notificationText.append(line).append("\n");
//                                            }
//
//                                            Notification notification1 = new NotificationCompat.Builder(requireContext(), "my_channel_for_poliv")
//                                                    .setSmallIcon(R.mipmap.ic_notification)
//                                                    .setContentTitle("\uD83D\uDCA7 Вам нужно поливать:")
//                                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText.toString()))
//                                                    .setOngoing(true)  // делает уведомление неотключаемым
//                                                    .setSound(null)    // отключение звука для уведомления
//                                                    .build();
//
//                                            notification1.flags |= Notification.FLAG_NO_CLEAR;  // делает уведомление неотключаемым
//
//                                            notificationManager.notify(1, notification1);
//                                        }}


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
                Intent intent = new Intent(requireContext(), User_profile.class);
                startActivity(intent);
            });
        }
    }
    private void checkNotifications() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()) // Замените "userId" на текущий идентификатор пользователя
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.contains("notifications")) {
                            ArrayList<Map<String, Object>> notificationsList = (ArrayList<Map<String, Object>>) documentSnapshot.get("notifications");
                            if (notificationsList != null && !notificationsList.isEmpty()) {
                                binding.notificationButton.setImageResource(R.drawable.free_icon_notifications_5189239); // Измените на ваш ресурс активной иконки уведомлений
                            } else {
                                Log.wtf("Firestore", "Notifications list is null or empty");
                            }
                        } else {
                            Log.e("Firestore", "Document does not contain 'notifications' field");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error checking notifications.", e));
        }}
}
