package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosPendBinding;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosRegistrBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosPendiAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_alumnos_pendFragment extends Fragment {
    FragmentDgAlumnosPendBinding binding;
    private List<Usuario> listaUserPendi = new ArrayList<>();
    private static boolean usuarioPendienteCargado = false;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgAlumnosPendBinding.inflate(inflater,container,false);

        db=FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("estado","inactivo")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        listaUserPendi = new ArrayList<>(); // Inicializa la lista

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario usuario = document.toObject(Usuario.class);

                            listaUserPendi.add(usuario);
                        }
                        
                        ListaUsuariosPendiAdapter adapter = new ListaUsuariosPendiAdapter();
                        adapter.setContext(getContext());
                        adapter.setListaUsuarios(listaUserPendi);
                        binding.recycleViewUserPendi.setAdapter(adapter);
                        binding.recycleViewUserPendi.setLayoutManager(new LinearLayoutManager(getContext()));

                        usuarioPendienteCargado = true; // Marca las actividades como cargadas
                    }
                });


        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    private void cargarListaUserPendi() {
        ListaUsuariosPendiAdapter adapter = new ListaUsuariosPendiAdapter();
        adapter.setContext(getContext());
        adapter.setListaUsuarios(listaUserPendi);

        binding.recycleViewUserPendi.setAdapter(adapter);
        binding.recycleViewUserPendi.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}