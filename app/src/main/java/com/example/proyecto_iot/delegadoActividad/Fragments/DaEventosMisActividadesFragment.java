package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.FragmentDaEventosMisActividadesBinding;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DaEventosMisActividadesFragment extends Fragment {
    private ArrayList<Evento> eventoList = new ArrayList<>();
    private ListaEventosAdapter adapter = new ListaEventosAdapter();
    ArrayList<Actividades> actividadList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();

    private FragmentDaEventosMisActividadesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDaEventosMisActividadesBinding.inflate(inflater, container, false);

        actividadList = obtenerActividadesDesdeMemoria();
        for (Actividades actividad: actividadList){
            if (actividad.getEstado().equals("abierto")){
                db.collection("actividades")
                        .document(actividad.getId())
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
                                    Log.d("msg-test", "AlumnoEventosMisActFragment: error en busqueda de eventos apoyados");
                                }
                            }
                        });
            }
        }
        adapter.setContext(getContext());
        adapter.setEventoList(eventoList);

        binding.rvEventosMisAct.setAdapter(adapter);
        binding.rvEventosMisAct.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    private ArrayList<Actividades> obtenerActividadesDesdeMemoria() {
        try (FileInputStream fileInputStream = getActivity().openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);
            return alumno.getActividadesId();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
                            if (evento.getEstado().equals("activo")){
                                eventoList.add(evento);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            Log.d("msg-test", "AlumnoEventosMisActFragment error buscando evento: "+eventoId);
                        }
                    }
                });
    }
}