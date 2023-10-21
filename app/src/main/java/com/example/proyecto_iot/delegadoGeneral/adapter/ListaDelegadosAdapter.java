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

public class ListaDelegadosAdapter extends RecyclerView.Adapter<ListaDelegadosAdapter.UsuariosViewHolder>{
    private List<Usuario> listaUsuarios;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(Usuario usuario);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_asignar_delegados,parent,false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        Usuario user = listaUsuarios.get(position);
        holder.usuario = user;

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(user);
            }
        });

        TextView tvNombreUser = holder.itemView.findViewById(R.id.textViewNombreDelegado);
        TextView tvCorreoUser = holder.itemView.findViewById(R.id.textViewCorreoDelegado);

        tvNombreUser.setText(user.getNombre()+' '+user.getApellido());
        tvCorreoUser.setText(user.getCorreo());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }


    //SubClase ViewHolder
    public class UsuariosViewHolder extends RecyclerView.ViewHolder{
        Usuario usuario;
        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> {

            });
        }
    }



    //Encapsulamiento
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
