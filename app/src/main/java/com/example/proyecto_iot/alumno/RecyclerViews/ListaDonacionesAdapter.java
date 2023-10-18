package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoDonacionConsultaActivity;
import com.example.proyecto_iot.alumno.Entities.Donacion;

import java.util.List;

public class ListaDonacionesAdapter extends RecyclerView.Adapter<ListaDonacionesAdapter.DonacionViewHolder> {

    public interface OnButtonClickListener {
        void onButtonClick(Donacion donacion);
    }

    private List<Donacion> donacionList;
    private Context context;
    private OnButtonClickListener listener;
    private String codigoAlumno;
    private double donacionesTotales = 0.0;
    public ListaDonacionesAdapter(Context context, List<Donacion> donacionList,String codigoAlumno, OnButtonClickListener listener) {
        this.context = context;
        this.donacionList = donacionList;
        this.listener = listener;
        this.codigoAlumno = codigoAlumno;
    }

    @NonNull
    @Override
    public DonacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_alumno_donacion, parent, false);
        return new DonacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonacionViewHolder holder, int position) {
        Donacion donacion = donacionList.get(position);
        holder.bind(donacion, listener);

        String donacionString = donacion.getDonacion();
        donacionString = donacionString.replaceAll("S/", "").trim();
        // Sumar la donación actual a las donaciones totales
        donacionesTotales += Double.parseDouble(donacionString);

    }

    @Override
    public int getItemCount() {
        return donacionList.size();
    }

    public class DonacionViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreDonacion;
        TextView textHora;
        TextView textDonacion;
        Button button6;

        public DonacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreDonacion = itemView.findViewById(R.id.textNombreDonacion);
            textHora = itemView.findViewById(R.id.textHora);
            textDonacion = itemView.findViewById(R.id.textDonacion);
            button6 = itemView.findViewById(R.id.button6);
        }

        public void bind(final Donacion donacion, final OnButtonClickListener listener) {
            textDonacion.setText(donacion.getDonacion());
            String donacionHoraConcatenada = donacion.getFecha() + " " + donacion.getHora();
            textNombreDonacion.setText(donacion.getTexto());
            textHora.setText(donacionHoraConcatenada);

            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onButtonClick(donacion);
                    }
                    // Añadir esta parte para abrir la nueva actividad
                    Intent intent = new Intent(context, AlumnoDonacionConsultaActivity.class);
                    // Pasar datos al Intent. Puedes pasar cualquier dato primitivo: int, String, etc.
                    intent.putExtra("nombreDonacion", donacion.getTexto());
                    intent.putExtra("horaDonacion", donacion.getHora());
                    intent.putExtra("montoDonacion", donacion.getDonacion());
                    intent.putExtra("fechaDonacion",donacion.getFecha());
                    intent.putExtra("rolDonacion", donacion.getRol());
                    intent.putExtra("codigoAlumno", codigoAlumno);
                    intent.putExtra("donacionesTotales", String.valueOf(donacionesTotales));
                    context.startActivity(intent);
                }
            });
        }
    }
}
