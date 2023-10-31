package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

                db = FirebaseFirestore.getInstance();
                db.collection("usuarios").document(alumno.getCodigo())
                                .update("estado","activo")
                                .addOnSuccessListener(unused -> {
                                    // Eliminar el usuario de la lista de datos
                                    listaUsuariosPendi.remove(alumno);
                                    // Notificar al adaptador que los datos han cambiado
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"Usuario aceptado",Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context,"algo pasó",Toast.LENGTH_SHORT).show();
                                });



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
                                db.collection("usuarios").document(alumno.getCodigo())
                                        .delete()
                                        .addOnSuccessListener(unused -> {
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
