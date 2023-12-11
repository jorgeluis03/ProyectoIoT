package com.example.proyecto_iot.inicioApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.util.Patterns;
import android.widget.Toast;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityLoginBinding;
import com.example.proyecto_iot.delegadoActividad.DaInicioActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); // autenticacion
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // cloud firestore
    StorageReference storageRef = FirebaseStorage.getInstance().getReference(); //storage
    Alumno alumno = new Alumno();
    String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton2.setOnClickListener(view -> {
            finish();
        });
        binding.loginSigninButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
        binding.forgotPasswButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        binding.loginButton.setOnClickListener(view -> {
            loguearUsuario();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            try (FileInputStream fileInputStream = openFileInput("userData");
                 FileReader fileReader = new FileReader(fileInputStream.getFD());
                 BufferedReader bufferedReader = new BufferedReader(fileReader)){

                String jsonData = bufferedReader.readLine();
                Gson gson = new Gson();
                Alumno alumnoAutenticado = gson.fromJson(jsonData, Alumno.class);

                redirigirSegunRol(alumnoAutenticado);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void loguearUsuario() {
        binding.loginButton.setEnabled(false);
        binding.relativeOverlay.setVisibility(View.VISIBLE);

        String correoOCodigo = binding.inputCorreo.getText().toString();
        String contrasena = binding.inputContrasena.getText().toString();

        if (esCorreo(correoOCodigo)) {
            loguearConCorreo(correoOCodigo, contrasena);
        } else { // se esta ingresando con codigo
            loguearConCodigo(correoOCodigo, contrasena);
        }
    }

    private void loguearConCorreo(String correo, String contrasena){
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            obtenerUserData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Las credenciales son incorrectas", Toast.LENGTH_SHORT).show();

                            habilitarLogueo();

                            Log.d("msg-test", "credenciales incorrectas");
                        }
                    }
                });
    }

    private void loguearConCodigo(String codigo, String contrasena){
        db.collection("alumnos")
                .whereEqualTo("codigo", codigo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().size() > 0){ // se encontro al alumno con el codigo ingresado
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                Log.d("msg-test", "doc: "+doc);
                                Alumno alumno = doc.toObject(Alumno.class);
                                loguearConCorreo(alumno.getCorreo(), contrasena);
                            }
                        }
                        else{
                            Toast.makeText(this, "El código ingresado no es válido", Toast.LENGTH_SHORT).show();
                            habilitarLogueo();
                        }
                    }
                    else{
                        Log.d("msg-test", "Error al recuperar alumno mediante codigo: "+task.getException().getMessage());
                        habilitarLogueo();
                    }

                });
    }

    void obtenerUserData() {
        userUid = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("alumnos").document(userUid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("msg-test", "busqueda ok");
                        alumno = document.toObject(Alumno.class);
                        if (alumno.getEstado().equals("activo")){

                            guardarDataEnMemoria(); // guardando data de usuario en internal storage para un manejo más rapido
                            redirigirSegunRol(alumno);

                        } else if (alumno.getEstado().equals("pendiente")) {
                            Log.d("msg-test", "alumno con estado pendiente");
                            habilitarLogueo();
                        }
                        else if (alumno.getEstado().equals("inactivo")) {
                            Toast.makeText(LoginActivity.this, "El usuario aún no ha sido aceptado por el administrador de la aplicación.", Toast.LENGTH_SHORT).show();
                            habilitarLogueo();
                        }
                        else if (alumno.getEstado().equals("baneado")) {
                            Toast.makeText(LoginActivity.this, "El usuario ha sido baneado por el administrador de la aplicacion.", Toast.LENGTH_SHORT).show();
                            habilitarLogueo();
                        }

                    } else {
                        Log.d("msg-test", "error: usuario no encontrado");
                        Toast.makeText(LoginActivity.this, "Ocurrió un error al encontrar su usuario", Toast.LENGTH_SHORT).show();
                        habilitarLogueo();
                    }
                }
                else{
                    Log.d("msg-test", "error en busqueda: "+task.getException());
                    Toast.makeText(LoginActivity.this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    habilitarLogueo();
                }
            }
        });
    }


    private void guardarDataEnMemoria() {
        Gson gson = new Gson();
        String alumnoJson = gson.toJson(alumno);
        try (FileOutputStream fileOutputStream = openFileOutput("userData", Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(alumnoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void redirigirSegunRol(Alumno alumno) {
        Intent intent = null;
        String rol = alumno.getRol();
        ArrayList<Actividades> actividades = alumno.getActividadesId();
        boolean valido = false;
        switch (rol) {
            case "Alumno":
                if (actividades == null){
                    // caso no actividades
                    intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                    break;
                }else {
                    for(Actividades a: actividades){
                        if (a.getEstado().equals("abierto")){
                            valido = true;
                        }
                    }
                    if (valido){
                        // caso delegadoActividad
                        intent = new Intent(LoginActivity.this, DaInicioActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        break;
                    }
                    // caso no actividades
                    intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    break;
                }
            case "Delegado General":
                intent = new Intent(LoginActivity.this, Dg_Activity.class);
                break;
        }
        binding.loginButton.setEnabled(true);
        binding.relativeOverlay.setVisibility(View.GONE);
        startActivity(intent);
    }

    private boolean esCorreo(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void habilitarLogueo(){
        FirebaseAuth.getInstance().signOut();
        binding.relativeOverlay.setVisibility(View.GONE);
        binding.loginButton.setEnabled(true);
    }
}