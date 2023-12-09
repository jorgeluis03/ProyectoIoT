package com.example.proyecto_iot.alumno.Utils;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtilAl {
    public static DocumentReference getChatRoomReference(String chatID){
        return FirebaseFirestore.getInstance().collection("chats").document(chatID);
    }

    public static CollectionReference getChatMessageReference(String chatID){
        return getChatRoomReference(chatID).collection("chatMessages");
    }
}
