package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityAlumnoEventoBinding;

public class AlumnoEventoActivity extends AppCompatActivity {

    ActivityAlumnoEventoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEventoBack.setOnClickListener(view -> {
            finish();
        });
    }
}