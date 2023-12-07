package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoEventosApoyandoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlumnoEventosApoyandoFragment extends Fragment {

    private ArrayList<Evento> eventoApoyandoList = new ArrayList<>();

    private FragmentAlumnoEventosApoyandoBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListaEventosAdapter adapter = new ListaEventosAdapter();
    private String userUid = FirebaseAuth.getInstance().getUid();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoEventosApoyandoBinding.inflate(inflater, container, false);

        // obteniendo ids de eventos apoyados
        binding.progressBar7.setVisibility(View.VISIBLE);

        List<Task<?>> tasks = new ArrayList<>();
        db.collection("alumnos")
                .document(userUid)
                .collection("eventos")
                .addSnapshotListener((value, error) -> {
                    if (error != null){
                        Log.d("msg-test", "Listen failed in eventos apoyando", error);
                        return;
                    }
                    if (value != null){
                        eventoApoyandoList.clear();
                        for (QueryDocumentSnapshot doc: value){
                            tasks.add(buscarEventos(doc.getId()));
                        }
                        Tasks.whenAllComplete(tasks)
                            .addOnCompleteListener(allTasks -> {
                                binding.progressBar7.setVisibility(View.GONE);
                                if (eventoApoyandoList.isEmpty()){
                                    binding.textView32.setVisibility(View.VISIBLE);
                                }else {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    }
                });

        adapter.setContext(getContext());
        adapter.setEventoList(eventoApoyandoList);

        binding.rvEventosAp.setAdapter(adapter);
        binding.rvEventosAp.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    private Task<Void> buscarEventos(String eventoId) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        db.collection("eventos")
                .document(eventoId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null){
                        return;
                    }
                    if (snapshot != null && snapshot.exists()){
                        Evento evento = snapshot.toObject(Evento.class);
                        if (!eventoListContainsId("evento"+evento.getFechaHoraCreacion())){
                            Log.d("msg-test", "evento apoyado encontrado: " + evento.getTitulo());
                            eventoApoyandoList.add(evento);
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            removerDeLista(evento.getFechaHoraCreacion());
                            eventoApoyandoList.add(evento);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Log.d("msg-test", "AlumnoEventosApoyandoFragment: No se encontró el evento con ID: " + eventoId);
                    }
                    taskCompletionSource.setResult(null);
                });
        return taskCompletionSource.getTask();
    }

    private void removerDeLista(Date fechaHoraCreacion) {
        int posicion = -1;
        for (int i = 0; i < eventoApoyandoList.size(); i++) {
            if (eventoApoyandoList.get(i).getFechaHoraCreacion().toString().equals(fechaHoraCreacion.toString())) {
                posicion = i;
                break;
            }
        }
        eventoApoyandoList.remove(posicion);
    }

    private boolean eventoListContainsId(String eventId) {
        String id;
        for (Evento existingEvento : eventoApoyandoList) {
            id = "evento"+existingEvento.getFechaHoraCreacion();
            if (id.equals(eventId)) {
                return true;
            }
        }
        return false;
    }
}