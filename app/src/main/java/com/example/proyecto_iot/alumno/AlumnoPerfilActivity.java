package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader1Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoPerfilBinding;

public class AlumnoPerfilActivity extends AppCompatActivity {

    private ActivityAlumnoPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Bundle bundle = new Bundle();
        bundle.putString("header", "Perfil");
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeader, AlumnoHeader1Fragment.class, bundle)
                .commit();


        binding.buttonEditarPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(AlumnoPerfilActivity.this, AlumnoPerfilEditarActivity.class);
            startActivity(intent);
        });

        binding.buttonContrasena.setOnClickListener(view -> {
            Intent intent = new Intent(AlumnoPerfilActivity.this, AlumnoPerfilContrasenaActivity.class);
            startActivity(intent);
        });
    }
}