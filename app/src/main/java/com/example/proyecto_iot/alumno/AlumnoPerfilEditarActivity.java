package com.example.proyecto_iot.alumno;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.FileOutputStream;
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
    private String correoNuevo;
    private String urlNuevo;
    private Uri imageUri; // data de imagen nueva seleccionada
    private boolean nuevaFoto = false;
    private String noFotoUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png";

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

        binding.inputNombre.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nombre = binding.inputNombre.getEditText().getText().toString();
                if (TextUtils.isEmpty(nombre)){
                    binding.inputNombre.setError("Ingrese un nombre");
                }
                else{
                    binding.inputNombre.setError(null);
                }

                binding.buttonGuardarPerfil.setEnabled(inputsValidos());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputApellidos.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String apellidos = binding.inputApellidos.getEditText().getText().toString();
                if (TextUtils.isEmpty(apellidos)){
                    binding.inputApellidos.setError("Ingrese un apellido");
                }
                else{
                    binding.inputApellidos.setError(null);
                }
                binding.buttonGuardarPerfil.setEnabled(inputsValidos());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.inputCorreo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String correo = binding.inputCorreo.getEditText().getText().toString();
                if (TextUtils.isEmpty(correo)){
                    binding.inputCorreo.setError("Ingrese un correo");
                }
                else{
                    binding.inputCorreo.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonQuitarFoto.setOnClickListener(view -> {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
            Glide.with(AlumnoPerfilEditarActivity.this)
                    .load(noFotoUrl)
                    .apply(requestOptions)
                    .into(binding.imageEdit);

            binding.buttonGuardarPerfil.setEnabled(true);
            nuevaFoto = true;
            obtenerUriImageView();
        });

        binding.buttonEditarFoto.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            openImageLauncher.launch(galleryIntent);
        });

        binding.buttonGuardarPerfil.setOnClickListener(view -> {
            guardarPerfil();
        });
    }

    ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK){
                    imageUri = result.getData().getData();
                    binding.imageEdit.setImageURI(imageUri);
                    binding.buttonGuardarPerfil.setEnabled(true);
                    nuevaFoto = true;
                }
            }
    );

    private boolean inputsValidos() {
        String nombre = binding.inputNombre.getEditText().getText().toString().trim();
        String apellidos = binding.inputApellidos.getEditText().getText().toString().trim();
        return !TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && (!nombre.equals(alumno.getNombre()) || !apellidos.equals(alumno.getApellidos()));
    }

    private void guardarPerfil() {
        binding.buttonGuardarPerfil.setEnabled(false);
        binding.progressBarGuardarPerfil.setVisibility(View.VISIBLE);

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
        nombreNuevo = binding.inputNombre.getEditText().getText().toString().trim();
        apellidosNuevos = binding.inputApellidos.getEditText().getText().toString().trim();
        correoNuevo = binding.inputCorreo.getEditText().getText().toString().trim();

        HashMap<String, Object> infoActualizada = new HashMap<>();
        infoActualizada.put("apellidos", apellidosNuevos);
        infoActualizada.put("nombre", nombreNuevo);
        infoActualizada.put("correo", correoNuevo);

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

                        actualizarCorreoFirebase();
                        actualizarInternalStorage();

                        // regresar a perfil activity
                        binding.progressBarGuardarPerfil.setVisibility(View.GONE);
                        Toast.makeText(AlumnoPerfilEditarActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
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

    private void actualizarCorreoFirebase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(correoNuevo)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("msg-test", "User email address updated.");
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
        binding.inputNombre.getEditText().setText(alumno.getNombre());
        binding.inputApellidos.getEditText().setText(alumno.getApellidos());
        binding.inputCorreo.getEditText().setText(alumno.getCorreo());
        cargarFoto();
    }

    private void cargarFoto() {
        String url = alumno.getFotoUrl().equals("")? "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_640.png": alumno.getFotoUrl();
        Glide.with(AlumnoPerfilEditarActivity.this)
                .load(url)
                .into(binding.imageEdit);
    }

    private void obtenerUriImageView(){
        Glide.with(AlumnoPerfilEditarActivity.this)
                .asBitmap()
                .load(noFotoUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String path = MediaStore.Images.Media.insertImage( // bitmap en archivo temporal
                                getContentResolver(),
                                resource,
                                "noFotoImagen",
                                null
                        );
                        imageUri = Uri.parse(path);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

}