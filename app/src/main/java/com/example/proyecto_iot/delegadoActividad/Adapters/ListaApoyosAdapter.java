package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListaApoyosAdapter extends RecyclerView.Adapter<ListaApoyosAdapter.ApoyoViewHolder>{
    private List<ApoyoDto> apoyos;
    private Context context;
    FirebaseFirestore db;

    @NonNull
    @Override
    public ListaApoyosAdapter.ApoyoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.rv_da_apoyos, parent, false);
        return new ApoyoViewHolder(view);
    }

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
            MaterialSwitch materialSwitch = itemView.findViewById(R.id.materialSwitch);
            TextView nombre = itemView.findViewById(R.id.nombreApoyo);
            materialSwitch.setOnClickListener(view ->{
                Log.d("msg-test","Switch: "+materialSwitch.isChecked());
                if (materialSwitch.isChecked()){
                    mostrarConfirmacionDialog(true, nombre.getText().toString(),itemView, materialSwitch, apoyo);
                }else {
                    mostrarConfirmacionDialog(false, nombre.getText().toString(),itemView, materialSwitch, apoyo);
                }
            });
            Button delete = itemView.findViewById(R.id.buttonDelete2);
            delete.setOnClickListener(view ->{
                dialogDelete(apoyo, itemView);
            });
        }
    }

    private void dialogDelete(ApoyoDto apoyo, View itemView) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Eliminar apoyo");
        alertDialog.setMessage("¿Desea eliminar al alumno "+apoyo.getAlumno().getNombre()+" de la lista de apoyos? Este cambio es irreversible, aunque el alumno podrá volver a apoyar el evento.");
        alertDialog.setPositiveButton("Eliminar", (dialogInterface, i) -> {
            actualizarApoyoDeleteEnDb(apoyo, itemView);
        });
        alertDialog.setNegativeButton("Regresar", (dialogInterface, i) -> {
        });
        alertDialog.show();
    }

    private void actualizarApoyoDeleteEnDb(ApoyoDto apoyo, View view) {
        db = FirebaseFirestore.getInstance();
        db.collection("alumnos").document(apoyo.getAlumno().getId())
                .collection("eventos").document(apoyo.getEventoId())
                .delete()
                .addOnSuccessListener(unused -> {})
                .addOnFailureListener(e -> {});
        db.collection("eventos").document(apoyo.getEventoId())
                .collection("apoyos").document(apoyo.getAlumno().getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    apoyos.remove(apoyo);
                    notifyDataSetChanged();
                    Snackbar.make(view, "Se eliminó al alumno "+apoyo.getAlumno().getNombre()+" de la lista de apoyos.", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //TODO: volver a agregar el documento de evento dentro de la coleccion alumno/eventos
                    Snackbar.make(view, "No se pudo eliminar a "+apoyo.getAlumno().getNombre()+" de la lista de apoyos.", Snackbar.LENGTH_SHORT).show();
                });
    }

    private Drawable getLocalDrawable(int id){
        Drawable d = ContextCompat.getDrawable(context, id); // Reemplaza "nuevo_icono" con el nombre de tu nuevo icono
        return d;
    }
    private void mostrarConfirmacionDialog(boolean b, String nombre, View itemView, MaterialSwitch materialSwitch, ApoyoDto apoyo){
        String cambioApoyo;

        ImageButton jugador = itemView.findViewById(R.id.buttonJugador);
        ImageButton barra = itemView.findViewById(R.id.buttonBarra);
        ColorStateList colorBat = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verde_bat));
        ColorStateList white = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white));

        if (b){
            cambioApoyo = "¿Está seguro de que desea asignar a "+nombre+" como parte del equipo?";
        }else {
            cambioApoyo = "¿Está seguro de que desea asignar a "+nombre+" como parte de la barra?";
        }
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Confirmación");
        alertDialog.setMessage(cambioApoyo);
        alertDialog.setPositiveButton("Sí", (dialogInterface, i) -> {
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
            actualizarApoyoEnDb(apoyo, b, itemView);
        });
        alertDialog.setNegativeButton("Regresar", (dialogInterface, i) -> {
            materialSwitch.setChecked(!b);
        });
        alertDialog.setOnCancelListener(dialogInterface -> {
            Log.d("msg-test","Estado Cancel");
            materialSwitch.setChecked(!b);
        });
        alertDialog.show();
    }

    private void actualizarApoyoEnDb(ApoyoDto apoyo, Boolean b, View view) {
        String categoria;
        if (b){
            categoria = "equipo";
        }else {
            categoria = "barra";
        }
        db = FirebaseFirestore.getInstance();
        db.collection("eventos").document(apoyo.getEventoId())
                .collection("apoyos").document(apoyo.getAlumno().getId())
                .update("categoria",categoria)
                .addOnSuccessListener(unused -> {
                    for (int i = 0; i< apoyos.size(); i++){
                        if (apoyos.get(i).getAlumno().getId().equals(apoyo.getAlumno().getId())){
                            apoyos.get(i).setCategoria(categoria);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    Snackbar.make(view, "Se actualizó la categoría de "+apoyo.getAlumno().getNombre()+" a "+ categoria, Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    notifyDataSetChanged();
                    Snackbar.make(view, "Ocurrió un error durante la actualizacoión de "+apoyo.getAlumno().getNombre()+". Inténtelo más tarde.", Snackbar.LENGTH_SHORT).show();
                });
    }
}
