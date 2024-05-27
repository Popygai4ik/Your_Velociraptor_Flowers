package com.example.yourvelociraptorflowers.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.yourvelociraptorflowers.ui.fragment.Moi_tviti_Fragment;
import com.example.yourvelociraptorflowers.ui.fragment.Vse_tviti_Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int REQUEST_CODE_ALARM_PERMISSION = 1;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            replaceFragment(new Moi_tviti_Fragment());
            binding.BottomNavigationView.setSelectedItemId(R.id.moi_tvti);
        } else {
            // Открыть начальный фрагмент по умолчанию
            replaceFragment(new Vse_tviti_Fragment());
            binding.BottomNavigationView.setSelectedItemId(R.id.vse_tvti);
        }

        checkAndRequestPermissions();

        binding.BottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.vse_tvti) {
                replaceFragment(new Vse_tviti_Fragment());
            } else if (itemId == R.id.moi_tvti) {
                replaceFragment(new Moi_tviti_Fragment());
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

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM}, REQUEST_CODE_ALARM_PERMISSION);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ALARM_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied, handle accordingly
            }
        } else if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    private void startNotificationWorker() {
        PeriodicWorkRequest notificationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueue(notificationWorkRequest);
    }
}
