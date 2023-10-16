package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader1Fragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoPerfilBinding;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class AlumnoPerfilActivity extends AppCompatActivity {

    private ActivityAlumnoPerfilBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Alumno alumno = new Alumno();

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
            intent.putExtra("alumno", alumno);
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
            completarPerfilInfo();
            //cargarFoto();
        }
    }

    // reemplazar por obtener info desde internal storage
    void completarPerfilInfo(){
        try (FileInputStream fileInputStream = openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            alumno = gson.fromJson(jsonData, Alumno.class);

            binding.textNombre.setText(alumno.getNombre()+" "+alumno.getApellidos());
            binding.textRol.setText(alumno.getRol());

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    void cargarFoto(){
        // recuperar foto de usario desde internal storage (se esta recuperando un archivo binario)
        Log.d("msg-test", "recuperando foto de shard preferences");
        SharedPreferences sharedPreferences = getSharedPreferences("userFiles", MODE_PRIVATE);
        String image = sharedPreferences.getString("userImage", null);
        Bitmap bitmap = BitmapFactory.decodeFile(image);

        // setear foto en imageview
        binding.imagePerfil.setImageBitmap(bitmap);
        /*
        try (FileInputStream fileInputStream = openFileInput("userImage");
             FileInputStream fileReader = new FileInputStream(fileInputStream.getFD())) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = fileReader.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            byte[] imageData = buffer.toByteArray();

            // setear foto en imageview
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            binding.imagePerfil.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}