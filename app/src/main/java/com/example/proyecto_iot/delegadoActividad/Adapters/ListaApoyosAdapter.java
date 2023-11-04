package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.delegadoActividad.DaGestionEventosActivity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;

import java.util.List;

public class ListaApoyosAdapter extends RecyclerView.Adapter<ListaApoyosAdapter.ApoyoViewHolder>{
    private List<Alumno> apoyos;
    private Context context;

    @NonNull
    @Override
    public ListaApoyosAdapter.ApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rv_da_apoyos, parent, false);
        return new ApoyoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaApoyosAdapter.ApoyoViewHolder holder, int position) {
        Alumno apoyo = getApoyos().get(position);
        holder.apoyo = apoyo;

        TextView nombre = holder.itemView.findViewById(R.id.nombreApoyo);
        TextView tipo = holder.itemView.findViewById(R.id.textTipoAlumno);

        nombre.setText(apoyo.getNombre());
        tipo.setText(apoyo.getTipo());

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
    }

    @Override
    public int getItemCount() {
        return getApoyos().size();
    }

    public List<Alumno> getApoyos() {
        return apoyos;
    }

    public void setApoyos(List<Alumno> apoyos) {
        this.apoyos = apoyos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ApoyoViewHolder extends RecyclerView.ViewHolder{
        Alumno apoyo;
        public ApoyoViewHolder(@NonNull View itemView) {
            super(itemView);/*
            Button button = itemView.findViewById(R.id.buttonCard);
            button.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), DaGestionEventosActivity.class);
                intent.putExtra("actividadCard", actividad);
                itemView.getContext().startActivity(intent);
            });*/
        }
    }
}
