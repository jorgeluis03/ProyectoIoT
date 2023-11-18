package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListaActiEstadisiticasAdapter extends FirestoreRecyclerAdapter<Actividades, ListaActiEstadisiticasAdapter.ActiViewHolder> {
    Context context;

    public ListaActiEstadisiticasAdapter(@NonNull FirestoreRecyclerOptions<Actividades> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActiViewHolder holder, int position, @NonNull Actividades model) {
        holder.nombreActividad.setText(model.getNombre());
        holder.cantApoyo.setText("5 participantes");
    }

    @NonNull
    @Override
    public ActiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_estadisticas,parent,false);
        return new ActiViewHolder(view);
    }

    class ActiViewHolder extends RecyclerView.ViewHolder{
        TextView nombreActividad, cantApoyo;

        public ActiViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreActividad = itemView.findViewById(R.id.tvNombreActividad);
            cantApoyo = itemView.findViewById(R.id.tv_apoyos);

        }
    }

}
