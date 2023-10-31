package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityAlumnoBuscarEventoBinding;

public class AlumnoBuscarEventoActivity extends AppCompatActivity {
    private ActivityAlumnoBuscarEventoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoBuscarEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(view -> {
            finish();
        });

        binding.inputBuscarEvento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    Toast.makeText(AlumnoBuscarEventoActivity.this, "ga", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}