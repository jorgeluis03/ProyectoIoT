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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;

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


        db.collection("alumnos")
                .document(userUid)
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

        adapter.setContext(getContext());
        adapter.setEventoList(eventoApoyandoList);

        binding.rvEventosAp.setAdapter(adapter);
        binding.rvEventosAp.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
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
                            eventoApoyandoList.add(evento);
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Log.d("msg-test", "AlumnoEventosApoyandoFragment error buscando evento: "+eventoId);
                        }
                    }
                });
    }
}