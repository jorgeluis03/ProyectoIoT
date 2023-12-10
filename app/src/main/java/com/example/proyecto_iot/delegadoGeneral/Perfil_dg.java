package com.example.proyecto_iot.delegadoGeneral;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityPerfilDgBinding;
import com.example.proyecto_iot.delegadoGeneral.utils.AndroidUtilDg;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Perfil_dg extends AppCompatActivity {
    ActivityPerfilDgBinding binding;
    Alumno usuarioActual;
    LinearLayout linearLayout;
    TextInputLayout textFielnombre;
    TextInputLayout textFielapellido;
    TextInputLayout textFielcorreo;
    TextInputLayout textFielcodigo;
    ImageView imgPerfil;
    Uri uriImgPerfilSeleccionada;
    ProgressBar progressBar, progressBarInfo;
    ImageButton btnBack;
    String encodedImage;

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
        progressBarInfo = binding.progressBarCargaInfo;
        linearLayout = binding.linearLayoutInfo;
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

        binding.frameLayoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
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
        usuarioActual.setFotoUrl(encodedImage);

        setInProgress(true);
        subirAlFirestore();


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


        FirebaseUtilDg.getUsuarioActualDetalles().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                setInProgress(false);
                usuarioActual = task.getResult().toObject(Alumno.class);
                textFielnombre.getEditText().setText(usuarioActual.getNombre());
                textFielapellido.getEditText().setText(usuarioActual.getApellidos());
                textFielcorreo.getEditText().setText(usuarioActual.getCorreo());
                textFielcodigo.getEditText().setText(usuarioActual.getCodigo());
                byte[] bytes = Base64.decode(usuarioActual.getFotoUrl(),Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                binding.imageViewPerfildg.setImageBitmap(bitmap);//cargar la imagen

            }
        });
    }

    ActivityResultLauncher<Intent> imgPerfilLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
       if(result.getResultCode()== AppCompatActivity.RESULT_OK){
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
    public void inProgressInfoPerfil(boolean enProgreso){
        if (enProgreso){

        }else {

        }
    }

    private Bitmap getBitmapFromEncodedImage(String encodedImage){ //obtiene la imagen url y la pasa a formato Bitmap para mostrarla
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
    private String encodeImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeigth =bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeigth,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result ->{
        if(result.getResultCode()==RESULT_OK){
            if(result.getData()!=null){
                Uri imageUri = result.getData().getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.imageViewPerfildg.setImageBitmap(bitmap);
                    binding.textAgregarImagen.setVisibility(View.GONE);
                    encodedImage = encodeImage(bitmap);//pone la imagen como url
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    });

    //Flecha para regrasar al inicio
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}