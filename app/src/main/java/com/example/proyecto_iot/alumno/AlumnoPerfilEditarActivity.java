package com.example.proyecto_iot.alumno;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.tasks.Continuation;
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
    private String nombreNuevo;
    private String apellidosNuevos;
    private String urlNuevo;
    private Uri imageUri; // data de imagen nueva seleccionada
    private boolean nuevaFoto = false;
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
                binding.buttonGuardarPerfil.setEnabled(inputsValidos());
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
                binding.buttonGuardarPerfil.setEnabled(inputsValidos());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonQuitarFoto.setOnClickListener(view -> {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
            Glide.with(AlumnoPerfilEditarActivity.this)
                    .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png")
                    .apply(requestOptions)
                    .into(binding.imageEdit);
            nuevaFoto = true;
        });

        binding.buttonEditarFoto.setOnClickListener(view -> {
            //Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            Intent galleryIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            openImageLauncher.launch(galleryIntent);
        });

        binding.buttonGuardarPerfil.setOnClickListener(view -> {
            guardarPerfil();
        });
    }

    ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    imageUri = result.getData().getData();
                    binding.imageEdit.setImageURI(imageUri);
                    binding.buttonGuardarPerfil.setEnabled(true);
                    nuevaFoto = true;
                }
            }
    );

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                binding.imageEdit.setImageURI(imageUri);
                binding.buttonGuardarPerfil.setEnabled(true);
                nuevaFoto = true;
            } else {
                Log.d("msg-test", "imagen no seleccionada");
            }
        }
    }

     */

    private boolean inputsValidos() {
        String nombre = binding.inputNombre.getText().toString().trim();
        String apellidos = binding.inputApellido.getText().toString().trim();
        return !TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && (!nombre.equals(alumno.getNombre()) || !apellidos.equals(alumno.getApellidos()));
    }

    private void guardarPerfil() {

        if (nuevaFoto){
            actualizarFotoEInfo();
        }
        else{
            actualizarInfo();
        }
    }

    private void actualizarFotoEInfo(){
        // primero actualizando la imagen de perfil
        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("images/" + mAuth.getCurrentUser().getUid() + ".jpg");
        storageReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("msg-test", "foto actualizada en storage");
                    urlNuevo = task.getResult().toString();
                    Log.d("msg-test", "nueva url: " + urlNuevo);
                    alumno.setFotoUrl(urlNuevo); // seteado para cuando se guarde en internal storage y firestore

                    // luego actualizando el resto de informacion
                    actualizarInfo();
                } else {
                    Log.d("msg-test", "error");
                }
            }
        });
    }

    private void actualizarInfo() {
        String userUid = mAuth.getCurrentUser().getUid();
        nombreNuevo = binding.inputNombre.getText().toString().trim();
        apellidosNuevos = binding.inputApellido.getText().toString().trim();

        HashMap<String, Object> infoActualizada = new HashMap<>();
        infoActualizada.put("apellidos", apellidosNuevos);
        infoActualizada.put("nombre", nombreNuevo);
        if (nuevaFoto){
            infoActualizada.put("fotoUrl", urlNuevo);
        }

        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("alumnos").document(userUid);
        documentReference
                .update(infoActualizada)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("msg-test", "actualizado en firestore");

                        // guardar info en internal storage
                        alumno.setNombre(nombreNuevo);
                        alumno.setApellidos(apellidosNuevos);
                        if (nuevaFoto){
                            alumno.setFotoUrl(urlNuevo);
                        }
                        actualizarInternalStorage();

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
    }

    private void actualizarInternalStorage() {
        Gson gson = new Gson();
        String alumnoJson = gson.toJson(alumno);
        try (FileOutputStream fileOutputStream = openFileOutput("userData", Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(alumnoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarInfo() {
        binding.inputNombre.setText(alumno.getNombre());
        binding.inputApellido.setText(alumno.getApellidos());
        cargarFoto();
    }

    private void cargarFoto() {
        String url = alumno.getFotoUrl().equals("")? "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png": alumno.getFotoUrl();
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
        Glide.with(AlumnoPerfilEditarActivity.this)
                .load(url)
                .apply(requestOptions)
                .into(binding.imageEdit);
    }

}