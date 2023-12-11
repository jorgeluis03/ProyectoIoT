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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListaUsuariosBaneadosAdapter extends FirestoreRecyclerAdapter<Alumno,ListaUsuariosBaneadosAdapter.UserBanViewHolder> {
    Context context;

    public ListaUsuariosBaneadosAdapter(@NonNull FirestoreRecyclerOptions<Alumno> options, Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserBanViewHolder holder, int position, @NonNull Alumno model) {
        holder.tvNombreUser.setText(model.getNombre()+' '+model.getApellidos());
        holder.tvCorreoUser.setText(model.getCorreo());

        holder.buttonDesban.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Desbanear")
                    .setMessage("¿Estás seguro que deseas desbanear este a usuario?")
                    .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                        //Hacer algo
                    })
                    .setPositiveButton("Aceptar", (dialogInterface, i) -> {

                        FirebaseUtilDg.getCollAlumnos().document(model.getId())
                                .update("estado","activo")
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context,"Usuario desbaneado",Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show());

                        AndroidUtilDg.enviarCorreo(model,"desbaneado");

                    })
                    .show();
        });
    }

    @NonNull
    @Override
    public UserBanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_baneados,parent,false);
        return new UserBanViewHolder(view);
    }

    public class UserBanViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUser;
        TextView tvCorreoUser;
        Button buttonDesban;
        public UserBanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUser = itemView.findViewById(R.id.textViewNombreUserBan_dg);
            tvCorreoUser = itemView.findViewById(R.id.textViewCorreoUserBan_dg);
            buttonDesban = itemView.findViewById(R.id.buttonDesbanearUser);

        }
    }
}






