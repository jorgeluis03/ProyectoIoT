package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosBaneadosBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaUsuariosBaneadosAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class Dg_alumnos_baneadosFragment extends Fragment {
    FragmentDgAlumnosBaneadosBinding binding;
    FirebaseFirestore db;
    ListenerRegistration listenerRegistration;
    private List<Alumno> listaUserBaneados;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDgAlumnosBaneadosBinding.inflate(inflater,container,false);

        db = FirebaseFirestore.getInstance(); //obtengo la isntancia

        //Escuchar en tiempo real los cambios
        listenerRegistration = db.collection("alumnos")
                .whereEqualTo("estado","baneado")
                .addSnapshotListener((snapshot, error) ->{
                    if (error != null) {
                        Log.w("msg-test", "Listen failed.", error);
                        return;
                    }
                    listaUserBaneados = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot: snapshot){
                        Alumno alumno = documentSnapshot.toObject(Alumno.class);
                        listaUserBaneados.add(alumno);
                    }

                    //Llamar al adapter
                    ListaUsuariosBaneadosAdapter adapter = new ListaUsuariosBaneadosAdapter();
                    adapter.setContext(getContext());
                    adapter.setLista(listaUserBaneados);

                    binding.recycleViewUserBaneados.setAdapter(adapter);
                    binding.recycleViewUserBaneados.setLayoutManager(new LinearLayoutManager(getContext()));


                });




        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }
}