package com.example.proyecto_iot.alumno.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoChatActivity;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.FragmentAlumnoApoyandoButtonBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlumnoApoyandoButtonFragment extends Fragment {

    FragmentAlumnoApoyandoButtonBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Evento evento;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoApoyandoButtonBinding.inflate(inflater, container, false);

        binding.buttonEventoApoyando.setOnClickListener(view -> {
            mostrarConfirmacionDialog();
        });

        evento = ((AlumnoEventoActivity) getActivity()).getEvento();

        cargarBotones();

        binding.buttonAbrirChat.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AlumnoChatActivity.class);
            intent.putExtra("evento", evento);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void mostrarConfirmacionDialog(){
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Confirmación");
        alertDialog.setMessage("¿Está seguro que desea dejar de apoyar el evento?");
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                desapoyarEvento();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void desapoyarEvento(){
        quitarAlumnoDeEvento();
    }

    private void quitarAlumnoDeEvento(){
        db.collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .collection("apoyos")
                .document(userUid)
                .delete()
                .addOnSuccessListener(unused -> {
                   Log.d("msg-test", "alumno quitado de eventos");
                   quitarEventoDeAlumno();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void quitarEventoDeAlumno(){
        db.collection("alumnos")
                .document(userUid)
                .collection("eventos")
                .document("evento"+evento.getFechaHoraCreacion().toString())
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "evento quitado de alumno");
                    quitarAlumnoDeChat();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void quitarAlumnoDeChat(){
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

                CometChat.leaveGroup(evento.getChatID(), new CometChat.CallbackListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("msg-test", s);

                        // TODO: RECARGAR ACTIVITY PARA ACTUALIZAR BOTONES
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.d("msg-test", "Group leaving failed with exception: " + e.getMessage());
                    }
                });

            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "Initialization failed with exception: " + e.getMessage());
            }
        });
    }

    private void cargarBotones(){
        if (evento.getEstado().equals("")){
            binding.buttonNotificaciones.setVisibility(View.GONE);
            binding.buttonEventoApoyando.setVisibility(View.GONE);
        }
    }
}