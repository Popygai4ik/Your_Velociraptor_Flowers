package com.example.yourvelociraptorflowers.domain.notification.weather;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.yourvelociraptorflowers.R;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "weather_notification_channel";
    private static final int NOTIFICATION_ID = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получаем данные о погоде
        String currentTemperature = getCurrentTemperature();
        String tomorrowTemperature = getTomorrowTemperature();
        String advice = generateAdvice();

        // Формируем текст уведомления
        String notificationText = "Текущая температура: " + currentTemperature + "°C\n" +
                "Температура на завтра: " + tomorrowTemperature + "°C\n" +
                "Совет дня: " + advice;

        // Отображаем уведомление
        showNotification(context, notificationText);
    }


    private void showNotification(Context context, String text) {
        // Создаем канал уведомлений (для Android O и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Weather Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Строим уведомление
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_weather)
                .setContentTitle("Погодное уведомление")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Отображаем уведомление
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    private String getCurrentTemperature() {
        // В данном методе вы можете получить текущую температуру, например, из API или из какого-то другого источника данных
        // Здесь я просто верну строку в качестве примера
        return "25";
    }

    private String getTomorrowTemperature() {
        // Аналогично предыдущему методу, здесь вы можете получить прогноз на завтрашний день
        // Я также просто верну строку в качестве примера
        return "27";
    }

    private String generateAdvice() {
        // В этом методе вы можете сгенерировать совет на основе текущей погоды или других факторов
        // Я опять же просто верну строку в качестве примера
        return "Сегодня хороший день для прогулок!";
    }

}
