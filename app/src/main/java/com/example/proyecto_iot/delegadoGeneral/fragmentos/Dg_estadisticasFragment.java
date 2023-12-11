package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.databinding.FragmentDgEstadisticasBinding;
import com.example.proyecto_iot.delegadoGeneral.DgEstadisticasDetallesActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaActiEstadisiticasAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Dg_estadisticasFragment extends Fragment {
    FragmentDgEstadisticasBinding binding;
    MaterialCardView cardView;
    TextView saldoDonacion, cantEstudiantes, cantEgresados;
    ListenerRegistration listenerRegistration;
    ListaActiEstadisiticasAdapter adapter;
    RecyclerView recyclerView;
    double suma;
    int coutEstu, countEgre;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgEstadisticasBinding.inflate(inflater,container,false);
        // Cambiar el contenido del Toolbar
        ((Dg_Activity) requireActivity()).setToolbarContent("Estadísticas");

        saldoDonacion = binding.montoDonacion;
        cardView = binding.cardDonaciones;
        cantEstudiantes = binding.cantEstudiantes;
        cantEgresados = binding.cantEgresados;
        recyclerView= binding.rvActividades;

        cargarCantidadUsuarios();
        cargarSaldoDonacion();

        cardView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DgEstadisticasDetallesActivity.class);
            startActivity(intent);
        });




        return binding.getRoot();
    }
    public void mostrarActividades(){
        Query query = FirebaseUtilDg.getActividadesCollection();

        FirestoreRecyclerOptions<Actividades> options = new FirestoreRecyclerOptions.Builder<Actividades>()
                .setQuery(query, Actividades.class).build();

        adapter = new ListaActiEstadisiticasAdapter(options,getContext());
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        //cargarSaldoDonacion();
        //cargarCantidadUsuarios();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }

    public void cargarSaldoDonacion(){
        FirebaseUtilDg.getDonaciones().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String codigoDonante = documentSnapshot.getId();

                    FirebaseUtilDg.getColeccionIdDonantes(codigoDonante)
                        .whereEqualTo("estado","validado")
                        .addSnapshotListener((value, error)->{
                            if (error != null) {
                                Log.w("msg-don", "Listen failed.", error);
                                return;
                            }
                            suma =0.0;
                            for (QueryDocumentSnapshot snapshot : value) {
                                String monto = snapshot.getString("monto");
                                suma += Double.parseDouble(monto);
                                saldoDonacion.setText("S/"+String.valueOf(suma));

                            }


                        });
                }
            }
        });

    }

    public void cargarCantidadUsuarios(){


        FirebaseUtilDg.getCollAlumnos().whereEqualTo("tipo","Estudiante")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("msg-don", "Listen failed.", error);
                        return;
                    }
                    coutEstu = value.getDocuments().size();
                    cantEstudiantes.setText(String.valueOf(coutEstu));

                });
        FirebaseUtilDg.getCollAlumnos().whereEqualTo("tipo","Egresado")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("msg-don", "Listen failed.", error);
                        return;
                    }

                    countEgre = value.getDocuments().size();
                    cantEgresados.setText(String.valueOf(countEgre));

                });


    }


}