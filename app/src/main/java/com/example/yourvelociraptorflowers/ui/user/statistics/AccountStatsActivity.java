package com.example.yourvelociraptorflowers.ui.user.statistics;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.AccountStatsActivityBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.example.yourvelociraptorflowers.ui.notifications.NotificationsActivity;
import com.example.yourvelociraptorflowers.ui.user.viewing.User_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

public class AccountStatsActivity extends AppCompatActivity {
    private static final String PREF_KEY_NOTIFICATIONS = "notifications_shown_weather";
    private AccountStatsActivityBinding binding;
    private SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AccountStatsActivityBinding   .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_account_stats);

        binding.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AccountStatsActivity.this, User_profile.class);
            startActivity(intent);
        });
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserStats();
    }

    private void loadUserStats() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        db.collection("users").document(mAuth.getCurrentUser().getUid())
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot document2 = task2.getResult();
                                        if (document2.exists()) {
                                            String userName = document2.getString("name");
                                            String userEmail = document2.getString("email");
                                            String userCity = document2.getString("city");
                                            String userCreationDate = document2.getString("creation_date");
//                                            Log.wtf("LOGGING", "name: " + userName + " email: " + userEmail + " city: " + userCity);

                                            if (userName != null) {
                                                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                                                boolean isSubscribed = sharedPreferences.getBoolean(PREF_KEY_NOTIFICATIONS, false);
                                                updateSubscriptionStatus(isSubscribed);
                                                binding.profileName.setText(userName);
                                                binding.profileEmail.setText("✉ "+userEmail+" ✉");
                                                binding.profileCity.setText("🏡 "+ userCity + " 🏡");
                                                binding.profileDataCreation.setText("📅 Вы создали аккаунт " +userCreationDate + " 📅");
                                                List<Map<String, Object>> moisFlowers = (List<Map<String, Object>>) document2.get("moisFlowers");

                                                if (moisFlowers != null || !moisFlowers.isEmpty()) {
                                                    int flowerCount = moisFlowers.size();
                                                    String flowerWord;
                                                    if (flowerCount == 1) {
                                                        flowerWord = "растение";
                                                    } else if (flowerCount >= 2 && flowerCount <= 4) {
                                                        flowerWord = "растения";
                                                    } else {
                                                        flowerWord = "растений";
                                                    }
                                                    binding.profileFlowersCount.setText("\uD83C\uDF3C Сейчас у вас " + flowerCount + " " + flowerWord+ " \uD83C\uDF3C");
                                                } else {
                                                    binding.profileFlowersCount.setText("Сейчас у вас 0 растений \uD83C\uDF3C");
                                                }
                                                List<Map<String, Object>> notifications = (List<Map<String, Object>>) document.get("notifications");

                                                if (notifications != null || !notifications.isEmpty()) {
                                                    int notificationCount = notifications.size();
                                                    String notificationWord;
                                                    if (notificationCount == 1) {
                                                        notificationWord = "уведомление";
                                                    } else if (notificationCount >= 2 && notificationCount <= 4) {
                                                        notificationWord = "уведомления";
                                                    } else {
                                                        notificationWord = "уведомлений";
                                                    }
                                                    binding.profileNotificationsCount.setText("✉ У вас " + notificationCount + " " + notificationWord+ " ✉");
                                                } else {
                                                    binding.profileNotificationsCount.setText("✉ У вас 0 уведомлений ✉");
                                                }
                                                binding.notificationButton.setOnClickListener(v -> {
                                                    Intent intent = new Intent(AccountStatsActivity.this, NotificationsActivity.class);
                                                    startActivity(intent);
                                                });
                                                binding.flowersButton.setOnClickListener(v -> {
                                                    Intent intent = new Intent(AccountStatsActivity.this, MainActivity.class);
                                                    intent.putExtra("fragment", "Moi_tviti_Fragment");
                                                    startActivity(intent);
                                                });

                                            }
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(AccountStatsActivity.this, "Не удалось загрузить статистику", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void updateSubscriptionStatus(boolean isSubscribed) {
        if (isSubscribed) {
            binding.profileSubscriptionNotification.setText("🌞 Вы согласны на уведомления о погоде 🌞");
        } else {
            binding.profileSubscriptionNotification.setText("⛈ Вы не согласны на уведомления о погоде ⛈");
        }
    }
}
