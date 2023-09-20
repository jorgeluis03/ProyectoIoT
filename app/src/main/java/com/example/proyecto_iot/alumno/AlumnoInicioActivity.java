package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityAlumnoInicioBinding;

public class AlumnoInicioActivity extends AppCompatActivity {

    private ActivityAlumnoInicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> {
            Intent intent = new Intent(AlumnoInicioActivity.this, AlumnoPerfilActivity.class);
            startActivity(intent);
        });
    }
}