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
import com.example.yourvelociraptorflowers.ui.plants.search.Search_activity;
import com.example.yourvelociraptorflowers.ui.user.viewing.User_profile;
import com.example.yourvelociraptorflowers.ui.weather.WeatherActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class All_Flowers_Fragment extends Fragment {
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

//        Plants newUser5 = new Plants(
//                "Денежное дерево (Crassula ovata) 💰",
//                "Денежное дерево - популярное суккулентное растение, приносящее удачу и процветание в дом. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/06/2-123-700x690.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/3-121-700x664.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/4-113-700x667.jpg",
//                "hhttps://rastenievod.com/wp-content/uploads/2016/06/5-101-700x661.jpg",
//                "Оптимальная температура для роста - от 15 до 24 градусов. 🌡️",
//                "Растение предпочитает низкую влажность воздуха и хорошо переносит сухой воздух. 💧",
//                "Денежное дерево любит яркий солнечный свет, но может расти и в полутени. Поливайте растение умеренно, давая почве полностью высохнуть между поливами. ☀️",
//                "https://youtu.be/_ROk8FXCvL4",
//                14 // Частота полива (раз в 14 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser5);
//        Plants newUser6 = new Plants(
//                "Фикус каучуконосный (Ficus elastica) 🌿",
//                "Фикус каучуконосный - крупное и привлекательное комнатное растение с глянцевыми листьями, которое легко ухаживать. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/08/2-50-700x678.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/4-44.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/3-11-700x516.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/5-29.jpg",
//                "Оптимальная температура для роста - от 18 до 26 градусов. 🌡️",
//                "Растение предпочитает умеренную влажность воздуха, около 50-60%. 💧",
//                "Фикус каучуконосный любит яркий рассеянный свет, но может адаптироваться к полутени. Избегайте прямых солнечных лучей. Поливайте растение регулярно, давая верхнему слою почвы просохнуть между поливами. ☀️",
//                "https://youtu.be/vQcWO2-PL7k",
//                7 // Частота полива (раз в 7 дней)
//        );
//        Plants newUser7 = new Plants(
//                "Диффенбахия (Dieffenbachia) 🌿",
//                "Диффенбахия - эффектное и быстрорастущее комнатное растение с крупными пестрыми листьями, идеально подходящее для украшения интерьера. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/06/0-700x707.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/1-110-700x700.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/2-113.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/06/3-113-700x694.jpg",
//                "Оптимальная температура для роста - от 18 до 26 градусов. 🌡️",
//                "Растение предпочитает высокую влажность воздуха, около 60-70%. 💧",
//                "Диффенбахия любит яркий рассеянный свет, но может расти и в полутени. Избегайте прямых солнечных лучей. Поливайте регулярно, поддерживая почву слегка влажной. ☀️",
//                "https://youtu.be/NhgatgTxj_E",
//                7 // Частота полива (раз в 7 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser7);
//
//        firestore.collection("plants")
//                .document()
//                .set(newUser6);
//        Plants newUser8 = new Plants(
//                "Монстера (Monstera deliciosa) 🌿",
//                "Монстера - эффектное и декоративное растение с большими резными листьями, идеально подходящее для украшения интерьера. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/08/2-52-700x695.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/3-51-700x672.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/4-47-700x597.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/5-32-700x681.jpg",
//                "Оптимальная температура для роста - от 18 до 27 градусов. 🌡️",
//                "Растение предпочитает высокую влажность воздуха, около 60-80%. 💧",
//                "Монстера любит яркий рассеянный свет, но может расти и в полутени. Избегайте прямых солнечных лучей. Поливайте умеренно, давая верхнему слою почвы подсохнуть между поливами. ☀️",
//                "https://youtu.be/PuRlKAAk4RE",
//                7 // Частота полива (раз в 7 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser8);
//        Plants newUser9 = new Plants(
//                "Замиокулькас (Долларовое дерево) 💵",
//                "Замиокулькас, или Долларовое дерево, - это неприхотливое и легкорастущее растение с глянцевыми листьями, идеально подходящее для интерьера. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/08/2-61-700x700.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/3-60.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/1-59-700x690.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/08/1-60-700x635.jpg",
//                "Оптимальная температура для роста - от 18 до 26 градусов. 🌡️",
//                "Растение предпочитает низкую влажность воздуха и хорошо переносит сухой воздух. 💧",
//                "Замиокулькас любит яркий рассеянный свет, но может расти и в полутени. Поливайте умеренно, давая почве полностью высохнуть между поливами. ☀️",
//                "https://youtu.be/vrfdMYoi7YA",
//                10 // Частота полива (раз в 10 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser9);
//        Plants newUser10 = new Plants(
//                "Фикус Панда (Ficus panda) 🐼",
//                "Фикус Панда - это компактное и декоративное растение с мелкими, зелеными листьями, напоминающими узоры на мехе панды. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2017/03/1-35-700x593.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/03/4-9-700x700.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/03/4-33-700x611.jpg",
//                "https://rastenievod.com/wp-content/uploads/2017/03/6-32-700x539.jpg",
//                "Оптимальная температура для роста - от 18 до 24 градусов. 🌡️",
//                "Растение предпочитает умеренную влажность воздуха и регулярное опрыскивание листьев. 💧",
//                "Фикус Панда предпочитает яркий рассеянный свет, но может расти и в полутени. Поливайте умеренно, давая почве частично высохнуть между поливами. ☀️",
//                "https://youtu.be/0wWpbePKRT8",
//                7 // Частота полива (раз в 7 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser10);
//
//        Plants newUser11 = new Plants(
//                "Финиковая пальма (Phoenix dactylifera) 🌴",
//                "Финиковая пальма - это красивое и изящное растение с характерными перистыми листьями и сладкими фруктами, идеально подходящее для украшения интерьера. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/05/1-102.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/05/2-92-700x718.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/05/3-92-700x700.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/05/4-81-700x698.jpg",
//                "Оптимальная температура для роста - от 20 до 25 градусов. 🌡️",
//                "Растение предпочитает высокую влажность воздуха и регулярный полив. 💧",
//                "Финиковая пальма предпочитает яркий прямой свет, но может расти и в полутени. Поливайте растение, когда верхний слой почвы подсохнет на ощупь. ☀️",
//                "https://youtu.be/M-8rqA9NUFg",
//                7 // Частота полива (раз в 7 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser11);
//        Plants newUser12 = new Plants(
//                "Каланхоэ (Kalanchoe) 🌸",
//                "Каланхоэ - это неприхотливое и красивое растение с яркими цветами, которые радуют глаз круглый год. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/07/1-43-700x703.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/07/2-45-700x684.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/07/1-44.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/07/3-41-700x593.jpg",
//                "Оптимальная температура для роста - от 18 до 25 градусов. 🌡️",
//                "Растение предпочитает среднюю влажность воздуха и умеренный полив. 💧",
//                "Каланхоэ любит яркий рассеянный свет, но может расти и в полутени. Поливайте растение умеренно, избегая застоя воды в горшке. ☀️",
//                "https://youtu.be/452X62Fdazw",
//                5 // Частота полива (раз в 5 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser12);
//        Plants newUser13 = new Plants(
//                "Кактус 🌵",
//                "Кактус - это устойчивое и легкорастущее растение, которое прекрасно подходит для украшения интерьера. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2016/12/1-57-700x695.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/12/2-55-700x634.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/12/3-55-700x645.jpg",
//                "https://rastenievod.com/wp-content/uploads/2016/12/4-49-700x690.jpg",
//                "Оптимальная температура для роста - от 18 до 30 градусов. 🌡️",
//                "Кактус предпочитает сухой воздух и редкий полив. 💧",
//                "Кактусу требуется яркий прямой свет. Поливайте растение очень осторожно, чтобы не заливать почву. ☀️",
//                "https://youtu.be/rlyrK-35C9w",
//                14 // Частота полива (раз в 14 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser13);
//        Plants newUser14 = new Plants(
//                "Бегония вечноцветущая (Begonia semperflorens) 🌸",
//                "Бегония вечноцветущая - это красивое и устойчивое растение с непрерывным цветением, которое отлично подходит для озеленения помещений. 🏡",
//                "https://rastenievod.com/wp-content/uploads/2021/11/1-2-700x678.jpg",
//                "https://rastenievod.com/wp-content/uploads/2021/11/2-2-700x700.jpg",
//                "https://rastenievod.com/wp-content/uploads/2021/11/3-2-700x669.jpg",
//                "https://rastenievod.com/wp-content/uploads/2021/11/4-2-700x616.jpg",
//                "Оптимальная температура для роста - от 18 до 24 градусов. 🌡️",
//                "Бегония предпочитает высокую влажность воздуха и регулярный полив. 💧",
//                "Растение предпочитает яркий рассеянный свет, но может расти и в полутени. Поливайте растение умеренно, поддерживая влажность почвы. ☀️",
//                "https://youtu.be/K9n4Q_UtJwU",
//                5 // Частота полива (раз в 5 дней)
//        );
//        firestore.collection("plants")
//                .document()
//                .set(newUser14);

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
            Intent intent = new Intent(requireContext(), User_profile.class);
            startActivity(intent);
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.weatherButton.setVisibility(View.VISIBLE);
        }else {
            binding.weatherButton.setVisibility(View.GONE);
        }
        binding.weatherButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), WeatherActivity.class);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Log.wtf("LOGG", documentSnapshot.get("city").toString());
                        documentSnapshot.get("city");
                        intent.putExtra("city", documentSnapshot.get("city").toString());
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Ошибка с городом!", Toast.LENGTH_SHORT).show());

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
            Intent intent = new Intent(requireContext(), Search_activity.class);
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



