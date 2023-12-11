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
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Donacion;
import com.example.proyecto_iot.databinding.ActivityDgEstadisticasDetallesBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDonacionesAdapter;
import com.example.proyecto_iot.delegadoGeneral.dto.DonacionDto;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.android.gms.tasks.Task;
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
    List<DonacionDto>  listaDonaciones;
    ListaDonacionesAdapter adapter;

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
                Log.d("msg-test","ok");
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String codigoDonante = documentSnapshot.getId();//codigo donante



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