package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.FragmentDaEventosMisActividadesBinding;
import com.example.proyecto_iot.delegadoActividad.DaEditEventoActivity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
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
import java.util.Date;
import java.util.List;

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
        binding.progressBar8.setVisibility(View.VISIBLE);
        actividadList = obtenerActividadesDesdeMemoria();
        List<Task<?>> tasks = new ArrayList<>();
        for (Actividades actividad: actividadList){
            if (actividad.getEstado().equals("abierto")){
                db.collection("actividades")
                        .document(actividad.getId())
                        .collection("eventos")
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.d("msg-test", "Listen failed in eventos todos", error);
                                return;
                            }
                            if (value != null) {
                                eventoList.clear();
                                for (QueryDocumentSnapshot doc : value) {
                                    tasks.add(buscarEventos(doc.getId()));
                                }
                            }
                        });
            }
        }
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(allTasks -> {
                    binding.progressBar8.setVisibility(View.GONE);
                    if (eventoList.isEmpty()){
                        binding.textView33.setVisibility(View.VISIBLE);
                    }else {
                        adapter.notifyDataSetChanged();
                        binding.textView33.setVisibility(View.GONE);
                    }
                });
        binding.progressBar8.setVisibility(View.GONE);
        adapter.setContext(getContext());
        adapter.setEventoList(eventoList);

        binding.rvEventosMisAct.setAdapter(adapter);
        binding.rvEventosMisAct.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DaEditEventoActivity.class);
            intent.putExtra("actividades", actividadList);
            startActivity(intent);
        });

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