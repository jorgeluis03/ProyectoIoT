package com.example.proyecto_iot.delegadoGeneral.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtilDg {
    public static String getusuarioActualId(){
        //devuelvo el id de autenticacion del usuario logeado
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference getUsuarioActualDetalles(){
        return FirebaseFirestore.getInstance().collection("alumnos").document(FirebaseUtilDg.getusuarioActualId());
    }

    public static StorageReference getPerfilUsuarioActualPicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("imgs_Perfil").child(FirebaseUtilDg.getusuarioActualId());
    }
}
