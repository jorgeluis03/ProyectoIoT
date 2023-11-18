package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class ListaBuscarDelegadoAdapter extends FirestoreRecyclerAdapter<Alumno,ListaBuscarDelegadoAdapter.DelegadoBuscarViewHolder> {
    private Context context;

    public interface OnAlumnoBuscarSelectedListener {
        void onAlumnoBuscarSelected(Alumno alumnos);
    }
    private OnAlumnoBuscarSelectedListener onAlumnoBuscarSelectedListener;

    public void setOnAlumnoBuscarSelectedListener(OnAlumnoBuscarSelectedListener listener) {
        this.onAlumnoBuscarSelectedListener = listener;
    }
    public ListaBuscarDelegadoAdapter(@NonNull FirestoreRecyclerOptions<Alumno> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DelegadoBuscarViewHolder holder, int position, @NonNull Alumno model) {
        holder.tv_nombre.setText(model.getNombre()+' '+model.getApellidos());
        holder.tv_correo.setText(model.getCorreo());
    }

    @NonNull
    @Override
    public DelegadoBuscarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_asignar_delegados,parent,false);
        return new DelegadoBuscarViewHolder(view);
    }


    class DelegadoBuscarViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nombre, tv_correo;
        CheckBox checkBox;
        Alumno alumno;

        public DelegadoBuscarViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_correo = itemView.findViewById(R.id.tv_correo);
            checkBox = itemView.findViewById(R.id.checkboxDA);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && isChecked) {
                    alumno = getItem(position);
                    if (alumno != null) {
                        if (onAlumnoBuscarSelectedListener != null) {
                            onAlumnoBuscarSelectedListener.onAlumnoBuscarSelected(alumno);
                        }
                        //Log.d("msg-test", "Alumno seleccionado en el adapter: " + alumno.getNombre() + ' ' + alumno.getApellidos());
                    }
                }
            });

        }
    }

    //Encapsulamiento
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
