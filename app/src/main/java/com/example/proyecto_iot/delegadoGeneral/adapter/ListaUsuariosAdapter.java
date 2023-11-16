package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuariosViewHolder>{
    private List<Alumno> listaUsuarios;
    private Context context;
    FirebaseFirestore db;

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_regist,parent,false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        Alumno userRegi = listaUsuarios.get(position);
        holder.alumno = userRegi;

        TextView tvNombreUser = holder.itemView.findViewById(R.id.textViewNombreUserRegi_dg);
        TextView tvCorreoUser = holder.itemView.findViewById(R.id.textViewCorreoUserRegi_dg);

        tvNombreUser.setText(userRegi.getNombre()+' '+userRegi.getApellidos());
        tvCorreoUser.setText(userRegi.getCorreo());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }


    //SubClase ViewHolder
    public class UsuariosViewHolder extends RecyclerView.ViewHolder{
        Alumno alumno;
        public UsuariosViewHolder(@NonNull View itemView) {

            super(itemView);

            Button buttonBanear = itemView.findViewById(R.id.buttonBanearUser);
            buttonBanear.setOnClickListener(view -> {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Banear")
                        .setMessage("¿Estás seguro que deseas banear a este usuario?")
                        .setNeutralButton("Cancelar", (dialog, which) -> {

                            // Responder a la pulsación del botón neutral

                        })
                        .setPositiveButton("Aceptar", (dialog, which) -> {

                            // Responder a la pulsación del botón positivo
                            db = FirebaseFirestore.getInstance();
                            db.collection("alumnos").document(alumno.getId())
                                    .update("estado","baneado")
                                    .addOnSuccessListener(unused -> {
                                        // Eliminar el usuario de la lista de datos
                                        listaUsuarios.remove(alumno);

                                        // Notificar al adaptador que los datos han cambiado
                                        notifyDataSetChanged();
                                        Toast.makeText(context,"Usuario baneado",Toast.LENGTH_SHORT).show();

                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context,"Algo pasó",Toast.LENGTH_SHORT).show();

                                    });
                        })
                        .show();
            });

        }
    }


    //Encapsulamiento
    public List<Alumno> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Alumno> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
