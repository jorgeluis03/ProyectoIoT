package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListaEventosAdapter extends FirestoreRecyclerAdapter<Evento,ListaEventosAdapter.EventoViewHolder> {
    Context context;


    public ListaEventosAdapter(@NonNull FirestoreRecyclerOptions<Evento> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventoViewHolder holder, int position, @NonNull Evento model) {

        holder.tituloEvento.setText(model.getTitulo());
        holder.lugarEvento.setText(model.getLugar());
        holder.fechaEvento.setText(model.getFecha()+' '+model.getHora());

    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_actividad_eventos,parent,false);
        return new EventoViewHolder(view);
    }


    class EventoViewHolder extends RecyclerView.ViewHolder{
        TextView tituloEvento,lugarEvento,fechaEvento;
        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloEvento = itemView.findViewById(R.id.tv_titulo_evento);
            lugarEvento = itemView.findViewById(R.id.tv_lugar);
            fechaEvento = itemView.findViewById(R.id.tv_fecha);

        }

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
