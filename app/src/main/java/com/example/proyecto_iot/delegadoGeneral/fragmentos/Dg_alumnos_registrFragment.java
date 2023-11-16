package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_registrFragment extends Fragment implements SearchView.OnQueryTextListener {
    FragmentDgAlumnosRegistrBinding binding;
    private List<Alumno> listaUserRegi;
    ListenerRegistration snapshotListener;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosRegistrBinding.inflate(inflater,container,false);

        db=FirebaseFirestore.getInstance();
        snapshotListener = db.collection("alumnos")
                .whereEqualTo("estado","activo")
                .addSnapshotListener((snapshot,error) ->{

                    if (error != null) {
                        Log.w("msg-test", "Listen failed.", error);
                        return;
                    }
                    listaUserRegi = new ArrayList<>(); // Inicializa la lista
                    for(QueryDocumentSnapshot document: snapshot){
                        Alumno alumno = document.toObject(Alumno.class);
                        Log.d("msg-test", "id: " + document.getId() + " | Nombre: " + alumno.getNombre() + " estado: " + alumno.getEstado());
                        listaUserRegi.add(alumno);
                    }
                    ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
                    adapter.setContext(getContext());
                    adapter.setListaUsuarios(listaUserRegi);
                    binding.recycleViewUserRegi.setAdapter(adapter);
                    binding.recycleViewUserRegi.setLayoutManager(new LinearLayoutManager(getContext()));

                });





        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(snapshotListener!=null){
            snapshotListener.remove();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String s) { // pueda ir buscando en tiempo real
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}