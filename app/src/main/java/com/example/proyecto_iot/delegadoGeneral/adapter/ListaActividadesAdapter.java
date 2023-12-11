package com.example.proyecto_iot.delegadoGeneral.adapter;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.delegadoGeneral.DgEventosPorActividadActivity;
import com.example.proyecto_iot.delegadoGeneral.EditarActividad;
import com.example.proyecto_iot.delegadoGeneral.dto.ActividadesDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividesViewHolder> {
    private List<Actividades> listaActividades;
    private Context context;
    FirebaseFirestore db;
    private ActivityResultLauncher<Intent> lunchEditar;

    public ListaActividadesAdapter(ActivityResultLauncher<Intent> lunchEditar, Context context, List<Actividades> listaActividades) {
        this.lunchEditar = lunchEditar;
        this.context =context;
        this.listaActividades = listaActividades;
    }
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
        TextView tvDelegadoAsignado = holder.itemView.findViewById(R.id.textViewDeleActiv_dg);


        tvNombreActividad.setText(act.getNombre());
        tvDelegadoAsignado.setText("Delegado: "+act.getDelegadoActividad().getNombre()+' '+act.getDelegadoActividad().getApellidos());
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
            ImageButton buttonEditar = itemView.findViewById(R.id.buttonEditarActivi);
            TextView btnVerEventos = itemView.findViewById(R.id.tv_verDetalles);

            btnVerEventos.setOnClickListener(view -> {
                Intent intent = new Intent(context, DgEventosPorActividadActivity.class);
                intent.putExtra("idActividad",actividades.getId());
                context.startActivity(intent);
            });

            buttonEditar.setOnClickListener(view -> {

                Intent intent = new Intent(context, EditarActividad.class);
                intent.putExtra("actividadActual",actividades);
                HashMap<String,Object> data = new HashMap<>();
                data.put("delegadoActividad",null);
                data.put("estado",actividades.getEstado());
                data.put("id",actividades.getId());
                data.put("nombre",actividades.getNombre());
                FirebaseUtilDg.getCollAlumnos().document(actividades.getDelegadoActividad().getId())
                        .update("actividadesId", FieldValue.arrayRemove(data));

                lunchEditar.launch(intent);

            });

            //borrar
            ImageButton buttonBorrar = itemView.findViewById(R.id.buttonEliminarActivi);
            buttonBorrar.setOnClickListener(view -> {
                Query query = FirebaseUtilDg.getColeccionEventos().whereEqualTo("actividadId",actividades.getId());

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getDocuments().size() > 0) {
                            new MaterialAlertDialogBuilder(context,R.style.DarkAlertDialog)
                                    .setTitle("¡Eliminar evento!")
                                    .setMessage("No es posible eliminar esta actividad ya que los eventos asociados " +
                                            "aun no finalizan.")
                                    .setPositiveButton("Aceptar", (dialog, which) -> {
                                    })
                                    .show();
                        } else {
                            new MaterialAlertDialogBuilder(context,R.style.DarkAlertDialog)
                                    .setTitle("¡Advertencia!")
                                    .setMessage("¿Desea eliminar este evento?")
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
                                            FirebaseUtilDg.getActividadesCollection().document(actividades.getId())
                                                    .delete()
                                                    .addOnSuccessListener(unused -> {
                                                        // Eliminar el usuario de la lista de datos
                                                        listaActividades.remove(actividades);
                                                        HashMap<String,Object> data = new HashMap<>();
                                                        data.put("delegadoActividad",null);
                                                        data.put("estado",actividades.getEstado());
                                                        data.put("id",actividades.getId());
                                                        data.put("nombre",actividades.getNombre());
                                                        FirebaseUtilDg.getCollAlumnos().document(actividades.getDelegadoActividad().getId())
                                                                .update("actividadesId", FieldValue.arrayRemove(data));
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
                        }
                    } else {
                        Log.e("msg-test", "Error al obtener eventos", task.getException());
                    }
                });



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
