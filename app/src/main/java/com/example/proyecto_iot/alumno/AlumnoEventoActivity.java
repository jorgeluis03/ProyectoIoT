package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Fragments.AlumnoApoyandoButtonFragment;
import com.example.proyecto_iot.alumno.Fragments.AlumnoApoyarButtonFragment;
import com.example.proyecto_iot.databinding.ActivityAlumnoEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlumnoEventoActivity extends AppCompatActivity {

    private ActivityAlumnoEventoBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Evento evento;
    private String eventoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        evento = (Evento) getIntent().getSerializableExtra("evento");
        cargarInfoEvento();
        insertarFragmentButtons(savedInstanceState);

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

    public Evento getEvento() {
        return evento;
    }
}