package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityChangePasswBinding;

public class ChangePasswActivity extends AppCompatActivity {
    ActivityChangePasswBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton5.setOnClickListener(view -> {
            finish();
        });

        binding.changePasswNext.setOnClickListener(view -> {
            Intent intent = new Intent(ChangePasswActivity.this, ConfirmNewPasswActivity.class);
            startActivity(intent);
        });
    }
}