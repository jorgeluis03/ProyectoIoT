package com.example.proyecto_iot.delegadoActividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.ActivityDaGestionEventosBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaEventosActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DaGestionEventosActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityDaGestionEventosBinding binding;
    Intent intent;
    private ListaEventosActividadesAdapter adapter = new ListaEventosActividadesAdapter();
    ArrayList<Evento> eventoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaGestionEventosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        Actividades a = (Actividades) intent.getSerializableExtra("actividadCard");
        binding.textSubtitleGestion.setText(a.getNombre());
        binding.buttonEventoABack.setOnClickListener(view -> finish());

        db.collection("actividades")
                .document(a.getId())
                .collection("eventos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                buscarEventos(document.getId());
                            }
                        }
                        else{
                            Log.d("msg-test", "AlumnoEventosApoyandoFragment: error en busqueda de eventos apoyados");
                        }
                    }
                });
        adapter.setContext(DaGestionEventosActivity.this);
        adapter.setEventoAList(eventoList);

        binding.rvActividadesEventos.setAdapter(adapter);
        binding.rvActividadesEventos.setLayoutManager(new LinearLayoutManager(DaGestionEventosActivity.this));

        binding.buttonCreateEvent2.setOnClickListener(view -> {
            Intent intent1 = new Intent(DaGestionEventosActivity.this, DaEditEventoActivity.class);
            intent1.putExtra("actividadName", a);
            startActivity(intent1);
        });
    }
    private void buscarEventos(String eventoId){
        db.collection("eventos")
                .document(eventoId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Evento evento = task.getResult().toObject(Evento.class);
                            Log.d("msg-test", "evento apoyado encontrado: "+evento.getTitulo());
                            eventoList.add(evento);
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Log.d("msg-test", "AlumnoEventosApoyandoFragment error buscando evento: "+eventoId);
                        }
                    }
                });
    }
}