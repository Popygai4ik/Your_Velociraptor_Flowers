package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.OpisanieActivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class OpisanieActivity extends AppCompatActivity {

    private TextView opisanieTextView;
    private OpisanieActivityBinding binding;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OpisanieActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });


        // Получаем данные из интента
        String Id = getIntent().getStringExtra("Id");
        String opisanie = getIntent().getStringExtra("opisanie");
        String name = getIntent().getStringExtra("name");
        String temp = getIntent().getStringExtra("temp");
        String vlag = getIntent().getStringExtra("vlag");
        String ysl = getIntent().getStringExtra("ysl");
        String resinok = getIntent().getStringExtra("resinok");
        String resinok2 = getIntent().getStringExtra("resinok2");
        String resinok3 = getIntent().getStringExtra("resinok3");
        String resinok4 = getIntent().getStringExtra("resinok4");
        String youtube = getIntent().getStringExtra("opisanie5");
//        Toast.makeText(getApplicationContext(), vlag, Toast.LENGTH_SHORT).show();

        // Отображаем данные
        binding.textViewTempInf.setText(temp);
        binding.textViewvlaginf.setText(vlag);
        binding.imageViewVarchInf.setText(ysl);
        binding.title.setText(name);
        binding.textViewOpisanie.setText(opisanie);

        Glide.with(this)
                .load(resinok)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewOpisanie);

        Glide.with(this)
                .load(resinok2)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewTemp);
        Glide.with(this)
                .load(resinok3)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewVlag);
        Glide.with(this)
                .load(resinok4)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imageViewVarch);
        binding.addButton.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                // Создаем новый элемент для добавления в список
                String id = getIntent().getStringExtra("Id");

                // Получаем текущий список из Firestore
                firestore.collection("users")
                        .document(currentUser.getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            // Проверяем, есть ли уже список moisFlowers
                            if (documentSnapshot.contains("moisFlowers")) {
                                ArrayList<String> moisFlowers = (ArrayList<String>) documentSnapshot.get("moisFlowers");
                                if (moisFlowers.contains(id)) {
                                    // Если элемент уже есть в списке, выдаем сообщение об ошибке
                                    Toast.makeText(this, "Цветок уже добавлен", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // Добавляем новый элемент в список
                                moisFlowers.add(id);

                                // Создаем карту для обновления данных пользователя
                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("moisFlowers", moisFlowers);

                                // Обновляем документ в Firestore
                                firestore.collection("users")
                                        .document(currentUser.getUid())
                                        .update(updateMap)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(this, "Цветок добавлен", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Если список еще не существует, создаем новый список с новым элементом
                                ArrayList<String> moisFlowers = new ArrayList<>();
                                moisFlowers.add(id);

                                // Создаем карту для обновления данных пользователя
                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("moisFlowers", moisFlowers);

                                // Обновляем документ в Firestore
                                firestore.collection("users")
                                        .document(currentUser.getUid())
                                        .set(updateMap)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(this, "Цветок добавлен", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Ошибка получения данных", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Пользователь не вошел, выводим сообщение
                Toast.makeText(this, "Войдите, чтобы добавить цветок", Toast.LENGTH_SHORT).show();
            }
        });
        binding.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создайте намерение для открытия YouTube
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Если приложение YouTube не установлено, вы можете предложить пользователю установить его
                    Toast.makeText(getApplicationContext(), "Приложение YouTube не установлено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}