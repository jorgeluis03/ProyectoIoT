package com.example.proyecto_iot.alumno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.ActivityAlumnoBuscarEventoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AlumnoBuscarEventoActivity extends AppCompatActivity {
    private ActivityAlumnoBuscarEventoBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Evento evento = document.toObject(Evento.class);
                                            Log.d("msg-test", "acitv: " + evento.getActividad());
                                        }
                                    } else {
                                        Log.d("msg-test", "sin resultados de busqueda");
                                    }
                                }
                            });
                    // buscar coincidencias en firestore
                    // si hay coincidencias:
                    // mostrar eventos encontrados
                    // si no hay coincidencias:
                    // mostrar mensaje
                    return true;
                }
                return false;
            }
        });
    }
}