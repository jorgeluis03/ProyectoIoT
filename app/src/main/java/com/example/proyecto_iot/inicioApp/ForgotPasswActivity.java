package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityForgotPasswBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswActivity extends AppCompatActivity {

    ActivityForgotPasswBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton3.setOnClickListener(view -> {
            finish();
        });

        binding.forgPasswNext.setOnClickListener(view -> {
            enviarCorreoRecuperarContrasena();
        });
    }

    private void enviarCorreoRecuperarContrasena(){
        binding.relativeOverlay.setVisibility(View.VISIBLE);
        binding.forgPasswNext.setEnabled(false);

        String correo = binding.editEmailForgPassw.getEditText().getText().toString();
        auth.sendPasswordResetEmail(correo)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(ForgotPasswActivity.this, FPVerificationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        binding.relativeOverlay.setVisibility(View.GONE);
                        binding.forgPasswNext.setEnabled(true);

                        startActivity(intent);
                    }
                });
    }
}