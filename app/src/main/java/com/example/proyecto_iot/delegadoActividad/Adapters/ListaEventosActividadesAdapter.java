package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.delegadoActividad.Entities.Actividad;

import java.util.List;

public class ListaEventosActividadesAdapter extends RecyclerView.Adapter<ListaEventosActividadesAdapter.EventoAViewHolder> {

    private List<Evento> eventoAList;

    private Context context;

    @NonNull
    @Override
    public EventoAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_da_actividad, parent, false);
        return new EventoAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAViewHolder holder, int position){
        Evento evento = eventoAList.get(position);
        holder.evento = evento;

        TextView textTitulo = holder.itemView.findViewById(R.id.textActividad2);
        TextView textFecha = holder.itemView.findViewById(R.id.textHoraAct);

        String date = evento.getFecha() + " " + evento.getHora();
        textTitulo.setText(evento.getTitulo());
        textFecha.setText(date);
    }

    public int getItemCount(){
        return eventoAList.size();
    }
    public class EventoAViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        public EventoAViewHolder(@NonNull View itemView) {
            super(itemView);
            Button participantes = itemView.findViewById(R.id.buttonParticipantes);
            participantes.setOnClickListener(view -> {

            });
            Button borrar = itemView.findViewById(R.id.buttonDelete);
            borrar.setOnClickListener(view -> {

            });
            Button editar = itemView.findViewById(R.id.buttonEdit);
            editar.setOnClickListener(view -> {

            });
        }
    }

    public List<Evento> getEventoAList(){return eventoAList;}
    public void setEventoAList(List<Evento> eventoAList){this.eventoAList = eventoAList;}
    public Context getContext() {return context;}

    public void setContext(Context context){this.context = context;}

}
