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
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListaEventosBusquedaAdapter extends FirestoreRecyclerAdapter<Evento, ListaEventosBusquedaAdapter.ViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUid = FirebaseAuth.getInstance().getUid();
    private Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ListaEventosBusquedaAdapter(@NonNull FirestoreRecyclerOptions<Evento> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Evento evento) {
        holder.evento = evento;

        float density = context.getResources().getDisplayMetrics().density;
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_delegado_actividad);
        dialog.getWindow().setLayout((int) (320*density), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.shape_user_dialog));

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
                        String nombreDelegado = alumno.getNombre() + " " + alumno.getApellidos();
                        if (nombreDelegado.length() > 25){
                            nombreDelegado = nombreDelegado.substring(0,25).trim()+" ...";
                        }

                        textDelegado.setText(nombreDelegado);

                        //conf de dialog
                        TextView textNombreDelegadoDialog = dialog.findViewById(R.id.textNombreDelegadoActividad);
                        TextView textCodigoDelegadoDialog = dialog.findViewById(R.id.textCodigoDelegadoActividad);
                        TextView textCorreoDelegadoDialog = dialog.findViewById(R.id.textCorreoDelegadoActividad);
                        ImageView imageDelegadoDialog = dialog.findViewById(R.id.imageDelegadoActividad);

                        textNombreDelegadoDialog.setText(nombreDelegado);
                        textCodigoDelegadoDialog.setText(alumno.getCodigo());
                        textCorreoDelegadoDialog.setText(alumno.getCorreo());

                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
                        Glide.with(context)
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
        Glide.with(context)
                .load(fotoUrl)
                .apply(requestOptions)
                .into(imageEvento);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_alumno_evento, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Evento evento;
        public ViewHolder(@NonNull View itemView) {
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

}
