package com.example.yourvelociraptorflowers.domain.notification.worker;
import static com.example.yourvelociraptorflowers.domain.Moiplants.PlantsViewHolderMoi.getNextWateringDate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.model.Yvedomlenie;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public Result doWork() {
        if (mAuth.getCurrentUser() != null) {
            CountDownLatch latch = new CountDownLatch(1);
            DocumentReference userRef = firestore.collection("users").document(mAuth.getCurrentUser().getUid());
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
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
                                                ArrayList<String> formattedDates = new ArrayList<>();
                                                for (Long dateInt : nextWateringDates) {
                                                    Date date = new Date(dateInt);
                                                    formattedDates.add(sdf.format(date));
                                                }
                                                plant.setLastWatered(formattedDates);
                                                plants.add(plant);
                                            }
                                        }
                                        updateNotification(plants);
                                    }
                                }
                                latch.countDown();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("NotificationWorker", "Error fetching plants", e);
                                latch.countDown();
                            });
                } else {
                    latch.countDown();
                }
            }).addOnFailureListener(e -> {
                Log.e("NotificationWorker", "Error fetching user data", e);
                latch.countDown();
            });
            try {
                latch.await();
            } catch (InterruptedException e) {
                Log.e("NotificationWorker", "Latch interrupted", e);
                return Result.failure();
            }
            scheduleNextWork();
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    private void updateNotification(List<Plants> plants) {
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_for_poliv", "My Channel poliv", NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null); // Отключение звука
            notificationManager.createNotificationChannel(channel);
        }

        ArrayList<String> notificationLines = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        String currentTimeNormal = sdf2.format(calendar.getTime());

        long oneHourInMillis = 60 * 60 * 1000;
        long oneMinuteInMillis = 60 * 1000;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;

        for (Plants plant : plants) {
            String plantId = plant.getId();
            String nextWateringTimestamp = getNextWateringDate(plant.getLastWateredFormatted(), plant.getKoofesiant_poliva());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            long nextWateringTimestampLong;
            try {
                Date date = sdf.parse(nextWateringTimestamp);
                if (date != null) {
                    nextWateringTimestampLong = date.getTime();
                } else {
                    throw new ParseException("Date parsing returned null", 0);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            String formattedDate = formatDate(nextWateringTimestamp);
            notificationLines.add(plant.getName() + " - " + formattedDate);

            if (nextWateringTimestampLong - currentTime <= oneHourInMillis && nextWateringTimestampLong - currentTime > oneMinuteInMillis) {
                if (!prefs.getBoolean(plantId + "_hourly_notified", false)) {
                    createScheduledNotification(context, plant, nextWateringTimestampLong - oneHourInMillis, "До полива меньше часа!");
                    Map<String, Object> newNotificationMap = new HashMap<>();
                    Yvedomlenie notification = new Yvedomlenie("💧 Напоминание о поливе 📢", plant.getName() + ": До полива меньше часа!", currentTimeNormal);
                    newNotificationMap.put("notification", notification);    // Запись нового уведомления в Firestore
                    firestore.collection("users")
                            .document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                ArrayList<Map<String, Object>> notifications;
                                if (documentSnapshot.contains("notifications")) {
                                    notifications = (ArrayList<Map<String, Object>>) documentSnapshot.get("notifications");
                                } else {
                                    notifications = new ArrayList<>();
                                }
                                notifications.add(newNotificationMap);
                                Map<String, Object> updateMap = new HashMap<>();
                                updateMap.put("notifications", notifications);
                                firestore.collection("users")
                                        .document(userId)
                                        .update(updateMap)
                                        .addOnSuccessListener(unused -> {
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("NotificationWorker", "Error updating notifications", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("NotificationWorker", "Error getting user notifications", e);
                            });
                    editor.putBoolean(plantId + "_hourly_notified", true);
                    editor.apply();
                }
            } else {
                editor.putBoolean(plantId + "_hourly_notified", false);
                editor.apply();
            }

            if (nextWateringTimestampLong - currentTime <= oneMinuteInMillis && nextWateringTimestampLong - currentTime > 0) {
                if (!prefs.getBoolean(plantId + "_minute_notified", false)) {
                    createScheduledNotification(context, plant, nextWateringTimestampLong - oneMinuteInMillis, "До полива меньше минуты!");
                    Map<String, Object> newNotificationMap2 = new HashMap<>();
                    Yvedomlenie notification2 = new Yvedomlenie("💧 Напоминание о поливе 📢", plant.getName() + ": До полива меньше минуты!", currentTimeNormal);
                    newNotificationMap2.put("notification", notification2);    // Запись нового уведомления в Firestore
                    firestore.collection("users")
                            .document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                ArrayList<Map<String, Object>> notifications;
                                if (documentSnapshot.contains("notifications")) {
                                    notifications = (ArrayList<Map<String, Object>>) documentSnapshot.get("notifications");
                                } else {
                                    notifications = new ArrayList<>();
                                }
                                notifications.add(newNotificationMap2);
                                Map<String, Object> updateMap = new HashMap<>();
                                updateMap.put("notifications", notifications);
                                firestore.collection("users")
                                        .document(userId)
                                        .update(updateMap)
                                        .addOnSuccessListener(unused -> {
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("NotificationWorker", "Error updating notifications", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("NotificationWorker", "Error getting user notifications", e);
                            });
                    editor.putBoolean(plantId + "_minute_notified", true);
                    editor.apply();
                } else {
                    createScheduledNotification(context, plant, nextWateringTimestampLong, "Пора поливать!");

                    Map<String, Object> newNotificationMap1 = new HashMap<>();

                    calendar.add(Calendar.MINUTE, 1);
                    String currentTimeNormal2 = sdf2.format(calendar.getTime());

                    Yvedomlenie notification = new Yvedomlenie("💧 Напоминание о поливе 📢", plant.getName() + ": Пора поливать!", currentTimeNormal2);

                    newNotificationMap1.put("notification", notification);

// Запись новых уведомлений в Firestore
                    firestore.collection("users")
                            .document(userId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                ArrayList<Map<String, Object>> notifications;
                                if (documentSnapshot.contains("notifications")) {
                                    notifications = (ArrayList<Map<String, Object>>) documentSnapshot.get("notifications");
                                } else {
                                    notifications = new ArrayList<>();
                                }
                                notifications.add(newNotificationMap1);

                                Map<String, Object> updateMap = new HashMap<>();
                                updateMap.put("notifications", notifications);

                                firestore.collection("users")
                                        .document(userId)
                                        .update(updateMap)
                                        .addOnSuccessListener(unused -> {
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("NotificationWorker", "Error updating notifications", e);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("NotificationWorker", "Error getting user notifications", e);
                            });

                    editor.putBoolean(plantId + "_time_to_water_notified", true);
                    editor.apply();
                }
            } else {
                editor.putBoolean(plantId + "_minute_notified", false);
                editor.apply();
            }
        }

        if (!notificationLines.isEmpty()) {
            StringBuilder notificationText = new StringBuilder();
            for (String line : notificationLines) {
                notificationText.append(line).append("\n");
            }

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("fragment", "Moi_tviti_Fragment");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(context, "my_channel_for_poliv")
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setContentTitle("💧 Вам нужно поливать:")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText.toString()))
                    .setOngoing(true) // делает уведомление неотключаемым
                    .setSound(null)   // отключение звука для уведомления
                    .setAutoCancel(false) // уведомление не удаляется при нажатии
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Видимость уведомления на заблокированном экране
                    .build();

            notification.flags |= Notification.FLAG_NO_CLEAR; // делает уведомление неотключаемым
            notificationManager.notify(1, notification);
        }
    }

    private void scheduleNextWork() {
        OneTimeWorkRequest nextWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("NotificationWorker", ExistingWorkPolicy.REPLACE, nextWorkRequest);
    }

    private static String formatDate(String dateStr) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy 'в' HH:mm", new Locale("ru", "RU"));
        Date date;
        try {
            date = originalFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr; // Возвращаем исходную строку в случае ошибки
        }
        return targetFormat.format(date);
    }

    private void createScheduledNotification(Context context, Plants plant, long triggerTime, String message) {
        NotificationManager notificationManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel2 = new NotificationChannel("my_channel_for_poliv2", "My Channel poliv2", NotificationManager.IMPORTANCE_HIGH);
            notificationManager2.createNotificationChannel(channel2);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fragment", "Moi_tviti_Fragment");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(context, "my_channel_for_poliv2")
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle("💧 Напоминание о поливе 📢")
                .setContentText(plant.getName() + ": " + message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Видимость уведомления на заблокированном экране
                .build();

        notificationManager2.notify((int) (triggerTime / 1000), notification); // Используем triggerTime для идентификации уведомления
    }
}
