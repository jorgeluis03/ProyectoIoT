package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.User;
import com.example.proyecto_iot.AppConstants;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListaUsuariosPendiAdapter extends RecyclerView.Adapter<ListaUsuariosPendiAdapter.UsuariosViewHolder>{
    private List<Alumno> listaUsuariosPendi;
    private Context context;
    FirebaseFirestore db;

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_pedients,parent,false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        Alumno userRegi = listaUsuariosPendi.get(position);
        holder.alumno = userRegi;

        TextView tvNombreUser = holder.itemView.findViewById(R.id.textViewNombreUserPendi_dg);
        TextView tvCorreoUser = holder.itemView.findViewById(R.id.textViewCorreoUserPendi_dg);

        tvNombreUser.setText(userRegi.getNombre()+' '+userRegi.getApellidos());
        tvCorreoUser.setText(userRegi.getCorreo());

    }

    @Override
    public int getItemCount() {
        return listaUsuariosPendi.size();
    }


    //SubClase ViewHolder
    public class UsuariosViewHolder extends RecyclerView.ViewHolder{
        Alumno alumno;
        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);

            //aceptar usuario
            Button buttonAceptar = itemView.findViewById(R.id.buttonAceptarUser);
            buttonAceptar.setOnClickListener(view -> {

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Aceptar")
                        .setMessage("¿Estás seguro que deseas aceptar este a usuario?")
                        .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                            //Hacer algo
                        })
                        .setPositiveButton("Aceptar", (dialogInterface, i) -> {

                            db = FirebaseFirestore.getInstance();
                            db.collection("alumnos").document(alumno.getId())
                                    .update("estado","activo")
                                    .addOnSuccessListener(unused -> {
                                        crearUsuarioCometChat(alumno);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context,"algo pasó",Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .show();
            });

            //Rechazar solicitud
            Button buttonRechazar = itemView.findViewById(R.id.buttonBanearUserPendi);
            buttonRechazar.setOnClickListener(view -> {

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Rechazar")
                        .setMessage("¿Estás seguro que deseas rechazar la solicitud de este usuario?")
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Responder a la pulsación del botón neutral
                            }
                        })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Responder a la pulsación del botón positivo
                                db = FirebaseFirestore.getInstance();
                                db.collection("alumnos").document(alumno.getId())
                                        .delete()
                                        .addOnSuccessListener(unused -> {

                                            eliminarUsuarioAuthentication();

                                            // Eliminar el usuario de la lista de datos
                                            listaUsuariosPendi.remove(alumno);

                                            // Notificar al adaptador que los datos han cambiado
                                            notifyDataSetChanged();
                                            Toast.makeText(context,"Usuario eliminado",Toast.LENGTH_SHORT).show();

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context,"Algo pasó",Toast.LENGTH_SHORT).show();

                                        });
                            }
                        })
                        .show();

            });
        }
    }

    private void crearUsuarioCometChat(Alumno alumno){
        String region = AppConstants.REGION;
        String appID = AppConstants.APP_ID;
        String authKey = AppConstants.AUTH_KEY;

        AppSettings appSettings = new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(getContext(), appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("msg-test", "Initialization completed successfully");

                // creando usuario
                User nuevoUsuario = new User();
                nuevoUsuario.setUid(alumno.getId());
                nuevoUsuario.setName(alumno.getNombre()+" "+alumno.getApellidos());

                CometChat.createUser(nuevoUsuario, authKey, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Log.d("msg-test", "usuario creado: "+user.toString());

                        // Eliminar el usuario de la lista de datos
                        listaUsuariosPendi.remove(alumno);
                        // Notificar al adaptador que los datos han cambiado
                        notifyDataSetChanged();

                        Toast.makeText(context,"Usuario aceptado",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.d("msg-test", "error creando usuario: "+e.getMessage());
                    }
                });

            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "Initialization failed with exception: " + e.getMessage());
            }
        });
    }

    private void eliminarUsuarioAuthentication(){

    }

    /*int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    lista.remove(position);
                    notifyItemRemoved(position);

                }*/

    //Encapsulamiento
    public List<Alumno> getListaUsuarios() {
        return listaUsuariosPendi;
    }

    public void setListaUsuarios(List<Alumno> listaUsuarios) {
        this.listaUsuariosPendi = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
