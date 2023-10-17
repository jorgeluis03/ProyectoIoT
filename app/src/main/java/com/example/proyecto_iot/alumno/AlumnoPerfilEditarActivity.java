package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader1Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoPerfilEditarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AlumnoPerfilEditarActivity extends AppCompatActivity {
    private ActivityAlumnoPerfilEditarBinding binding;
    private Alumno alumno = new Alumno();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // autenticacion
    private FirebaseFirestore db; // cloud firestore
    private FirebaseStorage storage; // storage
    private Uri imageUri;
    int GALLERY_REQUEST_CODE = 200;

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
        cargarInfo();

        binding.inputNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputsValidos()) {
                    binding.buttonGuardarPerfil.setEnabled(true);
                } else {
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
                if (inputsValidos()) {
                    binding.buttonGuardarPerfil.setEnabled(true);
                } else {
                    binding.buttonGuardarPerfil.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonEditarFoto.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        binding.buttonGuardarPerfil.setOnClickListener(view -> {
            guardarPerfil();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                binding.imageEdit.setImageURI(imageUri);
                binding.buttonGuardarPerfil.setEnabled(true);
            } else {
                Log.d("msg-test", "imagen no seleccionada");
            }
        }
    }

    boolean inputsValidos() {
        String nombre = binding.inputNombre.getText().toString().trim();
        String apellidos = binding.inputApellido.getText().toString().trim();
        //Log.d("msg-test", "nombre empty: "+TextUtils.isEmpty(nombre)+" | apellidos emplty: "+TextUtils.isEmpty(apellidos) + " | nombre equal: "+nombre.equals(alumno.getNombre()) + " | apellidos equal: "+apellidos.equals(alumno.getApellidos()));
        return !TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && (!nombre.equals(alumno.getNombre()) || !apellidos.equals(alumno.getApellidos()));
    }

    void guardarPerfil() {
        String userUid = mAuth.getCurrentUser().getUid();
        String nombre = binding.inputNombre.getText().toString().trim();
        String apellidos = binding.inputApellido.getText().toString().trim();

        // actaulizar info en firebase realtime database
        HashMap<String, Object> infoActualizada = new HashMap<>();
        infoActualizada.put("apellidos", apellidos);
        infoActualizada.put("nombre", nombre);

        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("alumnos").document(userUid);
        actualizarFotoPerfil();
        /* DESCOMENTAR PARA GESTIONAR CUANDO SI Y CUANDO NO ACTUALIZAR
        documentReference
                .update(infoActualizada)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("msg-test", "actualizado en firestore");

                        // guardar info en internal storage
                        alumno.setNombre(nombre);
                        alumno.setApellidos(apellidos);
                        actualizarInternalStorage();
                        actualizarFotoPerfil();

                        // regresar a perfil activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msg-test", "Error updating document: " + e);
                    }
                });

         */
    }

    void actualizarFotoPerfil(){
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("images/"+mAuth.getCurrentUser().getUid()+".jpg");
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("msg-test", "imagen subida");
                        Log.d("msg-test", "url: "+storageReference.getDownloadUrl());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msg-test", "error al subir imagen: "+e.getMessage());
                        finish();
                    }
                });
    }

    void actualizarInternalStorage() {
        Gson gson = new Gson();
        String alumnoJson = gson.toJson(alumno);
        try (FileOutputStream fileOutputStream = openFileOutput("userData", Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(alumnoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void cargarInfo() {
        binding.inputNombre.setText(alumno.getNombre());
        binding.inputApellido.setText(alumno.getApellidos());

        cargarFoto(alumno);
    }

    void cargarFoto(Alumno alumno){
        String url = alumno.getFotoUrl();
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
        Glide.with(AlumnoPerfilEditarActivity.this)
                .load(url)
                .apply(requestOptions)
                .into(binding.imageEdit);
    }

}