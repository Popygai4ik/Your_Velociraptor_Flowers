package com.example.yourvelociraptorflowers.domain.notification.weather;

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

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.model.notification.Notify;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WeatherNotificationManager {

    private static final String LAST_NOTIFICATION_KEY = "last_weather_notification_time";
    private static final String NOTIFICATION_CHANNEL_ID = "weather_channel";
    private static final String API_KEY = "5808f06df095f6182e6691286088842f";
    private RequestQueue requestQueue;
    private FirebaseFirestore firestore;
    private String userId;

    public WeatherNotificationManager(Context context, String userId) {
        // Инициализация RequestQueue и Firestore
        requestQueue = Volley.newRequestQueue(context);
        firestore = FirebaseFirestore.getInstance();
        this.userId = userId;
    }

    public void showDailyWeatherNotification(Context context, String city) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        long lastNotificationTime = preferences.getLong(LAST_NOTIFICATION_KEY, 0);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        fetchWeatherData(context, city);

//        // Проверка, если текущее время больше 12:00 и уведомление еще не было отправлено сегодня
//        if (currentHour >= 12 && !isSameDay(lastNotificationTime)) {
//
//            Log.wtf("LOGG", String.valueOf(!isSameDay(lastNotificationTime)));
//            preferences.edit().putLong(LAST_NOTIFICATION_KEY, System.currentTimeMillis()).apply();
//        }
    }

    public boolean isSameDay(long timeInMillis) {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar lastNotificationCalendar = Calendar.getInstance();
        lastNotificationCalendar.setTimeInMillis(timeInMillis);

        return currentCalendar.get(Calendar.YEAR) == lastNotificationCalendar.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.DAY_OF_YEAR) == lastNotificationCalendar.get(Calendar.DAY_OF_YEAR);
    }

    public void fetchWeatherData(Context context, String city) {
        String urlCurrent = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&lang=ru&units=metric";
        String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&lang=ru&units=metric";

        JsonObjectRequest jsonObjectRequestCurrent = new JsonObjectRequest(Request.Method.GET, urlCurrent, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        getForecastAndNotify(context, city, temperature);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });

        requestQueue.add(jsonObjectRequestCurrent);
    }

    public void getForecastAndNotify(Context context, String city, double currentTemperature) {
        String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&lang=ru&units=metric";

        JsonObjectRequest jsonObjectRequestForecast = new JsonObjectRequest(Request.Method.GET, urlForecast, null,
                response -> {
                    try {
                        JSONArray listArray = response.getJSONArray("list");
                        if (listArray.length() > 8) {
                            JSONObject list = listArray.getJSONObject(8); // Прогноз на завтра
                            JSONObject main = list.getJSONObject("main");
                            double temperatureTomorrow = main.getDouble("temp");

                            JSONArray weatherArray = list.getJSONArray("weather");
                            if (weatherArray.length() > 0) {
                                String weatherDescription = weatherArray.getJSONObject(0).getString("description");

                                String advice = generateAdvice(weatherDescription, temperatureTomorrow);

                                // Формируем текст уведомления
                                String notificationText = "Текущая температура за окном: " + currentTemperature + "°C 🌡\n" +
                                        "Температура на завтра: " + temperatureTomorrow + "°C 🌡\n" +
                                        "Совет дня: " + advice;

                                // Отображаем уведомление
                                showNotification(context, notificationText);

                                // Сохраняем уведомление в Firestore
                                saveNotificationToFirestore(notificationText);
                            } else {
                                Log.e("WeatherNotification", "Weather array is empty");
                            }
                        } else {
                            Log.e("WeatherNotification", "List array does not contain enough elements");
                        }
                    } catch (JSONException e) {
                        Log.e("WeatherNotification", "JSON parsing error", e);
                    }
                },
                error -> Log.e("WeatherNotification", "Volley error", error)
        );

        Volley.newRequestQueue(context).add(jsonObjectRequestForecast);
    }



    public void saveNotificationToFirestore(String notificationText) {
        Map<String, Object> newNotificationMap2 = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        String currentTimeNormal = sdf2.format(calendar.getTime());
        Notify notification2 = new Notify("😎 Ежедневный совет на основе погоды 😎", notificationText, currentTimeNormal);
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
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", "😎 Ежедневный совет на основе погоды 😎");
        notification.put("text", notificationText);
        notification.put("timestamp", System.currentTimeMillis());

        firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> {
                    Log.wtf("LOGGING", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Ошибка при добавлении
                    e.printStackTrace();
                });
    }

    public String generateAdvice(String weatherDescription, double temperature) {
        String[] hotAdvice = {
                "Сегодня жарко. Растения могут испытывать стресс от жары, увлажните их почву. ☀️",
                "При высокой температуре увеличьте частоту опрыска растений.💧",
                "Подумайте о тени для ваших растений в жаркий день. 🌿",
                "Высокие температуры могут привести к пересыханию почвы, увлажняйте ее. 💧",
                "Избегайте прямых солнечных лучей на ваших растениях во время жары. 🌞"
        };

        String[] rainAdvice = {
                "Сегодня ожидается дождь. Ваши растения получат дополнительную влагу от природы. 🌧️",
                "После дождя убедитесь, что ваши растения не застоялись в воде. 🌿",
                "Ваши растения будут благодарны за дополнительную влагу от природы. 🌧️",
                "После дождя обязательно проверьте почву на ваших растениях. 🌿"
        };

        String[] clearAdvice = {
                "Сегодня ясная погода. Поддерживайте растения на свету, чтобы они получили достаточно света. 🌞",
                "Используйте ясный день для удобрения ваших растений. 🌿",
                "Ясный день - прекрасное время для проведения работы в саду. 🌳",
                "Сегодня отличный день для обрезки растений. 🌿",
                "Ваши растения будут рады ясному дню и дополнительному свету. 🌞"
        };

        String[] cloudAdvice = {
                "Сегодня облачно. Ваши растения могут нуждаться в дополнительном освещении. Проверьте уровень освещения ваших растений. 🌥️",
                "Облачный день - отличное время для проверки влажности почвы ваших растений. 💧",
                "Ваши растения могут наслаждаться облачным днем, но следите за их световым режимом. 🌿",
                "Облачный день - время для обновления подпитки ваших растений. 🌱",
                "Сегодня облачно. Рассмотрите возможность увлажнения воздуха вокруг ваших растений. 💧"
        };

        String[] defaultAdvice = {
                "Сегодня хорошая погода. Не забудьте уделить внимание вашим растениям. Проверьте уровень освещения ваших растений и убедитесь, что чувствуете себя комфортно. 🌿",
                "Проведите время с вашими растениями, чтобы выяснить, что им нужно сегодня. 🌱",
                "Независимо от погоды, ваши растения ожидают вашего внимания и ухода. Проверьте уровень освещения ваших растений и убедитесь, что чувствуете себя комфортно. 🌿",
                "Сегодня - прекрасный день для обновления внутреннего украшения вашего дома с помощью растений. Также проверьте уровень освещения ваших растений и убедитесь, что чувствуете себя комфортно. 🌱",
                "Рассмотрите возможность создания микроклимата вокруг ваших растений, чтобы обеспечить им оптимальные условия. 🌿"
        };

        String advice;
        if (temperature > 25) {
            advice = hotAdvice[(int) (Math.random() * hotAdvice.length)];
        } else if (weatherDescription.contains("rain")) {
            advice = rainAdvice[(int) (Math.random() * rainAdvice.length)];
        } else if (weatherDescription.contains("clear")) {
            advice = clearAdvice[(int) (Math.random() * clearAdvice.length)];
        } else if (weatherDescription.contains("cloud")) {
            advice = cloudAdvice[(int) (Math.random() * cloudAdvice.length)];
        } else {
            advice = defaultAdvice[(int) (Math.random() * defaultAdvice.length)];
        }
        return advice;
    }

    public void showNotification(Context context, String text) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Weather Updates", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fragment", "Moi_tviti_Fragment");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_notification_round)
                .setContentTitle("😎 Ежедневный совет на основе погоды 😎")
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(101, notification);
    }
}
