package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Objetos.Evento;
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
        binding.buttonEventoLugar.setText(evento.getLugar().getNombre());

        binding.buttonEventoBack.setOnClickListener(view -> {
            finish();
        });
    }
}