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
import com.example.proyecto_iot.delegadoGeneral.utils.AndroidUtilDg;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListaUsuariosPendiAdapter extends FirestoreRecyclerAdapter<Alumno,ListaUsuariosPendiAdapter.UserPendiViewHolder> {

    private Context context;

    public ListaUsuariosPendiAdapter(@NonNull FirestoreRecyclerOptions<Alumno> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserPendiViewHolder holder, int position, @NonNull Alumno model) {
        holder.tvNombreUser.setText(model.getNombre()+' '+model.getApellidos());
        holder.tvCorreoUser.setText(model.getCorreo());
        holder.buttonAceptar.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context)
                        .setTitle("Aceptar")
                        .setMessage("¿Estás seguro que deseas aceptar este a usuario?")
                        .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                            //Hacer algo
                        })
                        .setPositiveButton("Aceptar", (dialogInterface, i) -> {

                            FirebaseUtilDg.getCollAlumnos().document(model.getId())
                                    .update("estado","activo")
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(context,"Usuario aceptado",Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show());
                            AndroidUtilDg.enviarCorreo(model,"aceptado");
                        })
                        .show();

        });

        holder.buttonRechazar.setOnClickListener(view -> {
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
                                FirebaseUtilDg.getCollAlumnos().document(model.getId())
                                        .delete()
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(context,"Usuario rechazado",Toast.LENGTH_SHORT).show();

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context,"Algo pasó",Toast.LENGTH_SHORT).show();

                                        });
                                AndroidUtilDg.enviarCorreo(model,"rechazado");
                            }
                        })
                        .show();

        });

    }

    @NonNull
    @Override
    public UserPendiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_pedients,parent,false);
        return new UserPendiViewHolder(view);
    }

    public class UserPendiViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUser;
        TextView tvCorreoUser;
        Button buttonAceptar, buttonRechazar;
        public UserPendiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUser=itemView.findViewById(R.id.textViewNombreUserPendi_dg);
            tvCorreoUser=itemView.findViewById(R.id.textViewCorreoUserPendi_dg);
            buttonAceptar = itemView.findViewById(R.id.buttonAceptarUser);
            buttonRechazar = itemView.findViewById(R.id.buttonRechazarUserPendi);


        }
    }


}
