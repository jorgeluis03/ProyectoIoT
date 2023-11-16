package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;

import java.util.List;

public class ListaDelegadosAdapter extends RecyclerView.Adapter<ListaDelegadosAdapter.UsuariosViewHolder>{
    private List<Alumno> listaUsuarios;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Alumno alumno);
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
        Alumno user = listaUsuarios.get(position);
        holder.alumno = user;

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(user);
            }
        });

        TextView tvNombreUser = holder.itemView.findViewById(R.id.textViewNombreDelegado);
        TextView tvCorreoUser = holder.itemView.findViewById(R.id.textViewCorreoDelegado);

        tvNombreUser.setText(user.getNombre()+' '+user.getApellidos());
        tvCorreoUser.setText(user.getCorreo());

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

            itemView.setOnClickListener(view -> {

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
