package com.example.yourvelociraptorflowers.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ActivityMainBinding;
import com.example.yourvelociraptorflowers.domain.notification.worker.NotificationWorker;
import com.example.yourvelociraptorflowers.ui.fragment.My_Flowers_Fragment;
import com.example.yourvelociraptorflowers.ui.fragment.All_Flowers_Fragment;
import com.example.yourvelociraptorflowers.ui.start.WelcomeActivity;
import com.example.yourvelociraptorflowers.ui.weather.WeatherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_DIALOG_SHOWN = "dialog_shown_count";
    private static final int MAX_DIALOG_SHOWS = 5;
    private ActivityMainBinding binding;
    private static final int REQUEST_CODE_ALARM_PERMISSION = 1;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверка на первый запуск приложения
        SharedPreferences preferences = getSharedPreferences("appPreferences", MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return; // Остановить выполнение, чтобы не загружать MainActivity
        }else {
            showNotificationAccessDialog();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startNotificationWorker();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Пользователь залогинен
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("name");
                                if (userName != null) {
                                    Toast.makeText(this, "С возвращением, " + userName + "!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            // Пользователь не залогинен
            Toast.makeText(this, "Добро пожаловать, новый пользователь!", Toast.LENGTH_SHORT).show();
        }

        // Проверка интента и установка правильного фрагмента
        if (getIntent() != null && "Moi_tviti_Fragment".equals(getIntent().getStringExtra("fragment"))) {
            replaceFragment(new My_Flowers_Fragment());
            binding.BottomNavigationView.setSelectedItemId(R.id.moi_tvti);
        } else if (getIntent() != null && "weather".equals(getIntent().getStringExtra("fragment"))) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city", getIntent().getStringExtra("city"));
            startActivity(intent);

        }
        else {
            // Открыть начальный фрагмент по умолчанию
            replaceFragment(new All_Flowers_Fragment());
            binding.BottomNavigationView.setSelectedItemId(R.id.vse_tvti);
        }


        binding.BottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.vse_tvti) {
                replaceFragment(new All_Flowers_Fragment());
            } else if (itemId == R.id.moi_tvti) {
                replaceFragment(new My_Flowers_Fragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private void startNotificationWorker() {
        PeriodicWorkRequest notificationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueue(notificationWorkRequest);
    }
    private void showNotificationAccessDialog() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int dialogShownCount = prefs.getInt(KEY_DIALOG_SHOWN, 0);

        // Проверяем, был ли пользователь освобожден от показа диалога
        boolean isUserExempted = prefs.getBoolean("is_user_exempted", false);

        if (dialogShownCount < MAX_DIALOG_SHOWS && !isUserExempted) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Требуется действие");
            builder.setIcon(R.drawable.flower_icon_176905);
            builder.setMessage("Пожалуйста, предоставьте доступ к уведомлениям и снимите все ограничения по энергоснабжению в настройках.");

            builder.setPositiveButton("Открыть настройки", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            });

            builder.setNegativeButton("Отмена", null);

            builder.setNeutralButton("Я все сделал", (dialog, which) -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("is_user_exempted", true);
                editor.apply();
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            // Увеличиваем счетчик показов диалога
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_DIALOG_SHOWN, dialogShownCount + 1);
            editor.apply();
        }
    }

}
