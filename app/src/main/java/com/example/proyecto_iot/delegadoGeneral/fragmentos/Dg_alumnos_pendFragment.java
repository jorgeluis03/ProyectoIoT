package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosPendBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosPendiAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_pendFragment extends Fragment {
    FragmentDgAlumnosPendBinding binding;
    ListenerRegistration listenerRegistration;
    private List<Alumno> listaUserPendi;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDgAlumnosPendBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance(); //obtengo la isntancia

        //Escuchar en tiempo real los cambios
        listenerRegistration = db.collection("alumnos")
                .whereEqualTo("estado", "inactivo")
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.w("msg-test", "Listen failed.", error);
                        return;
                    }
                    //notificacion

                    
                    listaUserPendi = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                        Alumno alumno = documentSnapshot.toObject(Alumno.class);
                        listaUserPendi.add(alumno);
                    }

                    //Llamar al adapter
                    ListaUsuariosPendiAdapter adapter = new ListaUsuariosPendiAdapter();
                    adapter.setContext(getContext());
                    adapter.setListaUsuarios(listaUserPendi);

                    binding.recycleViewUserPendi.setAdapter(adapter);
                    binding.recycleViewUserPendi.setLayoutManager(new LinearLayoutManager(getContext()));


                });


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }


}