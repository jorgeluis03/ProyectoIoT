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
import java.util.Date;

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
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        for (QueryDocumentSnapshot document: value){
                            buscarEventos(document.getId());
                        }
                    }
                    if (error != null){
                        Log.d("msg-test", "AlumnoEventosApoyandoFragment: error en busqueda de eventos apoyados");
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
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.d("msg-test", "AlumnoEventosApoyandoFragment error escuchando cambios en evento: " + eventoId, e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        
                        Evento evento = snapshot.toObject(Evento.class);
                        if (!eventoListContainsId("evento"+evento.getFechaHoraCreacion())) {
                            Log.d("msg-test", "evento apoyado encontrado: " + evento.getTitulo());
                            eventoList.add(evento);
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            removerDeLista(evento.getFechaHoraCreacion());
                            eventoList.add(evento);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("msg-test", "AlumnoEventosApoyandoFragment: No se encontró el evento con ID: " + eventoId);
                    }
                });
    }

    private void removerDeLista(Date fechaHoraCreacion) {
        int posicion = -1;
        for (int i = 0; i < eventoList.size(); i++) {
            if (eventoList.get(i).getFechaHoraCreacion().toString().equals(fechaHoraCreacion.toString())) {
                posicion = i;
                break;
            }
        }
        eventoList.remove(posicion);
    }

    private boolean eventoListContainsId(String eventId) {
        String id;
        for (Evento existingEvento : eventoList) {
            id = "evento"+existingEvento.getFechaHoraCreacion();
            if (id.equals(eventId)) {
                return true;
            }
        }
        return false;
    }
}