package com.example.proyecto_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityDgAsignarDelegadoBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDelegadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DgAsignarDelegadoActivity extends AppCompatActivity implements ListaDelegadosAdapter.OnAlumnoSelectedListener{
    ActivityDgAsignarDelegadoBinding binding;
    ListaDelegadosAdapter adapter;
    RecyclerView recyclerViewDelegados;
    FloatingActionButton fbSelec;
    ImageButton btnBack;
    ProgressBar progressBar;
    Alumno delegado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDgAsignarDelegadoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //declaraciones
        recyclerViewDelegados = binding.recycleviewDelegados;
        btnBack = binding.btnBack;
        fbSelec = binding.fbCheckDelegado;
        progressBar = binding.progressBar;

        enProgreso(true);
        cargarListaDelegado();
        enProgreso(false);

        adapter.setOnAlumnoSelectedListener(this);

        fbSelec.setOnClickListener(view -> {
            if(delegado!=null){
             Log.d("msg-test", "Alumno seleccionado en la actividad: " + delegado.getNombre() + ' ' + delegado.getApellidos());
                Intent intent = new Intent();
                intent.putExtra("delegado",delegado);
                setResult(RESULT_OK,intent);
                finish();

            }else {
                Log.d("msg-test","no llego: ");
            }
        });

        btnBack.setOnClickListener(view -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void cargarListaDelegado(){

        Query query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","activo");

        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query, Alumno.class).build();

        adapter = new ListaDelegadosAdapter(options,this);
        recyclerViewDelegados.setAdapter(adapter);
        recyclerViewDelegados.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();


    }

    public void enProgreso(boolean enProgreso){
        if(enProgreso){
            progressBar.setVisibility(View.VISIBLE);
            recyclerViewDelegados.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            recyclerViewDelegados.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onAlumnoSelected(Alumno alumno) {
        // Aquí puedes trabajar con la lista de alumnos seleccionados en tu actividad
        delegado = alumno;
    }
}