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
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListaUsuariosBaneadosAdapter extends RecyclerView.Adapter<ListaUsuariosBaneadosAdapter.UsuariosBanViewHolder> {
    private List<Usuario> lista;
    private Context context;
    FirebaseFirestore db;

    @NonNull
    @Override
    public UsuariosBanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //se infla el irv
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_baneados,parent,false);
        return new UsuariosBanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosBanViewHolder holder, int position) {
        //Como se llenara cada viweHolder cuando se tenga la info
        Usuario userBan = lista.get(position);
        holder.usuario = userBan;

        TextView tvNombreUser = holder.itemView.findViewById(R.id.textViewNombreUserBan_dg);
        TextView tvCorreoUser = holder.itemView.findViewById(R.id.textViewCorreoUserBan_dg);

        tvNombreUser.setText(userBan.getNombre()+' '+userBan.getApellido());
        tvCorreoUser.setText(userBan.getCorreo());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    //Clase view Holder
    public class UsuariosBanViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        public UsuariosBanViewHolder(@NonNull View itemView) {
            super(itemView);

            Button buttonDesbanear = itemView.findViewById(R.id.buttonDesbanearUser);
            buttonDesbanear.setOnClickListener(view -> {

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Desbanear")
                        .setMessage("¿Estás seguro que deseas desbanear este a usuario?")
                        .setNeutralButton("Cancelar", (dialogInterface, i) -> {
                            //Hacer algo
                        })
                        .setPositiveButton("Aceptar", (dialogInterface, i) -> {

                            db = FirebaseFirestore.getInstance();
                            db.collection("usuarios")
                                    .document(usuario.getCodigo())
                                    .update("estado","activo")
                                    .addOnSuccessListener(unused -> {

                                        lista.remove(usuario);
                                        // Notificar al adaptador que los datos han cambiado
                                        notifyDataSetChanged();
                                        Toast.makeText(context,"Usuario desbaneado",Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context,"algo pasó",Toast.LENGTH_SHORT).show();
                                    });

                        })
                        .show();


            });

        }
    }


    //encapsulamiento
    public List<Usuario> getLista() {
        return lista;
    }

    public void setLista(List<Usuario> lista) {
        this.lista = lista;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
