package com.example.yourvelociraptorflowers.domain.notification.alarmnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yourvelociraptorflowers.domain.notification.alarmnotification.MyNewIntentService;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String plantName = intent.getStringExtra("plant_name");
            Log.d("LOGG", "Received intent for plant: " + plantName);
            Intent serviceIntent = new Intent(context, MyNewIntentService.class);
            serviceIntent.putExtra("plant_name", plantName);
            context.startService(serviceIntent);
        } else {
            Log.e("LOGG", "Received null intent");
        }
    }
}
