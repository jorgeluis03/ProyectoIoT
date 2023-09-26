package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.entity.Empleado;

import java.util.List;

public class ListaEmpleadosAdapter extends RecyclerView.Adapter<ListaEmpleadosAdapter.EmpleadoViewHolder> {
    private List<Empleado> listaEmpleados;
    private Context context;

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_dg_actividades,parent,false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        Empleado e = listaEmpleados.get(position);
        holder.empleado = e;

        TextView textViewNombreActivity = holder.itemView.findViewById(R.id.textViewNombreActividad_dg);
        TextView textViewCorreo = holder.itemView.findViewById(R.id.textViewCorreo_dg);

        textViewNombreActivity.setText(e.getFirstName()+' '+e.getLastName());
        textViewCorreo.setText(e.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder{
        Empleado empleado;
        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //Encapsulamiento
    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

