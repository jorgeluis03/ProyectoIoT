package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.databinding.FragmentDgEstadisticasBinding;
import com.example.proyecto_iot.delegadoGeneral.DgEstadisticasDetallesActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Dg_estadisticasFragment extends Fragment {
    FragmentDgEstadisticasBinding binding;
    MaterialCardView cardView;
    TextView saldoDonacion, cantEstudiantes, cantEgresados;
    ListenerRegistration listenerRegistration;
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

        cargarSaldoDonacion();
        cargarCantidadUsuarios();

        cardView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DgEstadisticasDetallesActivity.class);
            startActivity(intent);
        });




        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

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

                    listenerRegistration = FirebaseUtilDg.getColeccionIdDonantes(codigoDonante)
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