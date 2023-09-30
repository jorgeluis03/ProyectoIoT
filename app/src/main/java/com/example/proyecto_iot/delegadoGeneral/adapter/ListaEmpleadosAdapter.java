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

    public ListaEmpleadosAdapter() {
        // Constructor vacío
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_actividades, parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        if (listaEmpleados == null || listaEmpleados.isEmpty()) {
            return;
        }

        Empleado e = listaEmpleados.get(position);
        holder.empleado = e;

        holder.textViewNombreActivity.setText(e.getJobId() + ". " + e.getFirstName() + ' ' + e.getLastName());
        holder.textViewNumeroTelefono.setText(e.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return listaEmpleados != null ? listaEmpleados.size() : 0;
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        Empleado empleado;
        TextView textViewNombreActivity;
        TextView textViewNumeroTelefono;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreActivity = itemView.findViewById(R.id.textViewNombreActividad_dg);
            textViewNumeroTelefono = itemView.findViewById(R.id.textViewNumeroTelefono_dg);
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

