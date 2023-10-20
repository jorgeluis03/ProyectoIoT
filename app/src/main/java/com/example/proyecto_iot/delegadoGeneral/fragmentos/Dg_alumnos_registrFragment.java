package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosPendiAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_registrFragment extends Fragment {
    FragmentDgAlumnosRegistrBinding binding;
    private List<Usuario> listaUserRegi = new ArrayList<>();
    private static boolean usuariosRegiCargado = false;
    ListenerRegistration snapshotListener;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosRegistrBinding.inflate(inflater,container,false);

        db=FirebaseFirestore.getInstance();
        snapshotListener = db.collection("usuarios")
                .whereEqualTo("estado","activo")
                .addSnapshotListener((snapshot,error) ->{

                    if (error != null) {
                        Log.w("msg-test", "Listen failed.", error);
                        return;
                    }
                    listaUserRegi = new ArrayList<>(); // Inicializa la lista
                    for(QueryDocumentSnapshot document: snapshot){
                        Usuario usuario = document.toObject(Usuario.class);
                        Log.d("msg-test", "id: " + document.getId() + " | Nombre: " + usuario.getNombre() + " estado: " + usuario.getEstado());
                        listaUserRegi.add(usuario);
                    }
                    ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
                    adapter.setContext(getContext());
                    adapter.setListaUsuarios(listaUserRegi);
                    binding.recycleViewUserRegi.setAdapter(adapter);
                    binding.recycleViewUserRegi.setLayoutManager(new LinearLayoutManager(getContext()));

                    usuariosRegiCargado = true;
                });



        return binding.getRoot();
    }


    @Override
    public void onPause() {
        super.onPause();
        //snapshotListener.remove();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        snapshotListener.remove();
    }


}