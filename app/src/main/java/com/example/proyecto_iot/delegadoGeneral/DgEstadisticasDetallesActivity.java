package com.example.proyecto_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.databinding.ActivityDgEstadisticasDetallesBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDonacionesAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDonacionesDtoAdapter;
import com.example.proyecto_iot.delegadoGeneral.dto.DonacionDto;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DgEstadisticasDetallesActivity extends AppCompatActivity {
    ActivityDgEstadisticasDetallesBinding binding;
    ImageButton btnBack;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ListenerRegistration listenerRegistration;
    List<DonacionDto> listaDonaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDgEstadisticasDetallesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recycleViewDetalleEstad;
        progressBar = binding.progressBarEstad;
        btnBack = binding.btnBack;

        btnBack.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerDonacionesSinValidar();
    }


    public void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    public void obtenerDonacionesSinValidar(){
        setInProgress(true);
        FirebaseUtilDg.getDonaciones().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String codigoDonante = documentSnapshot.getId();

                    Log.d("msg-don", documentSnapshot.getId());

                    listenerRegistration = FirebaseUtilDg.getColeccionIdDonantes(codigoDonante)
                            .whereEqualTo("estado","por validar")
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    Log.w("msg-don", "Listen failed.", error);
                                    return;
                                }
                                listaDonaciones = new ArrayList<>();

                                for (QueryDocumentSnapshot snapshot : value) {
                                    String idDocDonacion = snapshot.getId();
                                    FirebaseUtilDg.getDonante(codigoDonante)
                                            .get().addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()){



                                                    DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                                    String nombreDonante = document.getString("nombre")+ ' '+ document.getString("apellidos");
                                                    Donacion dona = snapshot.toObject(Donacion.class);
                                                    DonacionDto donacionDto = new DonacionDto(idDocDonacion,codigoDonante,dona.getFecha()+' '+ dona.getHora(),dona.getMonto(),nombreDonante,dona.getFotoQR());

                                                    listaDonaciones.add(donacionDto);

                                                    // aca va el adapter
                                                    ListaDonacionesAdapter adapter = new ListaDonacionesAdapter();
                                                    adapter.setLista(listaDonaciones);
                                                    adapter.setContext(this);

                                                    recyclerView.setAdapter(adapter);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                                                }

                                            });

                                }
                                if(listaDonaciones.isEmpty()){
                                   // setInProgress(false);
                                }

                            });
                }

            }
            setInProgress(false);
        });

    }

}