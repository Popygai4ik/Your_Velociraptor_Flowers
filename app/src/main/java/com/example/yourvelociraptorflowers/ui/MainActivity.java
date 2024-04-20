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

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new Vse_tviti_Fragment());
        String uid = FirebaseAuth.getInstance().getUid();
        Toast.makeText(this, "Login: " + uid, Toast.LENGTH_SHORT).show();
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