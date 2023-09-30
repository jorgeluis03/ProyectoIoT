package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Objetos.Donacion;
import com.example.proyecto_iot.alumno.Objetos.Notificacion;

import java.util.List;

public class ListaDonacionesAdapter extends RecyclerView.Adapter<ListaDonacionesAdapter.DonacionViewHolder>{
    private List<Donacion> donacionList;
    private Context context;

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_alumno_donacion, parent, false);
        return new DonacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        Donacion donacion = donacionList.get(position);
        holder.donacion = donacion;

        TextView textNombreDonacion = holder.itemView.findViewById(R.id.textNombreDonacion);
        TextView textHora = holder.itemView.findViewById(R.id.textHora);
        TextView textDonacion = holder.itemView.findViewById(R.id.textDonacion);
        textNombreDonacion.setText(donacion.getTexto());
        textHora.setText(donacion.getHora());
        textDonacion.setText(donacion.getDonacion());
    }

    @Override
    public int getItemCount() {
        return donacionList.size();
    }

    public class DonacionViewHolder extends RecyclerView.ViewHolder{
        Donacion donacion;

        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public List<Donacion> getDonacionList() {
        return donacionList;
    }

    public void setDonacionList(List<Donacion> donacionList) {
        this.donacionList = donacionList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
