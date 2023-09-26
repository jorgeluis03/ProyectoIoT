package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityConfirmNewPasswBinding;

public class ConfirmNewPasswActivity extends AppCompatActivity {

    ActivityConfirmNewPasswBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmNewPasswBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonIniciar.setOnClickListener(view -> {
            Intent intent = new Intent(ConfirmNewPasswActivity.this, IngresarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.buttonLogin.setOnClickListener(view -> {
            Intent intent = new Intent(ConfirmNewPasswActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(ConfirmNewPasswActivity.this, IngresarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}