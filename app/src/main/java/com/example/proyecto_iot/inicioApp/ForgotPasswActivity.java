package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityForgotPasswBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswActivity extends AppCompatActivity {

    ActivityForgotPasswBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton3.setOnClickListener(view -> {
            finish();
        });

        binding.forgPasswNext.setOnClickListener(view -> {
            String correo = binding.editEmailForgPassw.getEditText().getText().toString();
            auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(ForgotPasswActivity.this, FPVerificationActivity.class);
                            startActivity(intent);
                        }
                    });
        });
    }
}