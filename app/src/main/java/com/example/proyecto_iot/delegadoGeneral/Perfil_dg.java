package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityPerfilDgBinding;
import com.google.android.material.textfield.TextInputLayout;

public class Perfil_dg extends AppCompatActivity {
    ActivityPerfilDgBinding binding;
    TextInputLayout nombre;
    TextInputLayout apellido;
    TextInputLayout correo;
    TextInputLayout codigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilDgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Toolbar
        Toolbar toolbar = binding.toolbarPerfil;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //====================================

        nombre = binding.textFieldNombrePerfil;
        apellido = binding.textFieldApellidoPerfil;
        codigo = binding.textFieldCorreoPerfil;
        correo = binding.textFieldCorreoPerfil;

        binding.fbEditar.setOnClickListener(view -> {
            nombre.setEnabled(true);
            apellido.setEnabled(true);

        });
        binding.fbGuardar.setOnClickListener(view -> {
            nombre.setEnabled(false);
            apellido.setEnabled(false);
        });

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