package com.example.proyecto_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.databinding.ActivityIngresarBinding;

public class IngresarActivity extends AppCompatActivity {

    private ActivityIngresarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngresarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRegistrarme.setOnClickListener(view -> {
            Intent intent = new Intent(IngresarActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}