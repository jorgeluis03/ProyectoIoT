package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityEditarActividadBinding;

public class EditarActividad extends AppCompatActivity {
    ActivityEditarActividadBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarActividadBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_editar_actividad);


        //Toolbar
        Toolbar toolbar = binding.toolbarEditarActividadesDg;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //====================================
    }

    //Flecha para regrasar al inicio
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}