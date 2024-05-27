package com.example.yourvelociraptorflowers.domain.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.yourvelociraptorflowers.R;

public class PolivAlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "watering_notification_channel";
    private static final String TAG = "PolivAlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String plantName = intent.getStringExtra("plant_name");
            if (plantName != null) {
                Toast.makeText(context, "Time to water: " + plantName, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Received intent with plant_name: " + plantName);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (notificationManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Poliv Notifications", NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
                    }

                    Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_notification)
                            .setContentTitle("Пора поливать растение!")
                            .setContentText("Не забудьте полить растение: " + plantName)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .build();

                    int notificationId = (int) System.currentTimeMillis();  // Use unique notification ID
                    notificationManager.notify(notificationId, notification);
                } else {
                    Log.e(TAG, "NotificationManager is null");
                }
            } else {
                Toast.makeText(context, "Plant name is missing in the intent", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Plant name is missing in the intent");
            }
        } else {
            Toast.makeText(context, "Received null intent", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Received null intent");
        }
    }
}
