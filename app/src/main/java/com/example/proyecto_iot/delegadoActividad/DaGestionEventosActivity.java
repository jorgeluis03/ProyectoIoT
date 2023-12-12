package com.example.proyecto_iot.delegadoActividad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.ActivityDaGestionEventosBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaEventosActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.inicioApp.ConfirmNewPasswActivity;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        binding.progressBar10.setVisibility(View.VISIBLE);
        setContentView(binding.getRoot());
        intent = getIntent();
        Actividades a = (Actividades) intent.getSerializableExtra("actividadCard");
        binding.textSubtitleGestion.setText(a.getNombre());
        binding.buttonEventoABack.setOnClickListener(view -> finish());

        List<Task<?>> tasks = new ArrayList<>();
        db.collection("actividades")
                .document(a.getId())
                .collection("eventos")
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        eventoList.clear();
                        for (QueryDocumentSnapshot document: value){
                            if (document.toObject(Evento.class).getEstado().equals("activo")) {
                                tasks.add(buscarEventos(document.getId()));
                            }
                        }
                        Tasks.whenAllComplete(tasks)
                                .addOnCompleteListener(allTasks -> {
                                    binding.progressBar10.setVisibility(View.GONE);
                                    if (eventoList.isEmpty()){
                                        binding.imageView13.setVisibility(View.VISIBLE);
                                        binding.textView34.setVisibility(View.VISIBLE);
                                        binding.nameActividad.setVisibility(View.VISIBLE);
                                        binding.nameActividad.setText(a.getNombre());
                                    }else if (eventoList.size()==0){
                                        eventoList.clear();
                                        adapter.notifyDataSetChanged();
                                        binding.imageView13.setVisibility(View.VISIBLE);
                                        binding.textView34.setVisibility(View.VISIBLE);
                                        binding.nameActividad.setVisibility(View.VISIBLE);
                                        binding.nameActividad.setText(a.getNombre());
                                    }else {
                                        adapter.notifyDataSetChanged();
                                        binding.imageView13.setVisibility(View.GONE);
                                        binding.textView34.setVisibility(View.GONE);
                                        binding.nameActividad.setVisibility(View.GONE);
                                    }
                                });
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
    private Task<Void> buscarEventos(String eventoId){
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("eventos")
                .document(eventoId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.d("msg-test", "AlumnoEventosApoyandoFragment error escuchando cambios en evento: " + eventoId, e);
                        taskCompletionSource.setException(e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Evento evento = snapshot.toObject(Evento.class);
                        if (evento.getEstado().equals("activo")){
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
                        }
                        else {
                            removerDeLista(evento.getFechaHoraCreacion());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("msg-test", "DaGestionEventosActivity: No se encontró el evento con ID: " + eventoId);
                    }
                    if (!taskCompletionSource.getTask().isComplete()) {
                        taskCompletionSource.setResult(null);
                    }
                });
        return taskCompletionSource.getTask();
    }

    private void removerDeLista(Date fechaHoraCreacion) {
        int posicion = -1;
        for (int i = 0; i < eventoList.size(); i++) {
            if (eventoList.get(i).getFechaHoraCreacion().toString().equals(fechaHoraCreacion.toString())) {
                posicion = i;
                break;
            }
        }
        if (posicion != -1){
            eventoList.remove(posicion);
        }
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