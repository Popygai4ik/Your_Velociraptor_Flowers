package com.example.yourvelociraptorflowers.ui.plants.illumination;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.databinding.IlluminationActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class IlluminationActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor luxSensor;
    private IlluminationActivityBinding binding;
    private IlluminationViewModel viewModel;
    private Handler handler;
    private Runnable stopMeasurement;
    private Vibrator vibrator;
    private SensorEventListener listenLux;
    private float coefficientIllumination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация SensorManager и датчика освещенности
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        luxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Инициализация binding
        binding = IlluminationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация ViewModel
        viewModel = new ViewModelProvider(this).get(IlluminationViewModel.class);

        // Получение данных из Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Glide.with(this)
                .load(intent.getStringExtra("resinok"))
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.centerImage);
        coefficientIllumination = intent.getIntExtra("coefficient_illumination", 0);

        binding.title.setText("\uD83D\uDE0E Проверка освещенности для " + name + " \uD83D\uDE0E");
        binding.backButton.setOnClickListener(v -> onBackPressed());

        // Инициализация вибратора
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Восстановление последнего результата из SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("IlluminationPrefs", Context.MODE_PRIVATE);
        float lastAverage = sharedPref.getFloat("last_average", -1);
        String lastName = sharedPref.getString("last_name", "");
        float lastCoefficient = sharedPref.getFloat("last_coefficient", -1);

        if (lastAverage != -1 && lastName.equals(name)) {
            String comparisonResult;
            if (lastAverage > lastCoefficient * 1.2) {
                comparisonResult = "Слишком много света для " + name;
            } else if (lastAverage < lastCoefficient * 0.8) {
                comparisonResult = "Слишком мало света для " + name;
            } else {
                comparisonResult = "Освещенность в норме. Для " + name + " это идеальные условия.";
            }
            binding.resultLabel.setText("Результат: " + lastAverage + "\nТребуется: " + lastCoefficient + "\n" + comparisonResult);
            binding.resultFields.setVisibility(View.VISIBLE);
        }

        // Инициализация списка для хранения значений освещенности
        if (viewModel.getLuxValues().isEmpty()) {
            viewModel.setLuxValues(new ArrayList<>());
        }

        // Инициализация обработчика событий сенсора
        listenLux = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                viewModel.getLuxValues().add(sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        // Восстановление состояния
        if (viewModel.isMeasuring()) {
            startMeasurement();
        } else {
            binding.resultFields.setVisibility(View.VISIBLE);
        }

        // Обработка клика на кнопку
        binding.measureButton.setOnClickListener(v -> {
            if (luxSensor != null) {
                triggerVibration();
                binding.measureButton.setVisibility(View.INVISIBLE);
                binding.resultFields.setVisibility(View.INVISIBLE);
                binding.progressLayout.setVisibility(View.VISIBLE);
                startMeasurement();
            } else {
                Toast.makeText(this, "Нет датчика освещенности", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMeasurement() {
        // Начало измерения
        viewModel.setMeasuring(true);
        viewModel.getLuxValues().clear();
        sensorManager.registerListener(listenLux, luxSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Инициализация прогресса
        viewModel.setProgress(0);
        binding.progressBar.setProgress(viewModel.getProgress());
        binding.progressText.setText(viewModel.getProgress() + "%");

        handler = new Handler();
        Runnable updateProgress = new Runnable() {
            @Override
            public void run() {
                if (viewModel.getProgress() < 100) {
                    viewModel.setProgress(viewModel.getProgress() + 100 / 15); // 15 секунд
                    binding.progressBar.setProgress(viewModel.getProgress());
                    binding.progressText.setText(viewModel.getProgress() + "%");
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(updateProgress);

        stopMeasurement = () -> {
            sensorManager.unregisterListener(listenLux);
            calculateAndDisplayAverage();
            viewModel.setMeasuring(false);
            binding.measureButton.setVisibility(View.VISIBLE);
            binding.progressLayout.setVisibility(View.GONE);
            binding.resultFields.setVisibility(View.VISIBLE);
            triggerVibration();
        };
        handler.postDelayed(stopMeasurement, 15000);
    }

    // Метод для вычисления и отображения среднего значения освещенности
    private void calculateAndDisplayAverage() {
        List<Float> luxValues = viewModel.getLuxValues();
        if (!luxValues.isEmpty()) {
            float sum = 0;
            for (float value : luxValues) {
                sum += value;
            }
            float average = sum / luxValues.size();
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            String comparisonResult;
            if (average > coefficientIllumination * 1.8) {
                comparisonResult = "Слишком высокая освещенность для " + name + ". Риск для растений.";
            } else if (average > coefficientIllumination * 1.6) {
                comparisonResult = "Очень высокая освещенность для " + name + ". Может навредить.";
            } else if (average > coefficientIllumination * 1.4) {
                comparisonResult = "Высокая освещенность для " + name + ". Немного превышает норму.";
            } else if (average > coefficientIllumination * 1.3) {
                comparisonResult = "Высокая освещенность, почти нормальная для " + name + ".";
            } else if (average > coefficientIllumination * 1.2) {
                comparisonResult = "Почти нормальная освещенность, но больше нормы для " + name + ".";
            } else if (average > coefficientIllumination * 1.1) {
                comparisonResult = "Нормальная освещенность, но больше нормы для " + name + ".";
            } else if (average > coefficientIllumination * 1.0) {
                comparisonResult = "Освещенность в норме. Идеальные условия для " + name + ".";
            } else if (average > coefficientIllumination * 0.9) {
                comparisonResult = "Нормальная освещенность для " + name + ". Хорошие условия.";
            } else if (average > coefficientIllumination * 0.8) {
                comparisonResult = "Почти нормальная освещенность, но меньше нормы для " + name + ".";
            } else if (average > coefficientIllumination * 0.7) {
                comparisonResult = "Нормальная освещенность, но меньше нормы для " + name + ".";
            } else if (average > coefficientIllumination * 0.6) {
                comparisonResult = "Почти нормальная освещенность, но ниже нормы для " + name + ".";
            } else if (average > coefficientIllumination * 0.5) {
                comparisonResult = "Низкая освещенность, но почти нормальная для " + name + ".";
            } else if (average > coefficientIllumination * 0.4) {
                comparisonResult = "Низкая освещенность для " + name + ". Недостаточно света.";
            } else if (average > coefficientIllumination * 0.2) {
                comparisonResult = "Очень низкая освещенность для " + name + ". Нужно больше света.";
            } else {
                comparisonResult = "Очень очень низкая освещенность для " + name + ". Критично мало света.";
            }



            binding.resultLabel.setText("Результат: " + average + " люкс 🌞\nТребуется: " + coefficientIllumination + " люкс 🌞\n" + comparisonResult);

            // Сохранение результата в SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("IlluminationPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("last_average", average);
            editor.putString("last_name", name);
            editor.putFloat("last_coefficient", coefficientIllumination);
            editor.apply();
        }
    }

    // Метод для запуска вибрации
    private void triggerVibration() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500); // Вибрация на 500 миллисекунд
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null && listenLux != null) {
            sensorManager.unregisterListener(listenLux);
        }
        if (handler != null && stopMeasurement != null) {
            handler.removeCallbacks(stopMeasurement);
        }
    }
}
