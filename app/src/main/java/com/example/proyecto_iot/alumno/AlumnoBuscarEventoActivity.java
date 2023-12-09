package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosBusquedaAdapter;
import com.example.proyecto_iot.databinding.ActivityAlumnoBuscarEventoBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AlumnoBuscarEventoActivity extends AppCompatActivity {
    private ActivityAlumnoBuscarEventoBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ArrayList<Evento> eventoList = new ArrayList<>();
    private ListaEventosBusquedaAdapter adapter;
    private ArrayList<String> busquedas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoBuscarEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cargarBusquedas();

        binding.buttonBack.setOnClickListener(view -> {
            finish();
        });

        binding.inputBuscarEvento.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarEventosFirestore(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarEventosFirestore(newText);
                return false;
            }
        });
    }

    private void buscarEventosFirestore(String busqueda){
        Query query = db.collection("eventos").where(Filter.or(
                Filter.equalTo("titulo", busqueda),
                Filter.equalTo("actividad", busqueda)
        ));
        FirestoreRecyclerOptions<Evento> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(query, Evento.class)
                .build();

        adapter = new ListaEventosBusquedaAdapter(firestoreRecyclerOptions, this);
        adapter.startListening();
        binding.rvEventosBuscados.setAdapter(adapter);
        binding.rvEventosBuscados.setLayoutManager(new LinearLayoutManager(AlumnoBuscarEventoActivity.this));
    }

    private void guardarBusqueda() {
        /*
        try (FileOutputStream fileOutputStream = openFileOutput("userSearch", Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    private void cargarBusquedas() {
        /*
        try (FileInputStream fileInputStream = openFileInput("userSearch");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            busquedas = gson.fromJson(jsonData, ArrayList<String>);

            binding.textNombre.setText(alumno.getNombre() + " " + alumno.getApellidos());
            binding.textRol.setText(alumno.getRol());

            cargarFoto(alumno);

        } catch (IOException e) {
            e.printStackTrace();
        }

         */
    }

    private void clearItems() {
        int size = eventoList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                eventoList.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }
    }
}