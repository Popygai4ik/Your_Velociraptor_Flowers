package com.example.yourvelociraptorflowers.ui.user.deleteaccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourvelociraptorflowers.databinding.ActivityDeleteAccountBinding;
import com.example.yourvelociraptorflowers.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteAccountActivity extends AppCompatActivity {
    private ActivityDeleteAccountBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_delete_account);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.deleteAccountButton.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Вы точно хотите удалить аккаунт?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserAccount();
                    }
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    private void deleteUserAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // Удаление данных пользователя из Firestore
            db.collection("users").document(uid)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Удаление аккаунта пользователя из FirebaseAuth
                            user.delete()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(DeleteAccountActivity.this, "Аккаунт успешно удален", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(DeleteAccountActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(DeleteAccountActivity.this, "Ошибка удаления аккаунта", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {
                            Toast.makeText(DeleteAccountActivity.this, "Ошибка удаления данных пользователя", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
