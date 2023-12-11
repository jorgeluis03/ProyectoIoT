package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosPendBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosBaneadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosPendiAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosRegisAdpter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_pendFragment extends Fragment {
    FragmentDgAlumnosPendBinding binding;
    SearchView searchView;
    RecyclerView recycleViewUserPendi;
    Query query;
    ListaUsuariosPendiAdapter adapter,adapterBuscar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDgAlumnosPendBinding.inflate(inflater, container, false);

        //declaraciones
        searchView = binding.searchUserPendi;
        recycleViewUserPendi =binding.recycleViewUserPendi;

        cargarListaUsuariosPendi();

        searchMetod();
        return binding.getRoot();
    }

    public void cargarListaUsuariosPendi(){
        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","inactivo");

        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query, Alumno.class).build();

        adapter = new ListaUsuariosPendiAdapter(options,getContext());
        recycleViewUserPendi.setAdapter(adapter);
        recycleViewUserPendi.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.startListening();


    }
    public void searchMetod(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textSearch(newText);
                return false;
            }
        });
    }
    public void textSearch(String s){


        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","inactivo");
        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query.orderBy("nombre").startAt(s).endAt(s+"~"), Alumno.class).build();

        adapterBuscar = new ListaUsuariosPendiAdapter(options,getContext());
        recycleViewUserPendi.setAdapter(adapterBuscar);
        recycleViewUserPendi.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterBuscar.startListening();

    }




}