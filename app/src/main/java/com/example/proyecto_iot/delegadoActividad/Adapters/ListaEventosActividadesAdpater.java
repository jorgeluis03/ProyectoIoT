package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoActividad.Entities.Actividad;

import java.util.List;

public class ListaEventosActividadesAdpater extends RecyclerView.Adapter<ListaEventosActividadesAdpater.ActividadViewHolder> {

    private List<Actividad> actividadList;

    private Context context;

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_da_actividad, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position){
        Actividad actividad = actividadList.get(position);
        holder.actividad = actividad;

        TextView textTitulo = holder.itemView.findViewById(R.id.textActividad2);
        TextView textFecha = holder.itemView.findViewById(R.id.textHoraAct);

        String date = actividad.getFecha() + " " + actividad.getHora();
        textTitulo.setText(actividad.getNombre());
        textFecha.setText(date);
    }

    public int getItemCount(){
        return actividadList.size();
    }
    public class ActividadViewHolder extends RecyclerView.ViewHolder{
        Actividad actividad;
        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            /*
            ConstraintLayout constraintLayout = itemView.findViewById(R.id.actividad);
            constraintLayout.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), DaGestionActivity.class);
                itemView.getContext().startActivity(intent);
            });

             */
        }
    }

    public List<Actividad> getActividadList(){return actividadList;}
    public void setActividadList(List<Actividad> actividadList){this.actividadList = actividadList;}
    public Context getContext() {return context;}

    public void setContext(Context context){this.context = context;}

}
