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
import com.example.proyecto_iot.delegadoGeneral.dto.ApoyosActivdadDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Dg_estadisticasFragment extends Fragment {
    FragmentDgEstadisticasBinding binding;
    MaterialCardView cardView;
    TextView saldoDonacion, cantEstudiantes, cantEgresados;
    ListenerRegistration listenerRegistration;
    ListaActiEstadisiticasAdapter adapter;
    RecyclerView recyclerView;
    double suma;
    int coutEstu, countEgre;
    List<ApoyosActivdadDto> lista;
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

        cardView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DgEstadisticasDetallesActivity.class);
            startActivity(intent);
        });




        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        cargarSaldoDonacion();
        cargarCantidadUsuarios();
        mostrarActividades();
    }
    public void mostrarActividades(){

        Query queryBase = FirebaseUtilDg.getActividadesCollection();

        FirestoreRecyclerOptions<Actividades> options = new FirestoreRecyclerOptions.Builder<Actividades>()
                .setQuery(queryBase, Actividades.class).build();

        adapter = new ListaActiEstadisiticasAdapter(options,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }

    public void cargarSaldoDonacion() {
        suma = 0.0; // Reiniciar suma a 0
        FirebaseUtilDg.getDonaciones().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String codigoDonante = documentSnapshot.getId();

                    FirebaseUtilDg.getColeccionIdDonantes(codigoDonante)
                            .whereEqualTo("estado", "validado")
                            .get().addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task1.getResult()) {
                                        String monto = snapshot.getString("monto");
                                        suma += Double.parseDouble(monto);
                                    }
                                    // Truncar a dos decimales
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    String sumaFormateada = decimalFormat.format(suma);
                                    saldoDonacion.setText("S/" + sumaFormateada);
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