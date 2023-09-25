package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Objetos.Notificacion;

import java.util.List;

public class ListaNotificacionesAdapter extends RecyclerView.Adapter<ListaNotificacionesAdapter.NotificacionViewHolder>{
    private List<Notificacion> notificacionList;
    private Context context;

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_alumno_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notificacion notificacion = notificacionList.get(position);
        holder.notificacion = notificacion;

        TextView textNotificacion = holder.itemView.findViewById(R.id.textNotificacion);
        TextView textHora = holder.itemView.findViewById(R.id.textHora);
        textNotificacion.setText(notificacion.getTexto());
        textHora.setText(notificacion.getHora());
    }

    @Override
    public int getItemCount() {
        return notificacionList.size();
    }

    public class NotificacionViewHolder extends RecyclerView.ViewHolder{
        Notificacion notificacion;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public List<Notificacion> getNotificacionList() {
        return notificacionList;
    }

    public void setNotificacionList(List<Notificacion> notificacionList) {
        this.notificacionList = notificacionList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
