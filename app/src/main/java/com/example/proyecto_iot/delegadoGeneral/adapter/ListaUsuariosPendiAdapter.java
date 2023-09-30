package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;

import java.util.List;

public class ListaUsuariosPendiAdapter extends RecyclerView.Adapter<ListaUsuariosPendiAdapter.UsuariosViewHolder>{
    private List<Usuario> listaUsuariosPendi;
    private Context context;

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_alumnos_pedients,parent,false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        Usuario userRegi = listaUsuariosPendi.get(position);
        holder.usuario = userRegi;

        TextView tvNombreUser = holder.itemView.findViewById(R.id.textViewNombreUserPendi_dg);
        TextView tvCorreoUser = holder.itemView.findViewById(R.id.textViewCorreoUserPendi_dg);

        tvNombreUser.setText(userRegi.getNombre()+' '+userRegi.getApellido());
        tvCorreoUser.setText(userRegi.getCorreo());


    }

    @Override
    public int getItemCount() {
        return listaUsuariosPendi.size();
    }


    //SubClase ViewHolder
    public class UsuariosViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    //Encapsulamiento
    public List<Usuario> getListaUsuarios() {
        return listaUsuariosPendi;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuariosPendi = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
