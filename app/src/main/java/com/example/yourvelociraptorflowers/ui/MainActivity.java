package com.example.yourvelociraptorflowers.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.yourvelociraptorflowers.databinding.ActivityMainBinding;
import com.example.yourvelociraptorflowers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
                                    Toast.makeText(this, "Добро пожаловать, " + userName, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            // Пользователь не залогинен
            Toast.makeText(this, "Добро пожаловать, новый пользователь!", Toast.LENGTH_SHORT).show();
        }

        replaceFragment(new Vse_tviti_Fragment());

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

}