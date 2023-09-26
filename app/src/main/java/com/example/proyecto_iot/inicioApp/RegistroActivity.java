package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {
    ActivityRegistroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.sendButton.setOnClickListener(view -> {
            Intent intent = new Intent(RegistroActivity.this, ConfirmarregistroActivity.class);
            startActivity(intent);
        });
    }
}