package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.delegadoActividad.DaGestionEventosActivity;
import com.example.proyecto_iot.delegadoActividad.Entities.ApoyoDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class ListaApoyosAdapter extends RecyclerView.Adapter<ListaApoyosAdapter.ApoyoViewHolder>{
    private List<ApoyoDto> apoyos;
    private Context context;

    @NonNull
    @Override
    public ListaApoyosAdapter.ApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rv_da_apoyos, parent, false);
        return new ApoyoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListaApoyosAdapter.ApoyoViewHolder holder, int position) {
        ApoyoDto apoyo = getApoyos().get(position);
        holder.apoyo = apoyo;

        ColorStateList colorBat = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verde_bat));
        ColorStateList white = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white));

        TextView nombre = holder.itemView.findViewById(R.id.nombreApoyo);
        TextView tipo = holder.itemView.findViewById(R.id.textTipoAlumno);
        MaterialSwitch materialSwitch = holder.itemView.findViewById(R.id.materialSwitch);
        ImageButton buttonB = holder.itemView.findViewById(R.id.buttonBarra);
        ImageButton buttonJ = holder.itemView.findViewById(R.id.buttonJugador);

        nombre.setText(apoyo.getAlumno().getFullNameFormal());
        tipo.setText(apoyo.getAlumno().getCodigo()+" | "+apoyo.getAlumno().getTipo());
        if (apoyo.getCategoria().equals("barra")){
            materialSwitch.setChecked(false);
            buttonB.setImageResource(R.drawable.icon_barra_filled);
            buttonB.setImageTintList(colorBat);

            buttonJ.setImageResource(R.drawable.icon_running_outline);
            buttonJ.setImageTintList(white);
        }else {
            materialSwitch.setChecked(true);
            buttonJ.setImageResource(R.drawable.icon_running);
            buttonJ.setImageTintList(colorBat);

            buttonB.setImageResource(R.drawable.icon_barra_outline);
            buttonB.setImageTintList(white);
        }

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
    }

    @Override
    public int getItemCount() {
        return getApoyos().size();
    }

    public List<ApoyoDto> getApoyos() {
        return apoyos;
    }

    public void setApoyos(List<ApoyoDto> apoyos) {
        this.apoyos = apoyos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ApoyoViewHolder extends RecyclerView.ViewHolder{
        ApoyoDto apoyo;
        public ApoyoViewHolder(@NonNull View itemView) {
            super(itemView);
            ColorStateList colorBat = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verde_bat));
            ColorStateList white = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white));
            MaterialSwitch materialSwitch = itemView.findViewById(R.id.materialSwitch);
            ImageButton barra = itemView.findViewById(R.id.buttonBarra);
            ImageButton jugador = itemView.findViewById(R.id.buttonJugador);
            materialSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    jugador.setImageResource(R.drawable.icon_running);
                    jugador.setImageTintList(colorBat);
                    barra.setImageResource(R.drawable.icon_barra_outline);
                    barra.setImageTintList(white);
                }else {
                    jugador.setImageResource(R.drawable.icon_running_outline);
                    jugador.setImageTintList(white);
                    barra.setImageResource(R.drawable.icon_barra_filled);
                    barra.setImageTintList(colorBat);
                }
            });
        }
    }
    private Drawable getLocalDrawable(int id){
        Drawable d = ContextCompat.getDrawable(context, id); // Reemplaza "nuevo_icono" con el nombre de tu nuevo icono
        return d;
    }
}
