package com.example.proyecto_iot.delegadoGeneral.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class ListaDelegadosAdapter extends FirestoreRecyclerAdapter<Alumno,ListaDelegadosAdapter.DelegadoViewHolder> {
    private Context context;

    public interface OnAlumnoSelectedListener {
        void onAlumnoSelected(Alumno alumnos);
    }
    private OnAlumnoSelectedListener onAlumnoSelectedListener;

    public void setOnAlumnoSelectedListener(OnAlumnoSelectedListener listener) {
        this.onAlumnoSelectedListener = listener;
    }

    public ListaDelegadosAdapter(@NonNull FirestoreRecyclerOptions<Alumno> options, Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DelegadoViewHolder holder, int position, @NonNull Alumno model) {
        holder.tv_nombre.setText(model.getNombre()+' '+model.getApellidos());
        holder.tv_correo.setText(model.getCorreo());
    }

    @NonNull
    @Override
    public DelegadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_dg_asignar_delegados,parent,false);
        Log.d("msg-fire","hassta aca");
        return new DelegadoViewHolder(view);
    }


    //SubClase ViewHolder
    public class DelegadoViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nombre, tv_correo;
        RadioButton radioButton;
        Alumno alumno;
        public DelegadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_correo = itemView.findViewById(R.id.tv_correo);
            radioButton = itemView.findViewById(R.id.radioButtonDA);

            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && isChecked) {
                    alumno = getItem(position);
                    if (alumno != null) {
                        if (onAlumnoSelectedListener != null) {
                            onAlumnoSelectedListener.onAlumnoSelected(alumno);
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
