package com.example.proyecto_iot.delegadoGeneral.utils;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    public static CollectionReference getCollAlumnos(){
        return FirebaseFirestore.getInstance().collection("alumnos");
    }

    public static StorageReference getPerfilUsuarioActualPicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("imgs_Perfil").child(FirebaseUtilDg.getusuarioActualId());
    }

    public static CollectionReference getDonaciones(){
        return FirebaseFirestore.getInstance().collection("donaciones");
    }

    public static Query getDonante(String userUid){
       return FirebaseFirestore.getInstance().collection("alumnos")
                .whereEqualTo("codigo",userUid);
    }
    public static CollectionReference getColeccionIdDonantes(String idDocument){
        return FirebaseUtilDg.getDonaciones().document(idDocument).collection("id");
    }

    public static CollectionReference getColeccionEventos(){
        return FirebaseFirestore.getInstance().collection("eventos");
    }

    //para storage
    public static StorageReference getFotoEvento(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic").child("");
    }
    public static CollectionReference getActividadesCollection(){
        return FirebaseFirestore.getInstance().collection("actividades");
    }
}
