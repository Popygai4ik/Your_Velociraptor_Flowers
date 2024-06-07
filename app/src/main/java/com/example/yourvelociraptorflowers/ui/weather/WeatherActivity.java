package com.example.yourvelociraptorflowers.ui.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yourvelociraptorflowers.ui.weather.ChangeConsentActivity;
import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ActivityWeatherBinding;
import com.example.yourvelociraptorflowers.databinding.ResetLocationActivityBinding;
import com.example.yourvelociraptorflowers.domain.notification.weather.NotificationReceiver;
import com.example.yourvelociraptorflowers.ui.plants.illumination.IlluminationActivity;
import com.example.yourvelociraptorflowers.ui.user.location.ResetLocationActivity;
import com.google.android.material.button.MaterialButton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class WeatherActivity extends AppCompatActivity {
    private ActivityWeatherBinding binding;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private static final String PREF_KEY_NOTIFICATIONS = "notifications_shown_weather";
    private static final String PREF_KEY_DIALOG_SHOWN = "notification_dialog_shown";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация RequestQueue перед использованием
        requestQueue = Volley.newRequestQueue(this);


        // Инициализация SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Проверяем, отображался ли уже диалог уведомлений
        boolean dialogShown = sharedPreferences.getBoolean(PREF_KEY_DIALOG_SHOWN, false);

        boolean notificationsShown = sharedPreferences.getBoolean(PREF_KEY_NOTIFICATIONS, false);
        if (!notificationsShown && FirebaseAuth.getInstance().getCurrentUser() != null &&!dialogShown) {
            showNotificationDialog();
        }

        binding.changetheconsentButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherActivity.this, ChangeConsentActivity.class);
            intent.putExtra("city", getIntent().getStringExtra("city"));
            startActivity(intent);
        });


        String city = getIntent().getStringExtra("city");
        if (city != null) {
            binding.cityTextView.setText("🏡 " + city + " 🏡");
            getWeatherData(city);
        }

        binding.backButton.setOnClickListener(v -> onBackPressed());
    }

    // Метод для отображения диалогового окна с запросом о настройке уведомлений
    private void showNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Уведомления о погоде");
        builder.setIcon(R.drawable.ic_default_weather);
        builder.setMessage("Хотите ли вы получать уведомления с советом дня и о погоде каждые 24 часа в 12:00?");
        builder.setPositiveButton("Да", (dialog, which) -> {
            // Настройте уведомления здесь
            setupNotifications();
            // Установите флаг, чтобы показать, что диалог был отображен и пользователь согласился на уведомления
            sharedPreferences.edit().putBoolean(PREF_KEY_NOTIFICATIONS, true).apply();
            sharedPreferences.edit().putBoolean(PREF_KEY_DIALOG_SHOWN, true).apply();
            Log.d("WeatherActivity", "User agreed: " + sharedPreferences.getBoolean(PREF_KEY_NOTIFICATIONS, false));
        });
        builder.setNegativeButton("Нет", (dialog, which) -> {
            Toast.makeText(this, "😭 Вы отказались получать уведомления! 😭", Toast.LENGTH_SHORT).show();
            // Установите флаг, чтобы показать, что диалог был отображен и пользователь отказался от уведомлений
            sharedPreferences.edit().putBoolean(PREF_KEY_NOTIFICATIONS, false).apply();
            sharedPreferences.edit().putBoolean(PREF_KEY_DIALOG_SHOWN, true).apply();
            Log.d("WeatherActivity", "User disagreed: " + sharedPreferences.getBoolean(PREF_KEY_NOTIFICATIONS, false));
        });
        builder.show();
    }


    // Метод для настройки уведомлений
    // Метод для настройки уведомлений
    private void setupNotifications() {
        Toast.makeText(this, "🤩 Вы установили уведомления с советом дня! 🤩", Toast.LENGTH_SHORT).show();
//        // Создаем Intent для отправки уведомления
//        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Получаем доступ к AlarmManager
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        // Устанавливаем время первого уведомления (12 часов дня)
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 12);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        // Проверяем, чтобы уведомления отправлялись каждые 24 часа
//        long intervalMillis = AlarmManager.INTERVAL_DAY;
//
//        // Устанавливаем повторение уведомлений с заданным интервалом
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);

    }


    private void getWeatherData(String city) {
        String apiKey = "5808f06df095f6182e6691286088842f";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&lang=ru&units=metric";
        String urlTomorrow = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&lang=ru&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        binding.ScrollView.setVisibility(View.VISIBLE);
                        binding.errorLayout.setVisibility(View.GONE);

                        JSONObject main = response.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        int humidity = main.getInt("humidity");
                        String weatherDescription = response.getJSONArray("weather").getJSONObject(0).getString("description");
                        String iconCode = response.getJSONArray("weather").getJSONObject(0).getString("icon");
                        double windSpeed = response.getJSONObject("wind").getDouble("speed");

                        binding.temperatureTextView.setText("Температура за окном: " + temperature + "°C 🌡");
                        binding.humidityTextView.setText("Влажность: " + humidity + "% 💧");
                        binding.windSpeedTextView.setText("Скорость ветра: " + windSpeed + " м/с 🪁");

                        int weatherIconResource = getWeatherIconResource(iconCode);
                        binding.weatherIcon.setImageResource(weatherIconResource);

                        generateAdvice(weatherDescription, temperature);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.ScrollView.setVisibility(View.GONE);
                        binding.errorLayout.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            error.printStackTrace();
            binding.ScrollView.setVisibility(View.GONE);
            binding.errorLayout.setVisibility(View.VISIBLE);
            Toast.makeText(WeatherActivity.this, "Ошибка при получении данных о погоде. Код ошибки: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        binding.retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResetLocationActivity.class);
            String city2 = getIntent().getStringExtra("city");
            intent.putExtra("city", city2);
            startActivity(intent);
        });

        JsonObjectRequest jsonObjectRequestTomorrow = new JsonObjectRequest(Request.Method.GET, urlTomorrow, null,
                response -> {
                    try {
                        binding.ScrollView.setVisibility(View.VISIBLE);
                        binding.errorLayout.setVisibility(View.GONE);

                        JSONObject list = response.getJSONArray("list").getJSONObject(8); // Прогноз на завтра
                        JSONObject main = list.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        int humidity = main.getInt("humidity");
                        double windSpeed = list.getJSONObject("wind").getDouble("speed");
                        String weatherDescription = list.getJSONArray("weather").getJSONObject(0).getString("description");
                        String iconCode = list.getJSONArray("weather").getJSONObject(0).getString("icon");

                        boolean isSubscribed = sharedPreferences.getBoolean(PREF_KEY_NOTIFICATIONS, false);
                        updateSubscriptionStatus(isSubscribed);

                        binding.temperatureTomorrowTextView.setText("Температура на завтра в это же время: " + temperature + "°C 🌡");
                        binding.humidityTomorrowTextView.setText("Влажность завтра: " + humidity + "% 💧");
                        binding.windSpeedTomorrowTextView.setText("Скорость ветра завтра: " + windSpeed + " м/с 🪁");

                        int weatherIconResource = getWeatherIconResource(iconCode);
                        binding.weatherIconTomorrow.setImageResource(weatherIconResource);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.ScrollView.setVisibility(View.GONE);
                        binding.errorLayout.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            error.printStackTrace();
            binding.ScrollView.setVisibility(View.GONE);
            binding.errorLayout.setVisibility(View.VISIBLE);
            Toast.makeText(WeatherActivity.this, "Ошибка при получении данных о погоде на завтра. Код ошибки: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonObjectRequest);
        requestQueue.add(jsonObjectRequestTomorrow);
    }

    private void updateSubscriptionStatus(boolean isSubscribed) {
        if (isSubscribed) {
            binding.subscriptionTextView.setText("🤩 Вы согласны на уведомления о погоде 🤩");
        } else {
            binding.subscriptionTextView.setText("😭 Вы не согласны на уведомления о погоде 😭");
        }
    }
    // Метод для получения ресурса изображения на основе иконки погоды
    private int getWeatherIconResource(String iconCode) {
        switch (iconCode) {
            case "01d":
                return R.drawable._01d;
            case "01n":
                return R.drawable._01n;
            case "02d":
                return R.drawable._02d;
            case "02n":
                return R.drawable._02n;
            case "03d":
                return R.drawable._03d;
            case "03n":
                return R.drawable._03n;
            case "04d":
                return R.drawable._04d;
            case "04n":
                return R.drawable._04n;
            case "09d":
                return R.drawable._09d;
            case "09n":
                return R.drawable._09n;
            case "10d":
                return R.drawable._10d;
            case "10n":
                return R.drawable._10n;
            case "11d":
                return R.drawable._11d;
            case "11n":
                return R.drawable._11n;
            case "13d":
                return R.drawable._13d;
            case "13n":
                return R.drawable._13n;
            case "50d":
                return R.drawable._50d;
            case "50n":
                return R.drawable._50n;
            default:
                return R.drawable.ic_default_weather; // Убедитесь, что у вас есть иконка по умолчанию
        }
    }





    private void generateAdvice(String weatherDescription, double temperature) {
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
        if (temperature > 30) {
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
        binding.adviceTextView.setText(advice);
    }

//    private void loadImageWithGlide(String url, ImageView imageView) {
//        new Thread(() -> {
//            try {
//                URL imageUrl = new URL(url);
//                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                runOnUiThread(() -> Glide.with(this)
//                        .load(input)
//                        .placeholder(R.mipmap.ic_launcher)
//                        .error(R.drawable.flowers_icon_176905)
//                        .override(100, 100)
//                        .into(imageView));
//            } catch (IOException e) {
//                e.printStackTrace();
//                runOnUiThread(() -> Log.wtf("LOGGING", e.getMessage()));
//
//            }
//        }).start();
//    }
//private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//    ImageView bmImage;
//
//    public DownloadImageTask(ImageView bmImage) {
//        this.bmImage = bmImage;
//    }
//
//    protected Bitmap doInBackground(String... urls) {
//        String urldisplay = urls[0];
//        Bitmap mIcon11 = null;
//        try {
//            InputStream in = new java.net.URL(urldisplay).openStream();
//            mIcon11 = BitmapFactory.decodeStream(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mIcon11;
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        bmImage.setImageBitmap(result);
//    }
//}

}
