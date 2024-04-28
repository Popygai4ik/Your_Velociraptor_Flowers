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