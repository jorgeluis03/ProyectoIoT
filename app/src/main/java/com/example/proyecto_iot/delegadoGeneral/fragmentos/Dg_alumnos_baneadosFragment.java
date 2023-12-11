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
import com.example.proyecto_iot.databinding.FragmentDgAlumnosBaneadosBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosBaneadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosRegisAdpter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class Dg_alumnos_baneadosFragment extends Fragment {
    FragmentDgAlumnosBaneadosBinding binding;
    SearchView searchView;
    RecyclerView recycleViewUserBan;
    Query query;
    ListaUsuariosBaneadosAdapter adapter,adapterBuscar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDgAlumnosBaneadosBinding.inflate(inflater,container,false);

        //declaraciones
        searchView = binding.searchUserBan;
        recycleViewUserBan =binding.recycleViewUserBaneados;

        cargarListaUsuariosBan();

        searchMetod();

        return binding.getRoot();
    }

    public void cargarListaUsuariosBan(){
        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","baneado");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult().getDocuments().size()>0){
                    FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                            .setQuery(query, Alumno.class).build();

                    adapter = new ListaUsuariosBaneadosAdapter(options,getContext());
                    recycleViewUserBan.setAdapter(adapter);
                    recycleViewUserBan.setLayoutManager(new LinearLayoutManager(getContext()));
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


        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","baneado");
        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query.orderBy("nombre").startAt(s).endAt(s+"~"), Alumno.class).build();

        adapterBuscar = new ListaUsuariosBaneadosAdapter(options,getContext());
        recycleViewUserBan.setAdapter(adapterBuscar);
        recycleViewUserBan.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterBuscar.startListening();

    }
    public void setVisible(boolean noHaySolicitudes){
        if(noHaySolicitudes){
            binding.textNoHay.setVisibility(View.VISIBLE);
            recycleViewUserBan.setVisibility(View.INVISIBLE);
        }else {
            binding.textNoHay.setVisibility(View.INVISIBLE);
            recycleViewUserBan.setVisibility(View.VISIBLE);
        }
    }
}