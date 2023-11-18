package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.FragmentDaApoyosBinding;
import com.example.proyecto_iot.delegadoActividad.Entities.ApoyoDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDaApoyosBinding.inflate(inflater, container, false);
        evento = ((AlumnoEventoActivity) getActivity()).getEvento();
        Log.d("msg-test","DaApoyos: "+evento.getFechaHoraCreacion());
        cargarFragment();

        apoyos = new ArrayList<>();
        apoyos = cargarApoyos(evento);

        String apoyosStr;
        if (apoyos.size()==0){
            apoyosStr = "Sin apoyos";
        } else if (apoyos.size()==1) {
            apoyosStr = "1 apoyo";
        }else {
            apoyosStr = apoyos.size() + " apoyos";
        }
        binding.buttonApoyos.setText(apoyosStr);

        return inflater.inflate(R.layout.fragment_da_apoyos, container, false);
    }

    private void cargarFragment() {
        if (evento.getEstado().equals("inactivo")){
            binding.buttonApoyos.setVisibility(View.GONE);
        }
    }
    private ArrayList<ApoyoDto> cargarApoyos(Evento evento) {
        Log.d("msg-test", "evento"+evento.getFechaHoraCreacion().toString());
        db.collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .collection("apoyos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("msg-test", "busqueda apoyos ok: "+task.getResult().size());
                            for (QueryDocumentSnapshot document: task.getResult()){
                                apoyo = new ApoyoDto();
                                apoyo = buscarAlumno(document.getId());
                                apoyo.setCategoria(document.getString("categoria"));
                                apoyo.setEventoId(evento.getFechaHoraCreacion().toString());
                                apoyos.add(apoyo);
                            }
                        } else {
                            Log.d("msg-test", "AlumnoEventoActivity: error al buscar evento");
                        }
                    }
                });
        return apoyos;
    }
    private ApoyoDto buscarAlumno(String alumnoId){
        ApoyoDto apoyo = new ApoyoDto();
        db.collection("alumnos")
                .document(alumnoId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Alumno alumno = task.getResult().toObject(Alumno.class);
                            if (alumno.getEstado().equals("activo")){
                                Log.d("msg-test", "apoyo encontrado: "+alumno.getNombre());
                                apoyo.setAlumno(alumno);
                            }
                        }
                        else{
                            Log.d("msg-test", "ListaApoyos error buscando apoyo: "+alumnoId);
                        }
                    }
                });
        return apoyo;
    }
}