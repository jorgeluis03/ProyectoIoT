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
import com.example.proyecto_iot.delegadoGeneral.dto.DonacionDto;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
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
    List<DonacionDto>  listaDonaciones = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDgEstadisticasDetallesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recycleViewDetalleEstad;
        progressBar = binding.progressBarEstad;
        btnBack = binding.btnBack;


        setInProgress(true);
        obtenerDonacionesSinValidar();



        btnBack.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    public void obtenerDonacionesSinValidar(){

        FirebaseUtilDg.getDonaciones().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String idDocument = documentSnapshot.getId();
                    Log.d("msg-don", documentSnapshot.getId());

                    listenerRegistration = FirebaseUtilDg.getColeccionIdDonantes(idDocument)
                            .whereEqualTo("estado","por validar")
                            .addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    Log.w("msg-don", "Listen failed.", error);
                                    return;
                                }

                                for (QueryDocumentSnapshot snapshot : value) {

                                    FirebaseUtilDg.getDonante(idDocument)
                                            .get().addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()){



                                                    DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                                    String nombreDonante = document.getString("nombre")+ ' '+ document.getString("apellidos");
                                                    Donacion dona = snapshot.toObject(Donacion.class);
                                                    DonacionDto donacionDto = new DonacionDto(dona.getFecha()+' '+ dona.getHora(),dona.getMonto(),nombreDonante);

                                                    listaDonaciones.add(donacionDto);
                                                    // aca va el adapter
                                                    ListaDonacionesAdapter adapter = new ListaDonacionesAdapter();
                                                    adapter.setLista(listaDonaciones);
                                                    adapter.setContext(this);

                                                    recyclerView.setAdapter(adapter);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                                                    setInProgress(false);
                                                }

                                            });


                                }



                            });


                }



            }


        });

    }
    public void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }
}