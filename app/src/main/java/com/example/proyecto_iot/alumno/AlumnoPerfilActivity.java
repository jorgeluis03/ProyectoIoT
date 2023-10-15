package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader1Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoPerfilBinding;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlumnoPerfilActivity extends AppCompatActivity {

    private ActivityAlumnoPerfilBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = new Bundle();
        bundle.putString("header", "Perfil");
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeader, AlumnoHeader1Fragment.class, bundle)
                .commit();

        binding.buttonEditarPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(AlumnoPerfilActivity.this, AlumnoPerfilEditarActivity.class);
            startActivity(intent);
        });

        binding.buttonContrasena.setOnClickListener(view -> {
            Intent intent = new Intent(AlumnoPerfilActivity.this, AlumnoPerfilContrasenaActivity.class);
            startActivity(intent);
        });

        binding.buttonCerrarSesion.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AlumnoPerfilActivity.this, IngresarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String codigo = currentUser.getEmail().substring(0, 8);
            completarPerfilInfo(codigo);
        }
    }

    // reemplazar por obtener info desde internal storage
    void completarPerfilInfo(String codigo){
        reference = FirebaseDatabase.getInstance().getReference("alumnos");
        reference.child(codigo).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult().exists()){
                    DataSnapshot dataSnapshot = task.getResult();
                    String nombre = String.valueOf(dataSnapshot.child("nombre").getValue());
                    String apellidos = String.valueOf(dataSnapshot.child("apellidos").getValue());
                    String rol = String.valueOf(dataSnapshot.child("rol").getValue());
                    String foto = String.valueOf(dataSnapshot.child("foto").getValue());

                    binding.textNombre.setText(nombre+" "+apellidos);
                    binding.textRol.setText(rol);
                    cargarFoto();
                }
                else{
                    Log.d("msg-test", "error: usuario no encontrado");
                }
            }
            else{
                Log.d("msg-test", "error: error al realizar busqueda");
            }
        });
    }

    void cargarFoto(){

    }
}