package com.example.proyecto_iot.inicioApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name;
    private String lastName;
    private String code;
    private String email;
    private String pass;
    private String type;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String CHANNEL_ID = "canalDelegadoGeneral";
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

                // crear usuario en firebase authentication
                mAuth.createUserWithEmailAndPassword(code+"@app.com", pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d("msg-test", "usuario creado en authentication");
                                    // crear usuario en firestore
                                    crearUsuarioFirestore();
                                }
                                else{
                                    Log.d("msg-test", "error al crear usuario");
                                }
                            }
                        });
            }
        });
    }

    boolean validFields(){
        name = binding.editNameSign.getEditText().getText().toString();
        lastName = binding.editLastnameSign.getEditText().getText().toString();
        code = binding.editCodeSign.getEditText().getText().toString();
        email = binding.editEmailSign.getEditText().getText().toString();
        pass = binding.editPasswSign.getEditText().getText().toString(); // validar que contraseña tenga al menos 6 caracteres
        type = binding.userTypeSpinner.getEditText().getText().toString();

        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(code) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(type);
    }

    void crearUsuarioFirestore(){
        FirebaseUser user = mAuth.getCurrentUser();
        Alumno nuevoAlumno = new Alumno(user.getUid(),name, lastName, "Alumno", code, email, "", type, "inactivo");
        db.collection("alumnos")
                .document(user.getUid())
                .set(nuevoAlumno)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "usuario guardado en firestore");
                    // si se puede enviar correo xd
                    //Notificacion


                    Intent intent = new Intent(RegistroActivity.this, ConfirmarregistroActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

}