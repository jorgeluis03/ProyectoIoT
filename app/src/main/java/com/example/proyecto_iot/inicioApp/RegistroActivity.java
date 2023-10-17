package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityRegistroBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private String lastName;
    private String code;
    private String email;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.sendButton.setOnClickListener(view -> {
            if (validFields()){

                Alumno nuevoAlumno = new Alumno(name, lastName, "Alumno", code, email, "", type, "inactivo");

                //Intent intent = new Intent(RegistroActivity.this, ConfirmarregistroActivity.class);
                //startActivity(intent);
            }
        });
    }

    boolean validFields(){
        name = binding.editNameSign.getEditText().getText().toString();
        lastName = binding.editLastnameSign.getEditText().getText().toString();
        code = binding.editCodeSign.getEditText().getText().toString();
        email = binding.editEmailSign.getEditText().getText().toString();
        String pass = binding.editPasswSign.getEditText().getText().toString();
        type = binding.userTypeSpinner.getEditText().getText().toString();

        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(type);
    }
}