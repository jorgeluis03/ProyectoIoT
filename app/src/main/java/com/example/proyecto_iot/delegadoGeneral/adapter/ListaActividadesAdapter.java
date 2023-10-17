package com.example.proyecto_iot.delegadoGeneral.adapter;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Dialog;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.delegadoGeneral.CrearActividadActivity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.fragmentos.Dg_actividadesFragment;

import java.util.List;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividesViewHolder> {
    private List<Actividades> listaActividades;
    private Context context;

    public ListaActividadesAdapter(List<Actividades> listaActividades, Context context) {
        this.listaActividades = listaActividades;
        this.context = context;
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
        TextView tvEstado = holder.itemView.findViewById(R.id.textViewEstadoActiv_dg);
        TextView tvDelegadoAsignado = holder.itemView.findViewById(R.id.textViewDeleActiv_dg);
        Button buttonAsiganarDelegado = holder.itemView.findViewById(R.id.buttomAddDelegado_dg);

        tvNombreActividad.setText(act.getNombre());
        tvEstado.setText("• "+act.getEstado());
        /*if(act.getUsuario()!=null){
            tvDelegadoAsignado.setText("Delegado: "+act.getUsuario().getNombre()+' '+act.getUsuario().getApellido());
            buttonAsiganarDelegado.setEnabled(false);
            buttonAsiganarDelegado.setBackgroundColor(Color.LTGRAY);
            buttonAsiganarDelegado.setVisibility(View.GONE);
        }*/



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

            Button buttonAsiganrDele = itemView.findViewById(R.id.buttomAddDelegado_dg);
            buttonAsiganrDele.setOnClickListener(view -> {
                Integer id = actividades.getId();
                Log.d("msg-test",String.valueOf(id));
                showDialog();

            });

            Button buttonEditar = itemView.findViewById(R.id.buttonEditarActivi);
            buttonEditar.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Log.d("msg-pos",String.valueOf(position));
                Intent intent = new Intent(context, CrearActividadActivity.class);
                intent.putExtra("position",String.valueOf(getAdapterPosition()));
                intent.putExtra("nameActAntiguo",actividades.getNombre());
                context.startActivity(intent);

            });

        }
    }

    public void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buttomsheetlayout_dg);

        LinearLayout uploadVideo = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shorts = dialog.findViewById(R.id.layoutShorts);
        LinearLayout live = dialog.findViewById(R.id.layoutLive);

        uploadVideo.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(context,"hola 1",Toast.LENGTH_SHORT).show();
        });
        shorts.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(context,"hola 2",Toast.LENGTH_SHORT).show();
        });
        live.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(context,"hola 3",Toast.LENGTH_SHORT).show();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
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
