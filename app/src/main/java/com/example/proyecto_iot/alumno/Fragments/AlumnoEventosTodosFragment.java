package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoEventosTodosBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

public class AlumnoEventosTodosFragment extends Fragment {

    private ArrayList<Evento> eventoList = new ArrayList<>();

    private FragmentAlumnoEventosTodosBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListaEventosAdapter adapter = new ListaEventosAdapter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoEventosTodosBinding.inflate(inflater, container, false);

        db.collection("eventos")
                .orderBy("fechaHoraCreacion", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null){
                        Log.d("msg-test", "Listen failed in eventos todos", error);
                        return;
                    }
                    if (value != null){
                        eventoList.clear();
                        for (QueryDocumentSnapshot doc: value){
                            buscarEventos(doc.getId());
                        }
                    }
                });

        adapter.setContext(getContext());
        adapter.setEventoList(eventoList);

        binding.rvEventos.setAdapter(adapter);
        binding.rvEventos.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    private void buscarEventos(String eventoId) {

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
}