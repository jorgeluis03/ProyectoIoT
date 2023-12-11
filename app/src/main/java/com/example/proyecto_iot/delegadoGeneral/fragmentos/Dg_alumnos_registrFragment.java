package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosRegisAdpter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.List;

public class Dg_alumnos_registrFragment extends Fragment {
    FragmentDgAlumnosRegistrBinding binding;
    SearchView searchView;
    RecyclerView recycleViewUserRegi;
    Query query;
    ListaUsuariosRegisAdpter adapter,adapterBuscar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosRegistrBinding.inflate(inflater,container,false);

        //declaraciones
        searchView = binding.searchUserRegis;
        recycleViewUserRegi =binding.recycleViewUserRegi;

        cargarListaUsuariosRegis();

        searchMetod();

        return binding.getRoot();
    }
    public void cargarListaUsuariosRegis(){

        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","activo");
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if(task.getResult().getDocuments().size()>0){

                    FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                            .setQuery(query, Alumno.class).build();

                    adapter = new ListaUsuariosRegisAdpter(options,getContext());
                    recycleViewUserRegi.setAdapter(adapter);
                    recycleViewUserRegi.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.startListening();
                }else {
                    setVisible(true);
                }
            }
        });



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


        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","activo");
        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query.orderBy("nombre").startAt(s).endAt(s+"~"), Alumno.class).build();

        adapterBuscar = new ListaUsuariosRegisAdpter(options,getContext());
        recycleViewUserRegi.setAdapter(adapterBuscar);
        recycleViewUserRegi.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterBuscar.startListening();

    }

    public void setVisible(boolean noHaySolicitudes){
        if(noHaySolicitudes){
            binding.textNoHay.setVisibility(View.VISIBLE);
            recycleViewUserRegi.setVisibility(View.INVISIBLE);
        }else {
            binding.textNoHay.setVisibility(View.INVISIBLE);
            recycleViewUserRegi.setVisibility(View.VISIBLE);
        }
    }

}