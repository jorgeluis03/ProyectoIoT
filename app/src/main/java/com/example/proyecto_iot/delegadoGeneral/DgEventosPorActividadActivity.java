package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.ActivityDgEventosPorActividadBinding;
import com.example.proyecto_iot.databinding.ActivityEventosActividadDgBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaEventosAdapter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DgEventosPorActividadActivity extends AppCompatActivity {
    ActivityDgEventosPorActividadBinding binding;
    String idActividad;
    ListaEventosAdapter adapter;
    RecyclerView recyclerView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDgEventosPorActividadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recycleViewEventos;
        btnBack = binding.btnBack;

        idActividad = getIntent().getStringExtra("idActividad");


        Query query = FirebaseUtilDg.getColeccionEventos().whereEqualTo("actividadId",idActividad);

        FirestoreRecyclerOptions<Evento>options = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(query, Evento.class).build();

        adapter = new ListaEventosAdapter(options,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        btnBack.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}