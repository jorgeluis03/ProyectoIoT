package com.example.proyecto_iot.alumno.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoDonacionConsultaActivity;
import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Entities.Notificacion;
import com.example.proyecto_iot.alumno.Fragments.AlumnoDonacionesFragment;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.List;

public class ListaNotificacionesAdapter extends RecyclerView.Adapter<ListaNotificacionesAdapter.NotificacionViewHolder>{
    private List<Notificacion> notificacionList;
    private Context context;
    private String userUid = FirebaseAuth.getInstance().getUid();

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_alumno_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notificacion notificacion = notificacionList.get(position);
        holder.notificacion = notificacion;

        TextView textNotificacion = holder.itemView.findViewById(R.id.textNotificacion);
        TextView textHora = holder.itemView.findViewById(R.id.textHora);
        textNotificacion.setText(notificacion.getTexto());
        textHora.setText(notificacion.horaFromNow());
    }

    @Override
    public int getItemCount() {
        return notificacionList.size();
    }

    public class NotificacionViewHolder extends RecyclerView.ViewHolder{
        Notificacion notificacion;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);

            ConstraintLayout constraintLayout = itemView.findViewById(R.id.rvNotificacion);
            constraintLayout.setOnClickListener(view -> {
                Intent intent = null;
                switch (notificacion.getTipo()){
                    case "deleteEvento":
                        intent = new Intent(context, AlumnoInicioActivity.class);
                        break;
                    case "updateEvento": //update de evento, categoria de apoyo y nueva foto
                        intent = new Intent(context, AlumnoEventoActivity.class);
                        intent.putExtra("evento", notificacion.getEvento());
                        intent.putExtra("userUid", userUid);
                        break;
                    case "donateAccept":
                        DecimalFormat df = new DecimalFormat("#0.00");
                        String montoFormateado = df.format(Double.parseDouble(notificacion.getDonacion().getMonto()));
                        intent = new Intent(context, AlumnoDonacionConsultaActivity.class);
                        intent.putExtra("nombreDonacion", notificacion.getDonacion().getNombre());
                        intent.putExtra("horaDonacion", notificacion.getDonacion().getHora());
                        intent.putExtra("montoDonacion",montoFormateado);
                        intent.putExtra("fechaDonacion",notificacion.getDonacion().getFecha());
                        intent.putExtra("rolDonacion", notificacion.getDonacion().getRol());
                        intent.putExtra("codigoAlumno", notificacion.getCodigoAlumno());
                        break;
                }
                context.startActivity(intent);
            });
        }
    }

    public List<Notificacion> getNotificacionList() {
        return notificacionList;
    }

    public void setNotificacionList(List<Notificacion> notificacionList) {
        this.notificacionList = notificacionList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
