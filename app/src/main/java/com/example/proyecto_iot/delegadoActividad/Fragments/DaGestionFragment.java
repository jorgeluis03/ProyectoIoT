package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDaGestionBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaActividadesCardAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DaGestionFragment extends Fragment {

    private FragmentDaGestionBinding binding;
    private ArrayList<Actividades> actividadList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ListaActividadesCardAdapter adapter = new ListaActividadesCardAdapter();
    ArrayList<Actividades> listaFinal = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDaGestionBinding.inflate(inflater, container, false);
        binding.progressBar9.setVisibility(View.VISIBLE);
        actividadList = obtenerActividadesDesdeMemoria();
        Log.d("msg-test", "Número de actividades encontradas en memoria: "+actividadList.size());
        List<Task<?>> tasks = new ArrayList<>();
        for (Actividades actividad: actividadList){
            tasks.add(buscarActividad(actividad.getId()));
        }
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(allTasks -> {
                    binding.progressBar9.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                });
        adapter.setContext(getContext());
        adapter.setActividadCardList(listaFinal);

        binding.rvActividades.setAdapter(adapter);
        binding.rvActividades.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    private Task<Void> buscarActividad(String id) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("actividades")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Actividades a = task.getResult().toObject(Actividades.class);
                            Log.d("msg-test", "actividad encontrada: "+a.getNombre());
                            if (a.getEstado().equals("abierto")){
                                listaFinal.add(a);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            Log.d("msg-test", "DaGestionFragment error buscando act: "+id);
                        }
                        if (!taskCompletionSource.getTask().isComplete()) {
                            taskCompletionSource.setResult(null);
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Espera a que la vista esté lista antes de acceder a ella
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // La vista está lista
                TextView textView = view.findViewById(R.id.textHeader2);
                textView.setText("Gestionar eventos");

                TextView textView2 = view.findViewById(R.id.textView24);
                textView2.setText("Escoge una actividad");

                // Elimina el listener para que no se vuelva a llamar
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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
}