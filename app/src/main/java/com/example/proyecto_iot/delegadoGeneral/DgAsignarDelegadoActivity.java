package com.example.proyecto_iot.delegadoGeneral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityDgAsignarDelegadoBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaDelegadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class DgAsignarDelegadoActivity extends AppCompatActivity implements ListaDelegadosAdapter.OnAlumnoSelectedListener{
    ActivityDgAsignarDelegadoBinding binding;
    ListaDelegadosAdapter adapter,adapterBuscar;
    RecyclerView recyclerViewDelegados;
    FloatingActionButton fbSelec;
    ImageButton btnBack;
    ProgressBar progressBar;
    Alumno delegado;
    SearchView searchView;
    Query query;
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
        searchView = binding.searchDeleasig;

        enProgreso(true);
        cargarListaDelegado();
        enProgreso(false);





        fbSelec.setOnClickListener(view -> {
            if(delegado!=null){
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


        searchMetod();

    }
    public void searchMetod(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textSearch(newText);
                return false;
            }
        });
    }
    public void textSearch(String s){
        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query.orderBy("nombre").startAt(s).endAt(s+"~"), Alumno.class).build();

        adapterBuscar = new ListaDelegadosAdapter(options, this);
        adapterBuscar.setOnAlumnoSelectedListener(this);
        adapterBuscar.startListening();
        recyclerViewDelegados.setAdapter(adapterBuscar);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void cargarListaDelegado(){

        query = FirebaseUtilDg.getCollAlumnos().whereEqualTo("estado","activo");

        FirestoreRecyclerOptions<Alumno> options = new FirestoreRecyclerOptions.Builder<Alumno>()
                .setQuery(query, Alumno.class).build();

        adapter = new ListaDelegadosAdapter(options,this);
        recyclerViewDelegados.setAdapter(adapter);
        adapter.setOnAlumnoSelectedListener(this);
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