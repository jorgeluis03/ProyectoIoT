package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Chat;
import com.example.proyecto_iot.alumno.Entities.ChatMessage;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader3Fragment;
import com.example.proyecto_iot.alumno.Utils.FirebaseUtilAl;
import com.example.proyecto_iot.databinding.ActivityAlumnoChatBinding;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.firebase.Timestamp;

public class AlumnoChatActivity extends AppCompatActivity {
    private ActivityAlumnoChatBinding binding;
    private Evento evento;
    private String chatID;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        evento = (Evento) getIntent().getSerializableExtra("evento");
        chatID = "evento"+evento.getFechaHoraCreacion().toString();
        Log.d("msg-test", "chatID: "+chatID);

        cargarInterfaz();
        getOrCreateChat();

        binding.buttonEnviarMensaje.setOnClickListener(view -> {
            String message = binding.inputMessage.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToEvent(message);
        });
    }

    private void sendMessageToEvent(String message){
        chat.setLastMessageTimestamp(Timestamp.now());
        chat.setLastMessageSenderID(FirebaseUtilDg.getusuarioActualId());
        FirebaseUtilAl.getChatRoomReference(chatID).set(chat); // actualizando ultima hora de mensaje del chat y ultimo emisor de un mensaje en el doc del chat

        ChatMessage chatMessage = new ChatMessage(message, FirebaseUtilDg.getusuarioActualId(), Timestamp.now());
        FirebaseUtilAl.getChatMessageReference(chatID).add(chatMessage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("msg-test", "mensaje enviado: "+message);
                        binding.inputMessage.setText("");
                    }
                });
    }

    private void getOrCreateChat(){
        FirebaseUtilAl.getChatRoomReference(chatID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chat = task.getResult().toObject(Chat.class);
                if(chat == null){
                    // creador por primera vez -> debe agregarse en delegado de actividad
                    Log.d("msg-test", "chat nuevo");
                }
                else{
                    Log.d("msg-test", "chat existente");
                }
            }
        });
    }

    private void cargarInterfaz(){
        // seteando nombre de header
        Bundle bundle = new Bundle();
        bundle.putString("header", evento.getTitulo());
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeaderChat, AlumnoHeader3Fragment.class, bundle)
                .commit();

        if (evento.getEstado().equals("inactivo")){

        }
    }
}