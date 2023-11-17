package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.databinding.ActivityFpverificationBinding;

public class FPVerificationActivity extends AppCompatActivity {
    ActivityFpverificationBinding binding;
    EditText inputCode1, inputCode2, inputCode3, inputCode4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFpverificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton4.setOnClickListener(view -> {
            finish();
        });

        binding.forgPasswNext.setOnClickListener(view -> {
            Intent intent = new Intent(FPVerificationActivity.this, IngresarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        /*
        inputCode1 = binding.inputCode1;
        inputCode2 = binding.inputCode2;
        inputCode3 = binding.inputCode3;
        inputCode4 = binding.inputCode4;

         */

        //hideHintOnClick();

        //setJumpToNextInput();
    }
    private void hideHintOnClick(){
        inputCode1.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputCode1.setHint(null);
            } else {
                inputCode1.setHint("__");
            }
        });
        inputCode2.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputCode2.setHint(null);
            } else {
                inputCode2.setHint("__");
            }
        });
        inputCode3.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputCode3.setHint(null);
            } else {
                inputCode3.setHint("__");
            }
        });
        inputCode4.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                inputCode4.setHint(null);
            } else {
                inputCode4.setHint("__");
            }
        });
    }

    private void setJumpToNextInput(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}