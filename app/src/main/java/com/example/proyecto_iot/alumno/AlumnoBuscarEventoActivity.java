package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.ActivityAlumnoBuscarEventoBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

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
    private ListaEventosAdapter adapter;
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

        setUpRecyclerView();

        binding.inputBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                buscarEvento(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscarEvento(s);
                return false;
            }
        });

        binding.inputBuscarEvento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) { // busqueda hecha
                    String busqueda = binding.inputBuscarEvento.getText().toString();

                    // guardando busqueda en memoria (tipo fb :v)
                    guardarBusqueda();

                    buscarEventosFirestore(busqueda);

                    return true;
                }
                return false;
            }
        });
    }

    private void setUpRecyclerView(){

        adapter.setContext(AlumnoBuscarEventoActivity.this);
        adapter.setEventoList(eventoList);

    }

    private void buscarEvento(String busqueda){
        Query query = db.collection("eventos").where(Filter.or(
                Filter.equalTo("titulo", busqueda),
                Filter.equalTo("actividad", busqueda)
        ));
        FirestoreRecyclerOptions<Evento> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(query.startAt(busqueda).endAt(busqueda+"~"), Evento.class)
                .build();

        //adapter = new ListaEventosAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());

        binding.rvEventosBuscados.setAdapter(adapter);
        binding.rvEventosBuscados.setLayoutManager(new LinearLayoutManager(AlumnoBuscarEventoActivity.this));
    }

    private void buscarEventosFirestore(String busqueda) {
        Query query = db.collection("eventos").where(Filter.or(
                Filter.equalTo("titulo", busqueda),
                Filter.equalTo("actividad", busqueda)
        ));

        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("msg-test", "coincidencias: " + task.getResult().size());

                            if (task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Evento evento = document.toObject(Evento.class);
                                    eventoList.add(evento);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(AlumnoBuscarEventoActivity.this, "No se encontraron resultados :c", Toast.LENGTH_SHORT).show();
                                clearItems();
                            }

                        } else {
                            Log.d("msg-test", "error en busqueda: " + task.getException().getMessage());
                        }

                        // ocultar teclado
                        InputMethodManager imm = (InputMethodManager) AlumnoBuscarEventoActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.inputBuscarEvento.getWindowToken(), 0);

                    }
                });
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