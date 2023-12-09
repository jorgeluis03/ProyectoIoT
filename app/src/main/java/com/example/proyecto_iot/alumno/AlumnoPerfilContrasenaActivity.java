package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader1Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoPerfilContrasenaBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AlumnoPerfilContrasenaActivity extends AppCompatActivity {
    ActivityAlumnoPerfilContrasenaBinding binding;
    Alumno alumno = new Alumno();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoPerfilContrasenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = new Bundle();
        bundle.putString("header", "Contraseña");
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeader, AlumnoHeader1Fragment.class, bundle)
                .commit();

        alumno = (Alumno) getIntent().getSerializableExtra("alumno");

        binding.inputContrasena1.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputsValidos()) {
                    binding.buttonGuardarContrasena.setEnabled(true);
                } else {
                    binding.buttonGuardarContrasena.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputContrasena2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputsValidos()) {
                    binding.buttonGuardarContrasena.setEnabled(true);
                } else {
                    binding.buttonGuardarContrasena.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.inputContrasena3.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputsValidos()) {
                    binding.buttonGuardarContrasena.setEnabled(true);
                } else {
                    binding.buttonGuardarContrasena.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.buttonGuardarContrasena.setOnClickListener(view -> {
            String contrasena1 = binding.inputContrasena1.getEditText().getText().toString().trim();
            String contrasena2 = binding.inputContrasena2.getEditText().getText().toString().trim();
            String contrasena3 = binding.inputContrasena3.getEditText().getText().toString().trim();
            Log.d("msg-test", "pass1: "+contrasena1+" | pass2: "+contrasena2+" | pass3: "+contrasena3);
            binding.buttonGuardarContrasena.setEnabled(false);
            guardarContrasena();
        });
    }

    void guardarContrasena() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String contrasena1 = binding.inputContrasena1.getEditText().getText().toString().trim();
        String contrasena2 = binding.inputContrasena2.getEditText().getText().toString().trim();
        String contrasena3 = binding.inputContrasena3.getEditText().getText().toString().trim();

        //reautenticando para validar contraseña actual
        AuthCredential credential = EmailAuthProvider.getCredential(alumno.getCodigo() + "@app.com", contrasena1);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updatePassword(contrasena2)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("msg-test", "contraseña actualizada");
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("msg-test", "error al actualizar contraseña: "+e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msg-test", "contraseña actual INVALIDA: "+e.getMessage());
                    }
                });
    }

    boolean inputsValidos() {
        String contrasena1 = binding.inputContrasena1.getEditText().getText().toString().trim();
        String contrasena2 = binding.inputContrasena2.getEditText().getText().toString().trim();
        String contrasena3 = binding.inputContrasena3.getEditText().getText().toString().trim();

        return !TextUtils.isEmpty(contrasena1) && !TextUtils.isEmpty(contrasena2) && !TextUtils.isEmpty(contrasena3);
    }
}