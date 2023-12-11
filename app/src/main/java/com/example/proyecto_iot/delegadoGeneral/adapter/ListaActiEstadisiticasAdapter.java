package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListaActiEstadisiticasAdapter extends FirestoreRecyclerAdapter<Actividades, ListaActiEstadisiticasAdapter.ActiViewHolder> {
    Context context;
    int suma = 0;
    int apoyos;

    public ListaActiEstadisiticasAdapter(@NonNull FirestoreRecyclerOptions<Actividades> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActiViewHolder holder, int position, @NonNull Actividades model) {
        holder.nombreActividad.setText(model.getNombre());
        FirebaseUtilDg.getColeccionEventos().whereEqualTo("actividadId",model.getId())
                        .get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    FirebaseUtilDg.getColeccionEventos().document(documentSnapshot.getId()).collection("apoyos")
                                            .get().addOnCompleteListener(task1 -> {
                                                if(task1.isSuccessful()){
                                                    apoyos =task1.getResult().getDocuments().size();

                                                }
                                            });
                                }
                                suma+=apoyos;

                            }
                });

        holder.cantApoyo.setText(String.valueOf(suma)+" participantes");
    }

    @NonNull
    @Override
    public ActiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_estadisticas,parent,false);
        return new ActiViewHolder(view);
    }

    class ActiViewHolder extends RecyclerView.ViewHolder{
        TextView nombreActividad, cantApoyo;

        public ActiViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreActividad = itemView.findViewById(R.id.tvNombreActividad);
            cantApoyo = itemView.findViewById(R.id.tv_apoyos);

        }
    }

}
