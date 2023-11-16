package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.dto.DonacionDto;

import java.util.List;

public class ListaDonacionesAdapter extends RecyclerView.Adapter<ListaDonacionesAdapter.DonacionViewHolder> {
    private List<DonacionDto> lista;
    private Context context;

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_detalle_donacion,parent,false);
        return new DonacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        DonacionDto donacion = lista.get(position);
        holder.donacionDto = donacion;

        holder.nombreDonador.setText(donacion.getNombreDonante());
        holder.horaDonacion.setText(donacion.getHorahoraFecha());
        holder.montoDonacion.setText("S/"+donacion.getMonto());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    class DonacionViewHolder extends RecyclerView.ViewHolder{
        DonacionDto donacionDto;
        TextView nombreDonador,horaDonacion,montoDonacion;
        ImageButton btnAceptar, btnRechazar;

        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreDonador = itemView.findViewById(R.id.nombreDontante);
            horaDonacion = itemView.findViewById(R.id.horaDonacion);
            montoDonacion = itemView.findViewById(R.id.montoDonacion);
            btnAceptar = itemView.findViewById(R.id.btnAceptarDonacion);
            btnRechazar = itemView.findViewById(R.id.btnRechazarDonacion);

        }

    }

    //encapsulamiento

    public List<DonacionDto> getLista() {
        return lista;
    }

    public void setLista(List<DonacionDto> lista) {
        this.lista = lista;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
