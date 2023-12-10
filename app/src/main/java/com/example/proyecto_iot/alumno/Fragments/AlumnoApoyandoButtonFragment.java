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
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoChatActivity;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.databinding.FragmentAlumnoApoyandoButtonBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlumnoApoyandoButtonFragment extends Fragment {

    FragmentAlumnoApoyandoButtonBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Evento evento;
    private String eventoID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoApoyandoButtonBinding.inflate(inflater, container, false);

        binding.buttonEventoApoyando.setOnClickListener(view -> {
            mostrarConfirmacionDialog();
        });

        evento = ((AlumnoEventoActivity) getActivity()).getEvento();
        eventoID = "evento"+evento.getFechaHoraCreacion().toString();

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
        alertDialog.setMessage("¿Estas seguro que deseas dejar de apoyar al evento?");
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
                .document(eventoID)
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
                .document(eventoID)
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
        String chatID = eventoID;

        DocumentReference chatReference = db.collection("chats").document(chatID);
        chatReference.update("userIDs", FieldValue.arrayRemove(userUid));
    }

    private void cargarBotones(){
        if (evento.getEstado().equals("inactivo")){
            binding.buttonNotificaciones.setVisibility(View.GONE);
            binding.buttonEventoApoyando.setVisibility(View.GONE);
        }
    }
}