package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.ActivityProvileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class User_profil extends AppCompatActivity {

    private ActivityProvileBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> stringList_bol_18 = new ArrayList<>();
        stringList_bol_18.add("https://img.freepik.com/free-photo/smiling-young-handsome-slavic-gardener-in-uniform-wearing-hat-and-gardening-gloves-holding-spade-and-flowerpot-looking-isolated-on-orange-wall_141793-59688.jpg?size=626&ext=jpg&ga=GA1.1.1819696413.1714395090&semt=ais");
        stringList_bol_18.add("https://img.freepik.com/premium-photo/young-farmer-man-looking-happy-astonished-and-surprised_1194-235984.jpg?size=626&ext=jpg&ga=GA1.1.1819696413.1714395090&semt=ais");
        stringList_bol_18.add("https://img.freepik.com/premium-photo/young-pretty-woman-farmer-concept_1194-188143.jpg?size=626&ext=jpg&ga=GA1.1.1819696413.1714395090&semt=ais");
        ArrayList<String> stringList_men_18 = new ArrayList<>();
        stringList_men_18.add("https://img.freepik.com/free-photo/baby-dressed-up-as-business-person_23-2149831193.jpg?t=st=1714395930~exp=1714399530~hmac=5bddc8c2ab021efd5157bfddfa2076dfdc308e7863e2f7a34e2d943c63841176&w=740");
        stringList_men_18.add("https://img.freepik.com/premium-photo/portrait-smiling-young-woman-sitting-table-home_1048944-24453137.jpg?w=1060");
        stringList_men_18.add("https://img.freepik.com/free-photo/smiley-girl-greenhouse_23-2148414380.jpg?t=st=1714396050~exp=1714399650~hmac=f0b74e9f493cb1e833668132ffb7f1d29e55fb0adeb905e99bd8288f88a9da79&w=1060");
        Random random = new Random();
        int randomIndex = random.nextInt(stringList_bol_18.size());
        binding = ActivityProvileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
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
                                    binding.profileName.setVisibility(View.VISIBLE);
                                    binding.profileName.setText(userName);
                                    boolean vozrst = document.getBoolean("vozrst");
                                    if (vozrst){
                                        Glide.with(this)
                                                .load(stringList_bol_18.get(randomIndex))
                                                .placeholder(R.mipmap.ic_launcher)
                                                .into(binding.profileImage);
                                        binding.profileTitle.setVisibility(View.VISIBLE);
                                    }else {
                                        Glide.with(this)
                                                .load(stringList_men_18.get(randomIndex))
                                                .placeholder(R.mipmap.ic_launcher)
                                                .into(binding.profileImage);
                                        binding.profileTitle.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "get failed with", Toast.LENGTH_SHORT).show();
                        }
                    });
            String email = currentUser.getEmail();
            binding.profileEmail.setText(email);
            binding.profileEmail.setVisibility(View.VISIBLE);
            binding.logoutButton.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.GONE);
            binding.ne.setVisibility(View.GONE);
            binding.ine.setVisibility(View.GONE);
            binding.registerButton.setVisibility(View.GONE);
            binding.logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    // После выхода из аккаунта переходите на экран входа или любой другой экран
                    // Здесь можно использовать Intent для перехода на нужный экран
                    // Например:
                    Intent intent = new Intent(User_profil.this, User_profil.class);
                    startActivity(intent);
                    finish(); // Закрываем текущую активность

                }
            });

        } else {
            // Пользователь не залогинен
            binding.profileName.setVisibility(View.GONE);
            binding.ne.setVisibility(View.VISIBLE);
            binding.ine.setVisibility(View.VISIBLE);
            binding.profileTitle.setVisibility(View.GONE);
            binding.profileEmail.setVisibility(View.GONE);
            binding.logoutButton.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.registerButton.setVisibility(View.VISIBLE);
            binding.loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(User_profil.this, Login_activity.class);
                    startActivity(intent);
                }
            });
            binding.registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(User_profil.this, Register_activity.class);
                    startActivity(intent);
                }
            });
        }



    }

}
