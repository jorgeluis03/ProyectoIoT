package com.example.proyecto_iot.inicioApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityLoginBinding;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    boolean check = false;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ArrayList<Alumno> usuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // alumno
        Alumno alumno = new Alumno("Noe", "Martinez", "alumno", "20203248", "a20203248@pucp.edu.pe", "messi");
        // delegado actividad
        Alumno delegadoActividad = new Alumno("", "", "", "20203554", "", "pipipi");

        //credenciales.put("20200643", "bicho"); // delegado general

        usuarios.add(alumno);
        usuarios.add(delegadoActividad);

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
                                check = true;
                                redirigirSegunRol(codigo);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("msg-test", "xd");
                            }
                        }
                    });

            if (!check) {
                Snackbar.make(binding.getRoot(), "Las credenciales son incorrectas.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void redirigirSegunRol(String codigo) {
        reference = FirebaseDatabase.getInstance().getReference("alumnos");
        reference.child(codigo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String rol = String.valueOf(dataSnapshot.child("rol").getValue());
                        Intent intent = null;
                        switch (rol) {
                            case "Alumno":
                                intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                                break;
                            case "DelegadoActividad":
                                intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                                break;
                            case "DelegadoGeneral":
                                intent = new Intent(LoginActivity.this, Dg_Activity.class);
                                break;
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("msg-test", "error: usuario no encontrado");
                    }
                } else {
                    Log.d("msg-test", "error: error al realizar busqueda");
                }
            }
        });
    }
}