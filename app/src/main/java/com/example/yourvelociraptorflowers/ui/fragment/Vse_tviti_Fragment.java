package com.example.yourvelociraptorflowers.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.yourvelociraptorflowers.R;
import com.example.yourvelociraptorflowers.model.plant.Plants;
import com.example.yourvelociraptorflowers.domain.Allplants.PlantsAdapter;
import com.example.yourvelociraptorflowers.databinding.FragmentVseTvitiBinding;
import com.example.yourvelociraptorflowers.ui.notifications.NotificationsActivity;
import com.example.yourvelociraptorflowers.ui.plants.search.Search_activiti;
import com.example.yourvelociraptorflowers.ui.user.prosmotr.User_profil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
//        Plants newUser2 = new Plants(
//                "Змеиное растение (Сансевиерия) 🌿",
//                "Змеиное растение - это выносливое и неприхотливое растение, идеально подходящее для дома. 🏡",
//                "https://piantica.com/wp-content/uploads/2020/04/Untitled-design17-768x1086.png",
//                "https://media-be.chewy.com/wp-content/uploads/2023/05/01193934/air-purifying-plants-pets-toxic-mobile-scaled.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/09/6-30.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/09/4-48-700x700.jpg",
//                "Оптимальная температура для роста - от 18 до 30 градусов. 🌡️",
//                "Растение предпочитает умеренную влажность воздуха, но может адаптироваться к сухому воздуху. 💧",
//                "Змеиное растение выдерживает рассеянный свет и тень. Избегайте переувлажнения почвы и не оставляйте растение в воде. ☀️",
//                "https://youtu.be/Ql--qX762t4",
//                5 // Частота полива (раз в 5 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser2);
//
//        Plants newUser3 = new Plants(
//                "Орхидея-мотылек (Фаленопсис) 🌸",
//                "Орхидея-мотылек - элегантное и изысканное растение с красивыми цветами, идеально подходящее для дома. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/06/1-92-700x625.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/4-87-700x605.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/9-44-700x499.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/2-93-700x619.jpg",
//                "Оптимальная температура для роста - от 20 до 25 градусов. 🌡️",
//                "Растение предпочитает высокую влажность воздуха, около 60-80%. 💧",
//                "Орхидея любит яркий рассеянный свет, но следует избегать прямых солнечных лучей. Регулярно опрыскивайте листья и корни, обеспечивая хорошую вентиляцию. ☀️",
//                "https://youtu.be/N_WiaZ4rg2Q",
//                5 // Частота полива (раз в 5 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser3);
//        Plants newUser4 = new Plants(
//                "Фиалка (Saintpaulia) 🌺",
//                "Фиалка - красивое и компактное комнатное растение с яркими цветами, за  которым легко ухаживать. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2017/05/1-24-700x658.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/05/3-26-700x582.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/05/5-25-700x643.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/05/4-24-700x590.jpg",
//                "Оптимальная температура для роста - от 18 до 24 градусов. 🌡️",
//                "Растение предпочитает умеренную влажность воздуха, около 40-60%. 💧",
//                "Фиалка любит яркий, но рассеянный свет. Избегайте прямых солнечных лучей. Регулярно поливайте, но не допускайте застоя воды. ☀️",
//                "https://youtu.be/8qUExRvuXW8",
//                7 // Частота полива (раз в 7 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser4);
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

        binding.notificationButton.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(requireContext(), NotificationsActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(requireContext(), "Для просмотра уведомлений войдите в аккаунт", Toast.LENGTH_SHORT).show();
            }

        });
        binding.searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Search_activiti.class);
            startActivity(intent);
        });
        checkNotifications();


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
    private void checkNotifications() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()) // Замените "userId" на текущий идентификатор пользователя
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.contains("notifications")) {
                        ArrayList<Map<String, Object>> notificationsList = (ArrayList<Map<String, Object>>) documentSnapshot.get("notifications");
                        if (notificationsList != null && !notificationsList.isEmpty()) {
                            binding.notificationButton.setImageResource(R.drawable.free_icon_notifications_5189239); // Измените на ваш ресурс активной иконки уведомлений
                        } else {
                            Log.wtf("Firestore", "Notifications list is null or empty");
                        }
                    } else {
                        Log.e("Firestore", "Document does not contain 'notifications' field");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error checking notifications.", e));
    }}
}



