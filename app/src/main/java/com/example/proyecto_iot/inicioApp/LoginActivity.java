package com.example.proyecto_iot.inicioApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Objetos.Alumno;
import com.example.proyecto_iot.databinding.ActivityLoginBinding;
import com.example.proyecto_iot.delegadoActividad.DaInicioActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); // autenticacion
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // cloud firestore
    StorageReference storageRef = FirebaseStorage.getInstance().getReference(); //storage
    Alumno alumno = new Alumno();

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
            String codigo = binding.inputCodigo.getText().toString();
            String contrasena = binding.inputContrasena.getText().toString();

            mAuth.signInWithEmailAndPassword(codigo + "@app.com", contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                obtenerUserData();

                            } else {
                                // If sign in fails, display a message to the user.
                                Snackbar.make(binding.getRoot(), "Las credenciales son incorrectas.", Snackbar.LENGTH_SHORT)
                                        .show();
                                Log.d("msg-test", "credenciales incorrectas");
                            }
                        }
                    });
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

                redirigirSegunRol(alumnoAutenticado.getRol());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    void obtenerUserData() {
        String userUid = mAuth.getCurrentUser().getUid();
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
                            redirigirSegunRol(alumno.getRol());
                        } else if (alumno.getEstado().equals("pendiente")) {
                            Log.d("msg-test", "alumno con estado pendiente");
                        }
                        // considerar baneado tambien :u
                    } else {
                        Log.d("msg-test", "error: usuario no encontrado");
                    }
                }
                else{
                    Log.d("msg-test", "error en busqueda: "+task.getException());
                }
            }
        });
    }

    void guardarDataEnMemoria() {
        Gson gson = new Gson();
        String alumnoJson = gson.toJson(alumno);
        try (FileOutputStream fileOutputStream = openFileOutput("userData", Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(alumnoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void redirigirSegunRol(String rol) {
        Intent intent = null;
        switch (rol) {
            case "Alumno":
                intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                break;
            case "Delegado Actividad":
                intent = new Intent(LoginActivity.this, DaInicioActivity.class);
                break;
            case "Delegado General":
                intent = new Intent(LoginActivity.this, Dg_Activity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}