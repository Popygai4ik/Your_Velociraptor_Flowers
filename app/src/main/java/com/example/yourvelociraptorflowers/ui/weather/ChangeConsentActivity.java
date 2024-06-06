package com.example.yourvelociraptorflowers.ui.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.R;

public class ChangeConsentActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button agreeButton;
    private Button disagreeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        setContentView(R.layout.activity_change_consent);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        agreeButton = findViewById(R.id.agreeButton);
        disagreeButton = findViewById(R.id.disagreeButton);

        agreeButton.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("notifications_shown_weather", true).apply();
            Log.d("ChangeConsentActivity", "Notifications consent set to true");
            Intent intent2 = new Intent(this, WeatherActivity.class);
            intent2.putExtra("city", city);
            startActivity(intent2);
            finish();
        });

        disagreeButton.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("notifications_shown_weather", false).apply();
            Log.d("ChangeConsentActivity", "Notifications consent set to false");
            Intent intent3 = new Intent(this, WeatherActivity.class);
            intent3.putExtra("city", city);
            startActivity(intent3);
            finish(); // Закрывает Activity и возвращает пользователя в предыдущее Activity
        });
    }
}
