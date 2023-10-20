package com.example.proyecto_iot.delegadoGeneral.adapter;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.CrearActividadActivity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.example.proyecto_iot.delegadoGeneral.fragmentos.Dg_actividadesFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividesViewHolder> {
    private List<Actividades> listaActividades;
    private Context context;
    FirebaseFirestore db;

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
        //Button buttonAsiganarDelegado = holder.itemView.findViewById(R.id.buttomAddDelegado_dg);

        tvNombreActividad.setText(act.getNombre());
        tvEstado.setText("• "+act.getEstado());
        tvDelegadoAsignado.setText("Delegado: "+act.getDelegadoActividad().getNombre()+' '+act.getDelegadoActividad().getApellido());

            /*buttonAsiganarDelegado.setEnabled(false);
            buttonAsiganarDelegado.setBackgroundColor(Color.LTGRAY);
            buttonAsiganarDelegado.setVisibility(View.GONE);*/




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

            //editar
            Button buttonEditar = itemView.findViewById(R.id.buttonEditarActivi);
            buttonEditar.setOnClickListener(view -> {
                

            });

            //borrar
            Button buttonBorrar = itemView.findViewById(R.id.buttonEliminarActivi);
            buttonBorrar.setOnClickListener(view -> {

                new MaterialAlertDialogBuilder(context)
                        .setTitle("¡Advertencia!")
                        .setMessage("Se eliminarán todos los eventos que se encuentren en la actividad " +
                                "incluyendo aquellos que aun no finalizan.")
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Responder a la pulsación del botón neutral
                            }
                        })
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Responder a la pulsación del botón positivo
                                db = FirebaseFirestore.getInstance();
                                db.collection("actividades").document(actividades.getId())
                                        .delete()
                                        .addOnSuccessListener(unused -> {
                                            // Eliminar el usuario de la lista de datos
                                            listaActividades.remove(actividades);

                                            // Notificar al adaptador que los datos han cambiado
                                            notifyDataSetChanged();
                                            Toast.makeText(context,"Eliminado",Toast.LENGTH_SHORT).show();

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context,"Algo pasó",Toast.LENGTH_SHORT).show();

                                        });


                            }
                        })
                        .show();

            });

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
