package com.example.yourvelociraptorflowers.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.yourvelociraptorflowers.databinding.ActivityNotificationsBinding;
import com.example.yourvelociraptorflowers.domain.notification.view.NotificationsAdapter;
import com.example.yourvelociraptorflowers.model.notification.Yvedomlenie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsActivity extends AppCompatActivity {
    private NotificationsAdapter adapter;
    private ActivityNotificationsBinding binding;
    private List<Yvedomlenie> yvedomlenieList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private String userId;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        adapter = new NotificationsAdapter(yvedomlenieList, this::deleteNotification);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
        binding.recycler.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.backButton.setOnClickListener(v -> onBackPressed());

        loadNotifications();

        binding.alldelit.setOnClickListener(v -> clearAllNotifications());
    }

    private void loadNotifications() {
        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.contains("notifications")) {
                        ArrayList<Map<String, Object>> notificationsList = (ArrayList<Map<String, Object>>) documentSnapshot.get("notifications");
                        Log.d("loadNotifications", "Notifications: " + notificationsList);
                        for (Map<String, Object> notificationWrapper : notificationsList) {
                            Map<String, Object> notificationMap = (Map<String, Object>) notificationWrapper.get("notification");
                            if (notificationMap != null) {
                                binding.textView.setVisibility(View.GONE);
                                String title = (String) notificationMap.get("title");
                                String message = (String) notificationMap.get("message");
                                String time = (String) notificationMap.get("timestamp");

                                // Логируем полученные значения
                                Log.d("loadNotifications", "Notification: " + title + ", " + message + ", " + time);

                                if (title != null && message != null && time != null) {
                                    yvedomlenieList.add(new Yvedomlenie(title, message, time));
                                } else {
                                    Log.e("loadNotifications", "One or more fields are null: title=" + title + ", message=" + message + ", time=" + time);
                                }
                            } else {
                                binding.textView.setVisibility(View.VISIBLE);
                                Log.e("loadNotifications", "Notification map is null");
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("loadNotifications", "Document does not contain 'notifications' field");
                    }
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recycler.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> Log.e("loadNotifications", "Error loading notifications", e));
    }



    private void deleteNotification(int position) {
        yvedomlenieList.remove(position);
        adapter.notifyItemRemoved(position);
        updateNotificationsInFirestore();
    }

    private void clearAllNotifications() {
        yvedomlenieList.clear();
        adapter.notifyDataSetChanged();
        updateNotificationsInFirestore();
    }

    private void updateNotificationsInFirestore() {
        ArrayList<Map<String, Object>> notificationsList = new ArrayList<>();
        for (Yvedomlenie notification : yvedomlenieList) {
            Map<String, Object> notificationWrapper = new HashMap<>();
            Map<String, Object> notificationMap = new HashMap<>();
            notificationMap.put("title", notification.getTitle());
            notificationMap.put("message", notification.getMessage());
            notificationMap.put("timestamp", notification.getTimestamp()); // изменено с "time" на "timestamp"
            notificationWrapper.put("notification", notificationMap); // Обертываем уведомление внутри объекта "notification"
            notificationsList.add(notificationWrapper);
        }
        firestore.collection("users").document(userId)
                .update("notifications", notificationsList)
                .addOnSuccessListener(aVoid -> Log.d("NotificationsActivity", "Notifications updated successfully"))
                .addOnFailureListener(e -> Log.e("NotificationsActivity", "Error updating notifications", e));
    }}


