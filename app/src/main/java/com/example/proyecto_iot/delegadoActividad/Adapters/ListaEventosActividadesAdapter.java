package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Fragments.AlumnoApoyandoButtonFragment;
import com.example.proyecto_iot.alumno.Fragments.AlumnoApoyarButtonFragment;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.delegadoActividad.DaEditEventoActivity;
import com.example.proyecto_iot.delegadoActividad.Entities.Actividad;
import com.example.proyecto_iot.delegadoActividad.Entities.ApoyoDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaEventosActividadesAdapter extends RecyclerView.Adapter<ListaEventosActividadesAdapter.EventoAViewHolder> {

    private List<Evento> eventoAList;
    ApoyoDto apoyo;
    ArrayList<ApoyoDto> apoyos = new ArrayList<>();

    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListaApoyosAdapter adapter = new ListaApoyosAdapter();

    @NonNull
    @Override
    public EventoAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_da_actividad, parent, false);
        return new EventoAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAViewHolder holder, int position){
        Evento evento = eventoAList.get(position);
        holder.evento = evento;

        TextView textTitulo = holder.itemView.findViewById(R.id.textActividad2);
        TextView textFecha = holder.itemView.findViewById(R.id.textHoraAct);

        String date = evento.getFecha() + " " + evento.getHora();
        textTitulo.setText(evento.getTitulo());
        textFecha.setText(date);
    }

    public int getItemCount(){
        return eventoAList.size();
    }
    public class EventoAViewHolder extends RecyclerView.ViewHolder{
        Evento evento;
        Context context;
        public EventoAViewHolder(@NonNull View itemView) {
            super(itemView);
            Button participantes = itemView.findViewById(R.id.buttonParticipantes);
            participantes.setOnClickListener(view -> {
                apoyos = new ArrayList<>();

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_da_apoyos, (LinearLayout) view.findViewById(R.id.dialogListApoyos));
                cargarAdapter(evento);

                adapter.setContext(getContext());
                adapter.setApoyos(apoyos);

                RecyclerView recyclerView = bottomSheetView.findViewById(R.id.rv_apoyos_list);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();
            });
            Button borrar = itemView.findViewById(R.id.buttonDelete);
            borrar.setOnClickListener(view -> {
                mostrarConfirmacionDeleteDialog(evento, itemView);
            });
            Button editar = itemView.findViewById(R.id.buttonEdit);
            editar.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), DaEditEventoActivity.class);
                view.getContext().startActivity(intent);
            });
        }
    }

    private void cargarAdapter(Evento evento) {
        String name = "evento"+evento.getFechaHoraCreacion().toString();
        Log.d("msg-test", evento.getFechaHoraCreacion().toString());
        db.collection("eventos")
                .document(name)
                .collection("apoyos")
                .orderBy("codigo", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("msg-test", "busqueda apoyos ok: "+task.getResult().size());
                            for (QueryDocumentSnapshot document: task.getResult()){
                                apoyo = new ApoyoDto();
                                apoyo.setCategoria(document.getString("categoria"));
                                apoyo.setEventoId(name);
                                buscarAlumno(document.getId(), apoyo);
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d("msg-test", "AlumnoEventoActivity: error al buscar evento");
                        }
                    }
                });
    }

    private void buscarAlumno(String alumnoId, ApoyoDto apoyo){
        db.collection("alumnos")
                .document(alumnoId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Alumno alumno = task.getResult().toObject(Alumno.class);
                            Log.d("msg-test", "apoyO encontrado: "+alumno.getNombre());
                            if (alumno.getEstado().equals("activo")){
                                apoyo.setAlumno(alumno);
                                apoyos.add(apoyo);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            Log.d("msg-test", "ListaApoyos error buscando apoyo: "+alumnoId);
                        }
                    }
                });
    }

    public List<Evento> getEventoAList(){return eventoAList;}
    public void setEventoAList(List<Evento> eventoAList){this.eventoAList = eventoAList;}
    public Context getContext() {return context;}

    public void setContext(Context context){this.context = context;}

    private void mostrarConfirmacionDeleteDialog(Evento evento, View itemView){
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Eliminar evento");
        alertDialog.setMessage("¿Está seguro que desea eliminar el evento \""+evento.getTitulo()+"\"? Este cambio será permanente y se notificará a los que estén interesados en el evento.");
        alertDialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarEvento(evento, itemView);
            }
        });
        alertDialog.setNeutralButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }
    private void eliminarEvento(Evento evento, View itemView) {
        db = FirebaseFirestore.getInstance();
        //eliminando evento de 'apoyos' del alumno
        db.collection("alumnos").whereArrayContains("eventos",evento.getFechaHoraCreacion().toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                        String alumnoId = document.getId();
                        db.collection("alumnos")
                                .document(alumnoId)
                                .collection("eventos")
                                .document(evento.getFechaHoraCreacion().toString())
                                .delete()
                                .addOnSuccessListener(aVoid -> {})
                                .addOnFailureListener(e -> {});
                    }
                });
        //eliminando evento de actividades
        db.collection("actividades").document(evento.getActividad())
                .collection("eventos").document(evento.getFechaHoraCreacion().toString())
                .delete()
                .addOnSuccessListener(unused -> {})
                .addOnFailureListener(e -> {});
        //eliminando evento de eventos
        db.collection("eventos").document(evento.getFechaHoraCreacion().toString())
                .delete()
                .addOnSuccessListener(unused -> {
                    eventoAList.remove(evento);
                    notifyDataSetChanged();
                    Snackbar.make(itemView, "Se eliminó exitosamente el evento "+evento.getTitulo()+" de la actividad "+evento.getActividad()+".", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(itemView, "No se pudo el evento "+evento.getTitulo()+" de la actividad "+evento.getActividad()+". Intente más tarde.", Snackbar.LENGTH_SHORT).show();
                    // TODO DA: volver a escribir el evento en 'eventos' de los alumnos
                    // TODO DA: volver a escribir el evento en 'eventos' de la actividad
                });
    }

}
