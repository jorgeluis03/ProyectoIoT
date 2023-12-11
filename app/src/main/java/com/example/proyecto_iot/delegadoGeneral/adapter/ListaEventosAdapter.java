package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
        Glide.with(context)
                .load(model.getFotoUrl())
                .override(Target.SIZE_ORIGINAL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Error al cargar la imagen", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("Glide", "Imagen cargada con éxito");
                        return false;
                    }
                })
                .into(holder.imageView);

    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_actividad_eventos,parent,false);
        return new EventoViewHolder(view);
    }


    class EventoViewHolder extends RecyclerView.ViewHolder{
        TextView tituloEvento,lugarEvento,fechaEvento;
        ImageView imageView;
        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloEvento = itemView.findViewById(R.id.tv_titulo_evento);
            lugarEvento = itemView.findViewById(R.id.tv_lugar);
            fechaEvento = itemView.findViewById(R.id.tv_fecha);
            imageView = itemView.findViewById(R.id.img_foto_evento);

        }

    }



}
