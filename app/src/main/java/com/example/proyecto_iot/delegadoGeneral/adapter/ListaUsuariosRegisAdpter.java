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
import com.example.proyecto_iot.delegadoGeneral.utils.AndroidUtilDg;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class ListaUsuariosRegisAdpter extends FirestoreRecyclerAdapter<Alumno,ListaUsuariosRegisAdpter.DelegadoViewHolder> {
    Context context;

    public ListaUsuariosRegisAdpter(@NonNull FirestoreRecyclerOptions<Alumno> options,Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull DelegadoViewHolder holder, int position, @NonNull Alumno model) {
        holder.tvNombreUser.setText(model.getNombre()+' '+model.getApellidos());
        holder.tvCorreoUser.setText(model.getCorreo());

        holder.buttonBanear.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Banear")
                    .setMessage("¿Estás seguro que deseas banear a este usuario?")
                    .setNeutralButton("Cancelar", (dialog, which) -> {

                    })
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        // Responder a la pulsación del botón positivo
                        FirebaseUtilDg.getCollAlumnos().document(model.getId())
                        .update("estado","baneado")
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context,"Usuario baneado",Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show());

                        AndroidUtilDg.enviarCorreo(model,"baneado");
                    })
                    .show();
        });
    }

    @NonNull
    @Override
    public DelegadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_regist,parent,false);
        return new DelegadoViewHolder(view);
    }


    public class DelegadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUser;
        TextView tvCorreoUser;
        Button buttonBanear;
        public DelegadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUser=itemView.findViewById(R.id.textViewNombreUserRegi_dg);
            tvCorreoUser=itemView.findViewById(R.id.textViewCorreoUserRegi_dg);
            buttonBanear = itemView.findViewById(R.id.buttonBanearUser);


        }
    }
}
