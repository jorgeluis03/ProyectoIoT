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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AlumnoEventosTodosFragment extends Fragment {

    private ArrayList<Evento> eventoList = new ArrayList<>();

    private FragmentAlumnoEventosTodosBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListaEventosAdapter adapter = new ListaEventosAdapter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoEventosTodosBinding.inflate(inflater, container, false);


        binding.rvEventos.setAdapter(adapter);
        binding.rvEventos.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setContext(getContext());
        adapter.setEventoList(eventoList);

        eventChangeListener();

        //eventos hardcodeados
        /*
        eventoList.add(new Evento("Evento de Semana de Ingeniería",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.",
                "Nombre de actividad del evento",
                "10/09/23",
                "10:00",
                new Lugar("Cancha minas", 0),false));

        eventoList.add(new Evento("Otro evento de Semana de Ingeniería",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.",
                "Nombre de actividad del evento",
                "13/09/23",
                "Polideportivo",
                new Lugar("Polideportivo",0), false));

         */

        return binding.getRoot();
    }

    private void eventChangeListener(){
        db.collection("eventos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.d("msg-test", "error: "+error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                eventoList.add(dc.getDocument().toObject(Evento.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}