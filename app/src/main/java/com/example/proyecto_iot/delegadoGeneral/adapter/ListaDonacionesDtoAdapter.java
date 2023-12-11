package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.dto.DonacionDto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class ListaDonacionesDtoAdapter extends FirestoreRecyclerAdapter<DonacionDto,ListaDonacionesDtoAdapter.DonacionDtoViewHolder> {
    Context context;
    private List<DonacionDto> mSnapshots = new ArrayList<>();

    public void updateData(List<DonacionDto> newData) {
        // Actualiza los datos y notifica al adaptador sobre el cambio
        this.mSnapshots.clear();
        this.mSnapshots.addAll(newData);
        notifyDataSetChanged();
    }

    public ListaDonacionesDtoAdapter(@NonNull FirestoreRecyclerOptions<DonacionDto> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DonacionDtoViewHolder holder, int position, @NonNull DonacionDto model) {
        holder.nombreDontante.setText(model.getNombreDonante());
        holder.horaDonacion.setText(model.getHorahoraFecha());
        holder.montoDonacion.setText("S/"+model.getMonto());
    }

    @NonNull
    @Override
    public DonacionDtoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_detalle_donacion,parent,false);
        return new DonacionDtoViewHolder(view);
    }

    class DonacionDtoViewHolder extends RecyclerView.ViewHolder{
        TextView nombreDontante, horaDonacion,montoDonacion;
        Button aceptarBtn, rechazarBtn;
        public DonacionDtoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreDontante = itemView.findViewById(R.id.nombreDontante);
            horaDonacion = itemView.findViewById(R.id.horaDonacion);
            montoDonacion = itemView.findViewById(R.id.montoDonacion);
            aceptarBtn = itemView.findViewById(R.id.btnAceptarDonacion);
            rechazarBtn = itemView.findViewById(R.id.btnRechazarDonacion);
        }
    }
}
