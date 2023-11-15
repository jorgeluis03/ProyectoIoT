package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.ActivityAlumnoBuscarEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AlumnoBuscarEventoActivity extends AppCompatActivity {
    private ActivityAlumnoBuscarEventoBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Evento> eventoList = new ArrayList<>();
    private ListaEventosAdapter adapter = new ListaEventosAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoBuscarEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(view -> {
            finish();
        });

        binding.inputBuscarEvento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String busqueda = binding.inputBuscarEvento.getText().toString();

                    buscarEventosFirestore(busqueda);

                    adapter.setContext(AlumnoBuscarEventoActivity.this);
                    adapter.setEventoList(eventoList);

                    binding.rvEventosBuscados.setAdapter(adapter);
                    binding.rvEventosBuscados.setLayoutManager(new LinearLayoutManager(AlumnoBuscarEventoActivity.this));

                    return true;
                }
                return false;
            }
        });
    }

    private void buscarEventosFirestore(String busqueda){
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

                            if (task.getResult().size() > 0){
                                for (DocumentSnapshot document : task.getResult()) {
                                    Evento evento = document.toObject(Evento.class);
                                    eventoList.add(evento);
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else{Toast.makeText(AlumnoBuscarEventoActivity.this, "No se encontraron resultados :c", Toast.LENGTH_SHORT).show();
                                clearItems();
                            }

                        } else {
                            Log.d("msg-test", "error en busqueda: "+task.getException().getMessage());
                        }

                        // ocultar teclado
                        InputMethodManager imm = (InputMethodManager) AlumnoBuscarEventoActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.inputBuscarEvento.getWindowToken(), 0);

                    }
                });
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