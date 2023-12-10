package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.FragmentAlumnoApoyarButtonBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AlumnoApoyarButtonFragment extends Fragment {

    FragmentAlumnoApoyarButtonBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Evento evento;
    private String eventoID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlumnoApoyarButtonBinding.inflate(inflater, container, false);

        evento = ((AlumnoEventoActivity) getActivity()).getEvento();
        eventoID = "evento"+evento.getFechaHoraCreacion().toString();

        cargarBoton();

        binding.buttonEventoApoyar.setOnClickListener(view -> {
            guardarAlumnoEnEvento();
        });

        return binding.getRoot();
    }

    private void guardarAlumnoEnEvento(){
        // agregar alumno a lista de apoyos del evento
        HashMap<String, String> apoyo = new HashMap<>();
        apoyo.put("alumnoID", userUid);
        apoyo.put("categoria", "barra");

        db.collection("eventos")
                .document(eventoID)
                .collection("apoyos")
                .document(userUid)
                .set(apoyo)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "alumno apoyando");
                    guardarEventoEnAlumno();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void guardarEventoEnAlumno(){
        HashMap<String, String> eventoEnAlumno = new HashMap<>();
        eventoEnAlumno.put("eventoID", "evento"+evento.getFechaHoraCreacion().toString());
        eventoEnAlumno.put("notificaciones", "no");

        db.collection("alumnos")
                .document(userUid)
                .collection("eventos")
                .document(eventoID)
                .set(eventoEnAlumno)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "evento agregado a alumno");
                    guardarAlumnoEnChat();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void guardarAlumnoEnChat(){
        String chatID = eventoID;

        DocumentReference chatReference = db.collection("chats").document(chatID);
        chatReference.update("userIDs", FieldValue.arrayUnion(userUid));
    }

    private void cargarBoton(){
        if (evento.getEstado().equals("inactivo")){
            binding.buttonEventoApoyar.setVisibility(View.GONE);
        }
    }
}