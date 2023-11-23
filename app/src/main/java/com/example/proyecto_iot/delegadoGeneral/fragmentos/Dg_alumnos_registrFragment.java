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
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosAdapter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_registrFragment extends Fragment {
    FragmentDgAlumnosRegistrBinding binding;
    SearchView searchView;
    private List<Alumno> listaUserRegi;
    ListenerRegistration snapshotListener;
    RecyclerView recycleViewUserRegi;
    FirebaseFirestore db;
    Query query;
    ListaUsuariosAdapter adapterBuscar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosRegistrBinding.inflate(inflater,container,false);

        //declaraciones
        searchView = binding.searchUserRegis;
        recycleViewUserRegi =binding.recycleViewUserRegi;

        snapshotListener = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","activo")
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
                    recycleViewUserRegi.setAdapter(adapter);
                    recycleViewUserRegi.setLayoutManager(new LinearLayoutManager(getContext()));

                });



        //search
        //searchMetod();
        return binding.getRoot();
    }

    public void searchMetod(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //textSearch(newText);
                return false;
            }
        });
    }
/*
    public void textSearch(String s){
        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","activo");
        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query.orderBy("nombre").startAt(s).endAt(s+"~"), Alumno.class).build();

        adapterBuscar = new ListaBuscarDelegadoAdapter(options);
        adapterBuscar.setOnAlumnoBuscarSelectedListener(this);
        adapterBuscar.startListening();
        recyclerViewDelegados.setAdapter(adapterBuscar);
    }
*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(snapshotListener!=null){
            snapshotListener.remove();
        }
    }

}