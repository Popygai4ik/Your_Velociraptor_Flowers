package com.example.yourvelociraptorflowers.ui;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.example.yourvelociraptorflowers.databinding.RegisterActivityBinding;
        import com.example.yourvelociraptorflowers.model.User;
        import com.google.firebase.Firebase;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseAuthUserCollisionException;
        import com.google.firebase.firestore.FirebaseFirestore;

public class Register_activity extends AppCompatActivity {

    private RegisterActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginEmail.setOnClickListener(v -> loginwithEmail());
        binding.backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    public Boolean validateFields(String imifam, String email, String password) {
        if (imifam.isEmpty()){
            binding.imifam.setError("Wrong имя");
        }
        if (!email.contains("@")) {
            binding.emailField.setError("Wrong email");
            return false;
        }
        if (password.isEmpty()) {
            binding.passwordField.setError("Password should not be empty");
            return false;
        }
        return true;
    }
    private void loginwithEmail() {
        String ime = binding.imifam.getText().toString();
        String email = binding.emailField.getText().toString();
        String password = binding.passwordField.getText().toString();
        if (validateFields(ime, email, password)) {
            signinFirebase(ime, email, password);
        }
    }
    private void signinFirebase(String ime, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    User newUser = new User(authResult.getUser().getUid(), ime, email, password);
                    firestore.collection("users")
                            .document()
                            .set(newUser);
                    Toast.makeText(this, "Login success: " + authResult.getUser().getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "SignIn error: " + email+" "+password, Toast.LENGTH_SHORT).show();

                    Toast.makeText(this, "SignIn error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }
}
