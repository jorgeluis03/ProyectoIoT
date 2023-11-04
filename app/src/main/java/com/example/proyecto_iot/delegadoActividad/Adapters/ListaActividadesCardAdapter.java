package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;

import java.util.List;

public class ListaActividadesCardAdapter extends RecyclerView.Adapter<ListaActividadesCardAdapter.ActividadCardViewHolder> {
    private List<Actividades> actividadCardList;
    private Context context;

    @NonNull
    @Override
    public ActividadCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rv_da_actividad_card, parent, false);
        return new ActividadCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadCardViewHolder holder, int position) {
        Actividades actividad = getActividadCardList().get(position);
        holder.actividad = actividad;

        TextView textTitulo = holder.itemView.findViewById(R.id.textActividadCard);

        textTitulo.setText(actividad.getNombre());

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
    }

    @Override
    public int getItemCount() {
        return getActividadCardList().size();
    }

    public List<Actividades> getActividadCardList() {
        return actividadCardList;
    }

    public void setActividadCardList(List<Actividades> actividadCardList) {
        this.actividadCardList = actividadCardList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ActividadCardViewHolder extends RecyclerView.ViewHolder{

        Actividades actividad;
        public ActividadCardViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout constraintLayout = itemView.findViewById(R.id.actividadCard);
            constraintLayout.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), AlumnoEventoActivity.class);
                intent.putExtra("actividadCard", actividad);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
