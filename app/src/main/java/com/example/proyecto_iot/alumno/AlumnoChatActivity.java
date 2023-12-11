package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Chat;
import com.example.proyecto_iot.alumno.Entities.ChatMessage;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Notificacion;
import com.example.proyecto_iot.alumno.Fragments.AlumnoHeader3Fragment;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaMensajesAdapter;
import com.example.proyecto_iot.alumno.Utils.FirebaseUtilAl;
import com.example.proyecto_iot.databinding.ActivityAlumnoChatBinding;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseFCMUtils;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class AlumnoChatActivity extends AppCompatActivity {
    private ActivityAlumnoChatBinding binding;
    private Alumno alumnoLogueado;
    private Evento evento;
    private String chatID;
    private Chat chat;
    private ListaMensajesAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        evento = (Evento) getIntent().getSerializableExtra("evento");
        chatID = "evento"+evento.getFechaHoraCreacion().toString();
        setUpChat();
        cargarInterfaz();
        setUpChatRecyclerView();

        binding.buttonEnviarMensaje.setOnClickListener(view -> {
            String message = binding.inputMessage.getText().toString().trim();
            if (message.isEmpty())
                return;
            sendMessageToEvent(message);
        });
    }

    private void setUpChat(){
        FirebaseUtilAl.getChatRoomReference(chatID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chat = task.getResult().toObject(Chat.class);
                binding.buttonEnviarMensaje.setClickable(true);
            }
            else{
                Log.d("msg-test", "error al recuperar chat: "+task.getException().getMessage());
            }
        });
    }

    private void sendMessageToEvent(String message){

        HashMap<String, Object> chatUpdate = new HashMap<>();
        chatUpdate.put("lastMessageTimestamp", Timestamp.now());
        chatUpdate.put("lastMessageSenderID", FirebaseUtilDg.getusuarioActualId());

        //chat.setLastMessageTimestamp(Timestamp.now());
        //chat.setLastMessageSenderID(FirebaseUtilDg.getusuarioActualId());
        FirebaseUtilAl.getChatRoomReference(chatID).update(chatUpdate); // actualizando ultima hora de mensaje del chat y ultimo emisor de un mensaje en el doc del chat

        ChatMessage chatMessage = new ChatMessage(message, FirebaseUtilDg.getusuarioActualId(), Timestamp.now());
        FirebaseUtilAl.getChatMessageReference(chatID).add(chatMessage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("msg-test", "mensaje enviado: "+message);
                        binding.inputMessage.setText("");
                        sendNotification(message);
                    }
                });
    }

    private void setUpChatRecyclerView(){
        Query query = FirebaseUtilAl.getChatMessageReference(chatID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        adapter = new ListaMensajesAdapter(options, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        binding.rvChat.setLayoutManager(manager);
        binding.rvChat.setAdapter(adapter);
        adapter.startListening();

        //scrolleo autom
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                binding.rvChat.smoothScrollToPosition(0);
            }
        });
    }

    private void sendNotification(String message){
        for (String userID: chat.getUserIDs()){
            if (!alumnoLogueado.getId().equals(userID)) {
                FirebaseUtilDg.getCollAlumnos().document(userID).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Alumno alumnoChat = task.getResult().toObject(Alumno.class);
                        sendNotificationToUser(alumnoChat.getFcmToken(), message);
                        notifyFirebase(alumnoChat.getId(), "newChat", evento, message);
                        Log.d("msg-test", "notificacion enviada a: "+alumnoChat.getNombre());
                    }
                });
            }
        }

    }

    private void notifyFirebase(String userId, String categoria, Evento evento, String message) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(categoria);
        notificacion.setHora(Date.from(Instant.now()));
        notificacion.setTexto("Nuevo mensaje\n"+evento.getTitulo()+": "+message);
        notificacion.setEvento(evento);
        db.collection("alumnos").document(userId)
                .collection("notificaciones")
                .add(notificacion)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test", "notificacion de nuevo mensaje");
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void sendNotificationToUser(String userToken, String message){
        try {
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", evento.getTitulo().substring(0, 25));
            notificationObject.put("body", alumnoLogueado.getNombre()+": "+message);

            jsonObject.put("notification", notificationObject);
            jsonObject.put("to", userToken);

            FirebaseFCMUtils.callApi(jsonObject);
        }
        catch (Exception e){
            Log.d("msg-test", "error notificaciones: "+e.getMessage());
        }
    }

    private Alumno obtenerAlumnoDeMemoria(){
        try (FileInputStream fileInputStream = openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);

            return alumno;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void cargarInterfaz(){
        alumnoLogueado = obtenerAlumnoDeMemoria();
        // para setear nombre de header
        Bundle bundle = new Bundle();

        if (evento.getEstado().equals("inactivo")){
            binding.relativeInputChat.setVisibility(View.GONE);
            bundle.putBoolean("activo", false);
        }

        bundle.putString("header", evento.getTitulo());
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentHeaderChat, AlumnoHeader3Fragment.class, bundle)
                .commit();

    }
}