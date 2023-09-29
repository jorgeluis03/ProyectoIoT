package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;

import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividesViewHolder> {
    private List<Actividades> listaActividades;
    private Context context;

    //override metodos
    @NonNull
    @Override
    public ActividesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflar el irv
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_actividades,parent,false);
        return new ActividesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividesViewHolder holder, int position) {
        Actividades act = listaActividades.get(position);
        holder.actividades = act;

        TextView tvNombreActividad = holder.itemView.findViewById(R.id.textViewNombreActividad_dg);
        TextView tvEstado = holder.itemView.findViewById(R.id.textViewEstadoActiv_dg);
        TextView tvDelegadoAsignado = holder.itemView.findViewById(R.id.textViewDeleActiv_dg);
        Button buttonAsiganarDelegado = holder.itemView.findViewById(R.id.buttomAddDelegado_dg);

        tvNombreActividad.setText(act.getNombre());
        tvEstado.setText("• "+act.getEstado());
        if(act.getUsuario()!=null){
            tvDelegadoAsignado.setText("Delegado: "+act.getUsuario().getNombre()+' '+act.getUsuario().getApellido());
            buttonAsiganarDelegado.setEnabled(false);
            buttonAsiganarDelegado.setBackgroundColor(Color.LTGRAY);
            buttonAsiganarDelegado.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }


    //SubClaseViewHolder
    public class ActividesViewHolder extends RecyclerView.ViewHolder{
        Actividades actividades;
        public ActividesViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    //Encapsulamiento
    public List<Actividades> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<Actividades> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
