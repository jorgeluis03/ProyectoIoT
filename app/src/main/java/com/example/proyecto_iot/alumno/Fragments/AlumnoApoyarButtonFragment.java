package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.FragmentAlumnoApoyarButtonBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AlumnoApoyarButtonFragment extends Fragment {

    FragmentAlumnoApoyarButtonBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Evento evento;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlumnoApoyarButtonBinding.inflate(inflater, container, false);

        evento = ((AlumnoEventoActivity) getActivity()).getEvento();

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
                .document("evento"+evento.getFechaHoraCreacion().toString())
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
                .document("evento"+evento.getFechaHoraCreacion().toString())
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
        String region = AppConstants.REGION;
        String appID = AppConstants.APP_ID;

        AppSettings appSettings = new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(getContext(), appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {

                CometChat.joinGroup(evento.getChatID(), CometChatConstants.GROUP_TYPE_PUBLIC, null, new CometChat.CallbackListener<Group>() {
                    @Override
                    public void onSuccess(Group group) {
                        Log.d("msg-test", group.toString());

                        // TODO: RECARGAR ACTIVITY PARA ACTUALIZAR BOTONES
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.d("msg-test", "Group joining failed with exception: " + e.getMessage());
                    }
                });

            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "Initialization failed with exception: " + e.getMessage());
            }
        });
    }

    private void cargarBoton(){
        if (evento.getEstado().equals("inactivo")){
            binding.buttonEventoApoyar.setVisibility(View.GONE);
        }
    }
}