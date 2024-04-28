package com.example.yourvelociraptorflowers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.yourvelociraptorflowers.domain.plants_generate;
import com.example.yourvelociraptorflowers.model.Plants;
import com.example.yourvelociraptorflowers.domain.PlantsAdapter;
import com.example.yourvelociraptorflowers.databinding.FragmentVseTvitiBinding;
import com.example.yourvelociraptorflowers.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Vse_tviti_Fragment extends Fragment {
    private FragmentVseTvitiBinding binding;
    private PlantsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVseTvitiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        Plants newUser1 = new Plants("Джункус (Ситник)", "Ситник - необычное и требовательное растение, нуждающееся в большом количестве влаги.",
//                "https://rastenievod.com/wp-content/uploads/2017/03/1-37-700x633.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/03/6-15-700x561.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/03/7-12-700x819.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/03/10-10-700x525.jpg",
//                "Ситник нуждается в тепле, оптимальная температура для роста составляет более 24 градусов.",
//                "Растение требует высокой влажности воздуха и регулярного увлажнения из опрыскивателя.",
//                "Для выращивания ситника в комнатных условиях необходимо создать почти болотные условия. Растение любит свет, но нуждается в рассеянном освещении и защите от прямых солнечных лучей.");
////        firestore.collection("plants")
//                .document()
//                .set(newUser1);
//        Plants newUser2 = new Plants("Фикус бенджамина", "Фикус бенджамина - популярное декоративное растение с блестящими листьями.",
//                "https://fitosystems.ru/images/stories/virtuemart/product/resized/DSC03256_210x210.jpg");
//        firestore.collection("plants")
//                .document()
//                .set(newUser2);
//        Plants newUser3 = new Plants("Ривина", "Ривина – это низкорослый декоративный кустарник семейства Лаконосовых (Phykolaccaceae). Его родиной считаются тропические и субтропические зоны Америки."
//                ,
//                "https://rastenievod.com/wp-content/uploads/2017/03/2-6-700x605.jpg");
//        firestore.collection("plants")
//                .document()
//                .set(newUser3);
//        Plants newUser4 = new Plants("Микросорум", "Микросорум - род папоротников с разнообразными листьями."
//                ,
//                "https://rastenievod.com/wp-content/uploads/2017/03/6-31-700x686.jpg");
//        firestore.collection("plants")
//                .document()
//                .set(newUser4);
//        Plants newUser5 = new Plants("Дримиопсис", "Дримиопсис - листопадное вечнозеленое растение из тропических областей Южной Африки, относящееся к семейству гиацинтовых.",
//                "https://rastenievod.com/wp-content/uploads/2017/03/3-33-700x634.jpg");
//        firestore.collection("plants")
//                .document()
//                .set(newUser5);
//        Plants newUser6 = new Plants("Фикус Панда", "Фикус Панда - популярный сорт с необычными плодами и густой кроной.",
//                "https://rastenievod.com/wp-content/uploads/2017/03/4-9-700x700.jpg");
//        firestore.collection("plants")
//                .document()
//                .set(newUser6);
//        Plants newUser7 = new Plants("Бригамия", "Бригамия - суккулент с необычным бутылковидным стеблем и бледно-зелеными листочками.",
//                "https://rastenievod.com/wp-content/uploads/2017/03/2-7-700x744.jpg ");
//        firestore.collection("plants")
//                .document()
//                .set(newUser7);
        adapter = new PlantsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);
        binding.profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), User_profil.class);
            startActivity(intent);
        });


        // Отображаем ProgressBar при начале загрузки данных
        binding.progressBar.setVisibility(View.VISIBLE);

//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("plants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Plants> plants = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Plants plant = document.toObject(Plants.class);
                        plants.add(plant);
                    }
                    adapter.setItems(plants);
                    // Скрываем ProgressBar после загрузки данных
                    binding.progressBar.setVisibility(View.GONE);
                    // Отображаем RecyclerView с данными
                    binding.recycler.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents.", e);
                    // Добавьте здесь обработку ошибки, например, вывод сообщения об ошибке пользователю
                    // Скрываем ProgressBar в случае ошибки
                    binding.progressBar.setVisibility(View.GONE);
                });
    }

}

