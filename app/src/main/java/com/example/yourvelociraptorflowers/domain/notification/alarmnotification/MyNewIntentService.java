package com.example.yourvelociraptorflowers.domain.notification.alarmnotification;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.ui.MainActivity;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            Log.e("LOGG", "Intent is null");
            return;
        }

        String plantName = intent.getStringExtra("plant_name");
        Log.d("LOGG", "Handling intent for plant: " + plantName);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "watering_notification_channel")
                .setContentTitle("Пора поливать растение!")
                .setContentText("Не забудьте полить растение: " + plantName)
                .setSmallIcon(R.mipmap.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LOGG", "Missing POST_NOTIFICATIONS permission");
            return;
        }

        managerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
