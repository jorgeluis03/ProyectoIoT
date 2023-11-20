package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoChatActivity;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.FragmentDaApoyosBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaApoyosAdapter;
import com.example.proyecto_iot.delegadoActividad.Entities.ApoyoDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DaApoyosFragment extends Fragment {
    FragmentDaApoyosBinding binding;
    private Evento evento;
    private ApoyoDto apoyo;
    private ArrayList<ApoyoDto> apoyos = new ArrayList<>();
    private ListaApoyosAdapter adapter = new ListaApoyosAdapter();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDaApoyosBinding.inflate(inflater, container, false);
        //binding.buttonApoyos.setText("hola");
        evento = ((AlumnoEventoActivity) getActivity()).getEvento();
        if (evento.getEstado().equals("inactivo")){
            binding.buttonApoyos.setEnabled(false);
        }
        apoyos = new ArrayList<>();
        cargarApoyos(evento);
        binding.buttonChatDelegado.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), AlumnoChatActivity.class);
            intent.putExtra("evento", evento);
            startActivity(intent);
        });
        binding.buttonApoyos.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_da_apoyos, (LinearLayout) view.findViewById(R.id.dialogListApoyos));

            adapter.setContext(getContext());
            adapter.setApoyos(apoyos);

            RecyclerView recyclerView = bottomSheetView.findViewById(R.id.rv_apoyos_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        return binding.getRoot();
    }
    private void cargarApoyos(Evento evento) {
        Log.d("msg-test", "evento"+evento.getFechaHoraCreacion().toString());
        db.collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .collection("apoyos")
                .addSnapshotListener((value, error) -> {
                    if (value!=null){
                        apoyos = new ArrayList<>();
                        ArrayList<Task<Void>> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot document: value){
                            apoyo = new ApoyoDto();
                            Task<Void> apoyoTask = db.collection("alumnos")
                                    .document(document.getId())
                                    .get()
                                    .continueWith(task1 -> {
                                        if (task1.isSuccessful()){
                                            Alumno alumno = task1.getResult().toObject(Alumno.class);
                                            if (alumno.getEstado().equals("activo")){
                                                Log.d("msg-test", "apoyo encontrado: "+alumno.getNombre());
                                                apoyo.setAlumno(alumno);
                                                Log.d("msg-test", "seteado alumno "+apoyo.getAlumno().getNombre());
                                                apoyo.setCategoria(document.getString("categoria"));
                                                apoyo.setEventoId("evento"+evento.getFechaHoraCreacion().toString());
                                                apoyos.add(apoyo);
                                                Log.d("msg-test", "tamaño: " + apoyos.size());
                                            }
                                        }
                                        else{
                                            Log.d("msg-test", "ListaApoyos error buscando apoyo: "+document.getId());
                                        }
                                        return null;
                                    });
                            tasks.add(apoyoTask);
                        }
                        Tasks.whenAllComplete(tasks)
                                .addOnCompleteListener(task1 -> {
                                    Log.d("msg-test", "tamaño final: "+apoyos.size());
                                    String apoyosStr;
                                    if (apoyos!=null){
                                        if (apoyos.size()==0){
                                            apoyosStr = "Sin apoyos";
                                        } else if (apoyos.size()==1) {
                                            apoyosStr = "1 apoyo";
                                        }else {
                                            apoyosStr = apoyos.size() + " apoyos";
                                        }
                                    }else {
                                        apoyosStr = "Sin apoyos";
                                    }
                                    binding.buttonApoyos.setText(apoyosStr);
                                });
                    } if (error != null){
                        Log.d("msg-test", "AlumnoEventoActivity: error al buscar evento");
                    }
                });
    }
}