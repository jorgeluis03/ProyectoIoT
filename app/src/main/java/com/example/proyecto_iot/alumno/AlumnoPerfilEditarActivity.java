package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader1Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoPerfilEditarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AlumnoPerfilEditarActivity extends AppCompatActivity {
    ActivityAlumnoPerfilEditarBinding binding;
    Alumno alumno = new Alumno();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoPerfilEditarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = new Bundle();
        bundle.putString("header", "Editar perfil");
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeader, AlumnoHeader1Fragment.class, bundle)
                .commit();

        alumno = (Alumno) getIntent().getSerializableExtra("alumno");
        completarInputs();

        binding.inputNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputsValidos()){
                    binding.buttonGuardarPerfil.setEnabled(true);
                }
                else{
                    binding.buttonGuardarPerfil.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputsValidos()){
                    binding.buttonGuardarPerfil.setEnabled(true);
                }
                else{
                    binding.buttonGuardarPerfil.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonGuardarPerfil.setOnClickListener(view -> {
            guardarPerfil();
        });
    }

    boolean inputsValidos(){
        String nombre = binding.inputNombre.getText().toString().trim();
        String apellidos = binding.inputApellido.getText().toString().trim();
        //Log.d("msg-test", "nombre empty: "+TextUtils.isEmpty(nombre)+" | apellidos emplty: "+TextUtils.isEmpty(apellidos) + " | nombre equal: "+nombre.equals(alumno.getNombre()) + " | apellidos equal: "+apellidos.equals(alumno.getApellidos()));
        return !TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && (!nombre.equals(alumno.getNombre()) || !apellidos.equals(alumno.getApellidos()));
    }

    void guardarPerfil(){
        String nombre = binding.inputNombre.getText().toString().trim();
        String apellidos = binding.inputApellido.getText().toString().trim();
        // actaulizar info en firebase realtime database

        HashMap<String, Object> alumnoActualizado = new HashMap<>();
        alumnoActualizado.put("apellidos", apellidos);
        alumnoActualizado.put("nombre", nombre);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("alumnos").child(alumno.getCodigo()).updateChildren(alumnoActualizado).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    alumno.setNombre(nombre);
                    alumno.setApellidos(apellidos);
                    Log.d("msg-test", "actualizado en firebase realtime");

                    // actualizar foto en firebase storage

                    // guardar info en internal storage
                    Gson gson = new Gson();
                    String alumnoJson = gson.toJson(alumno);
                    try (FileOutputStream fileOutputStream = openFileOutput("userData", Context.MODE_PRIVATE);
                         FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
                        fileWriter.write(alumnoJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // regresar a perfil activity
                    finish();
                }
            }
        });
    }

    void completarInputs(){
        binding.inputNombre.setText(alumno.getNombre());
        binding.inputApellido.setText(alumno.getApellidos());
    }

}