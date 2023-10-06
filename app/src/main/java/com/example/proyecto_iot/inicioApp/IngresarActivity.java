package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.databinding.ActivityIngresarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IngresarActivity extends AppCompatActivity {

    private ActivityIngresarBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngresarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRegistrarme.setOnClickListener(view -> {
            intent = new Intent(IngresarActivity.this, RegistroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        binding.buttonIniciarSesion.setOnClickListener(view -> {
            intent = new Intent(IngresarActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(IngresarActivity.this, AlumnoInicioActivity.class);
            startActivity(intent);
            finish();
        }
    }
}