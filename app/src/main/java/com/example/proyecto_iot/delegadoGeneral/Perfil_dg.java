package com.example.proyecto_iot.delegadoGeneral;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityPerfilDgBinding;
import com.example.proyecto_iot.delegadoGeneral.utils.AndroidUtilDg;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Perfil_dg extends AppCompatActivity {
    ActivityPerfilDgBinding binding;
    Alumno usuarioActual;

    TextInputLayout textFielnombre;
    TextInputLayout textFielapellido;
    TextInputLayout textFielcorreo;
    TextInputLayout textFielcodigo;
    ImageView imgPerfil;
    Uri uriImgPerfilSeleccionada;
    ProgressBar progressBar;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilDgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        textFielnombre = binding.textFieldNombrePerfil;
        textFielapellido = binding.textFieldApellidoPerfil;
        textFielcodigo = binding.textFieldCodigoPerfil;
        textFielcorreo = binding.textFieldCorreoPerfil;
        imgPerfil = binding.imageViewPerfildg;
        progressBar = binding.profileProgressBar;
        btnBack = binding.buttonBack;

        getDatosUsuario();

        binding.fbEditar.setOnClickListener(view -> {
            textFielnombre.setEnabled(true);
            textFielapellido.setEnabled(true);

        });
        binding.fbGuardar.setOnClickListener(view -> {
            textFielnombre.setEnabled(false);
            textFielapellido.setEnabled(false);

            btnGuardarCambios();

        });

        //cambiar de foto de perfil al hacer click en la imagen y mostrarla en el layout
        imgPerfil.setOnClickListener(view -> {

            ImagePicker.with(this)
                    .cropSquare()
                    .compress(512)
                    .maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imgPerfilLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        btnBack.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
    public void btnGuardarCambios(){
        String newNombre = textFielnombre.getEditText().getText().toString();
        String newApellido = textFielapellido.getEditText().getText().toString();

        if(newNombre.isEmpty() || newNombre.length()<3){
            textFielnombre.setError("El nombre de usuario debe tener más de 3 caracteres");
            return;
        }
        if(newApellido.isEmpty() || newApellido.length()<3){
            textFielapellido.setError("El nombre de usuario debe tener más de 3 caracteres");
            return;
        }

        //actualizar el nomrbre y apelldio del usuario
        usuarioActual.setNombre(newNombre);
        usuarioActual.setApellidos(newApellido);

        setInProgress(true);

        //antes de actualizar cualquier usuario obtenedremos el almacenamiento y agregar la Uri de imagen
        if(uriImgPerfilSeleccionada!=null){
            FirebaseUtilDg.getPerfilUsuarioActualPicStorageRef().putFile(uriImgPerfilSeleccionada)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            subirAlFirestore();
                        }
                    });
        }else {
            subirAlFirestore();
        }


    }
    public void subirAlFirestore(){
        FirebaseUtilDg.getUsuarioActualDetalles().set(usuarioActual).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                setInProgress(false);
                Toast.makeText(this,"Perfil actualizado", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"No se puddo actualizar el perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getDatosUsuario(){
        setInProgress(true);

        //obtener la foto mia de FirebaseStorage (Descargar archivos)
        FirebaseUtilDg.getPerfilUsuarioActualPicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        //carga el uri en la ImageView
                        AndroidUtilDg.setPerfilImg(this,uri,imgPerfil);
                    }
                });

        FirebaseUtilDg.getUsuarioActualDetalles().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                setInProgress(false);
                usuarioActual = task.getResult().toObject(Alumno.class);
                textFielnombre.getEditText().setText(usuarioActual.getNombre());
                textFielapellido.getEditText().setText(usuarioActual.getApellidos());
                textFielcorreo.getEditText().setText(usuarioActual.getCorreo());
                textFielcodigo.getEditText().setText(usuarioActual.getCodigo());

            }
        });
    }

    ActivityResultLauncher<Intent> imgPerfilLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
       if(result.getResultCode()== Activity.RESULT_OK){
            Intent data = result.getData();
            if(data!=null && data.getData()!=null){
                uriImgPerfilSeleccionada = data.getData();
                AndroidUtilDg.setPerfilImg(this,uriImgPerfilSeleccionada,imgPerfil);
            }
       }
    });

    public void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);

        }else {
            progressBar.setVisibility(View.GONE);

        }
    }

    //Flecha para regrasar al inicio
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}