package com.example.proyecto_iot.alumno.RecyclerViews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaEventosAdapter extends RecyclerView.Adapter<ListaEventosAdapter.EventoViewHolder> {
    private List<Evento> eventoList;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_alumno_evento, parent, false);
        return new EventoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventoList.get(position);
        holder.evento = evento;

        float density = context.getResources().getDisplayMetrics().density;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delegado_actividad);
        dialog.getWindow().setLayout((int) (320*density), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.shape_user_dialog));

        TextView textDelegado = holder.itemView.findViewById(R.id.textDelegadoActividad);
        TextView textEstado = holder.itemView.findViewById(R.id.textEstado);
        TextView textTitulo = holder.itemView.findViewById(R.id.textTitulo);
        TextView textActividad = holder.itemView.findViewById(R.id.textActividad);
        TextView textDescripcion = holder.itemView.findViewById(R.id.textDescripcion);
        TextView textFecha = holder.itemView.findViewById(R.id.textFecha);
        TextView textHora = holder.itemView.findViewById(R.id.textHora);
        ImageView imageEvento = holder.itemView.findViewById(R.id.imageEvento);
        TextView lugar = holder.itemView.findViewById(R.id.textLugar);

        lugar.setText(evento.getLugar());
        if (evento.getEstado().equals("inactivo")) { // si está finalizado se muestra mensaje
            Log.d("msg-test",evento.getTitulo()+" se encuentra "+evento.getEstado());
            textEstado.setVisibility(View.VISIBLE);
        }

        db.collection("alumnos")
                .document(evento.getDelegado())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Alumno alumno = task.getResult().toObject(Alumno.class);
                        textDelegado.setText(alumno.getNombre() + " " + alumno.getApellidos());

                        //conf de dialog
                        TextView textNombreDelegadoDialog = dialog.findViewById(R.id.textNombreDelegadoActividad);
                        TextView textCodigoDelegadoDialog = dialog.findViewById(R.id.textCodigoDelegadoActividad);
                        TextView textCorreoDelegadoDialog = dialog.findViewById(R.id.textCorreoDelegadoActividad);
                        ImageView imageDelegadoDialog = dialog.findViewById(R.id.imageDelegadoActividad);

                        textNombreDelegadoDialog.setText(alumno.getNombre()+" "+alumno.getApellidos());
                        textCodigoDelegadoDialog.setText(alumno.getCodigo());
                        textCorreoDelegadoDialog.setText(alumno.getCorreo());

                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
                        Glide.with(getContext())
                                .load(alumno.getFotoUrl())
                                .apply(requestOptions)
                                .into(imageDelegadoDialog);

                        textDelegado.setOnClickListener(view -> {
                            dialog.show();
                        });

                    } else {
                        Log.d("msg-test", "error buscando delegado de evento: " + task.getException().getMessage());
                    }
                });

        textTitulo.setText(evento.getTitulo());
        textActividad.setText(evento.getActividad());
        textDescripcion.setText(evento.getDescripcion());
        textFecha.setText(evento.getFecha());
        textHora.setText(evento.getHora());

        String fotoUrl = evento.getFotoUrl();
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
        Glide.with(getContext())
                .load(fotoUrl)
                .apply(requestOptions)
                .into(imageEvento);
    }

    @Override
    public int getItemCount() {
        return eventoList.size();
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {

        Evento evento;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            ConstraintLayout constraintLayout = itemView.findViewById(R.id.evento);
            constraintLayout.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), AlumnoEventoActivity.class);
                intent.putExtra("evento", evento);
                Log.d("msg-test","delegado de evento: "+evento.getDelegado());
                intent.putExtra("userUid", userUid);
                itemView.getContext().startActivity(intent);
            });
        }
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
