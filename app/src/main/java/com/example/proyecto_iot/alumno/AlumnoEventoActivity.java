package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.ActivityAlumnoEventoBinding;

public class AlumnoEventoActivity extends AppCompatActivity {

    ActivityAlumnoEventoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Evento evento = (Evento) getIntent().getSerializableExtra("evento");
        binding.textEventoTitulo.setText(evento.getTitulo());
        binding.textEventoActividad.setText(evento.getActividad());
        binding.textEventoDescripcion.setText(evento.getDescripcion());
        binding.buttonEventoFecha.setText(evento.getFecha());
        binding.buttonEventoHora.setText(evento.getHora());

        binding.buttonEventoBack.setOnClickListener(view -> {
            finish();
        });
    }
}