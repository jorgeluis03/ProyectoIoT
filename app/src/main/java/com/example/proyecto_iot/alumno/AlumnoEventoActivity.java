package com.example.proyecto_iot.alumno;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Foto;
import com.example.proyecto_iot.alumno.Fragments.AlumnoApoyandoButtonFragment;
import com.example.proyecto_iot.alumno.Fragments.AlumnoApoyarButtonFragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Timestamp;

public class AlumnoEventoActivity extends AppCompatActivity {

    private ActivityAlumnoEventoBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Evento evento;
    private Uri imageUri;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        evento = (Evento) getIntent().getSerializableExtra("evento");
        cargarInfoEvento();
        cargarFotos();
        insertarFragmentButtons(savedInstanceState);

        binding.buttonSubirFotos.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            openImageLauncher.launch(galleryIntent);
        });

        binding.buttonEventoBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void insertarFragmentButtons(Bundle savedInstanceState){
        db.collection("alumnos")
                .document(userUid)
                .collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (savedInstanceState == null){
                                if (document.exists()){ // evento en lista de eventos de alumno (evento apoyado)
                                    getSupportFragmentManager().beginTransaction()
                                            .setReorderingAllowed(true)
                                            .add(R.id.fragmentEventoButtons, AlumnoApoyandoButtonFragment.class, null)
                                            .commit();

                                    binding.buttonSubirFotos.setVisibility(View.VISIBLE);
                                }
                                else{ // evento no apoyado
                                    getSupportFragmentManager().beginTransaction()
                                            .setReorderingAllowed(true)
                                            .add(R.id.fragmentEventoButtons, AlumnoApoyarButtonFragment.class, null)
                                            .commit();
                                }
                            }
                        }
                        else{
                            Log.d("msg-test", "AlumnoEventoActivity: error al buscar evento");
                        }
                    }
                });

    }

    private ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    imageUri = result.getData().getData();
                    //abrir dialog de subir foto
                    abrirDialogSubirFoto();
                }
            }
    );

    private void cargarFotos(){
        db.collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .collection("fotos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            Foto foto = document.toObject(Foto.class);
                        }
                    }
                    else{
                        Log.d("msg-test", "error al cargar fotos");
                    }
                });
    }

    private void abrirDialogSubirFoto(){
        bottomSheetDialog = new BottomSheetDialog(AlumnoEventoActivity.this);
        View bottomSheetView = LayoutInflater.from(AlumnoEventoActivity.this).inflate(R.layout.dialog_alumno_subir_foto, (ConstraintLayout) findViewById(R.id.bottomSheetSubirFoto));

        ImageView imagenFoto = bottomSheetView.findViewById(R.id.imageFoto);
        imagenFoto.setImageURI(imageUri);
        Button botonSubirFoto = bottomSheetView.findViewById(R.id.buttonDialogSubirFoto);
        botonSubirFoto.setOnClickListener(view -> {
            // subir foto a firestore y storage
            String descripcion = bottomSheetView.findViewById(R.id.inputDescripcion).toString();
            Timestamp fechaHoraSubida = new Timestamp(System.currentTimeMillis());
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void cargarInfoEvento(){
        binding.textEventoTitulo.setText(evento.getTitulo());
        binding.textEventoActividad.setText(evento.getActividad());
        binding.textEventoDescripcion.setText(evento.getDescripcion());
        binding.buttonEventoFecha.setText(evento.getFecha());
        binding.buttonEventoHora.setText(evento.getHora());

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
        Glide.with(AlumnoEventoActivity.this)
                .load(evento.getFotoUrl())
                .apply(requestOptions)
                .into(binding.imageEvento);
    }

    // funcion necesaria para chat
    public Evento getEvento() {
        return evento;
    }
}