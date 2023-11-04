package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.proyecto_iot.delegadoActividad.Entities.Actividad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaEventosActividadesAdapter extends RecyclerView.Adapter<ListaEventosActividadesAdapter.EventoAViewHolder> {

    private List<Evento> eventoAList;
    ArrayList<Alumno> apoyos = new ArrayList<>();

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
        public EventoAViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView textView = itemView.findViewById(R.id.textActividad2);
            Button participantes = itemView.findViewById(R.id.buttonParticipantes);
            participantes.setOnClickListener(view -> {
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
                mostrarConfirmacionDialog(textView.getText().toString());

            });
            Button editar = itemView.findViewById(R.id.buttonEdit);
            editar.setOnClickListener(view -> {

            });
        }
    }

    private void cargarAdapter(Evento evento) {
        String name = "eventoWed Nov 01 23:54:25 GMT-05:00 2023";
        db.collection("eventos")
                .document(name)
                .collection("apoyos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("msg-test", "busqueda apoyos ok: "+task.getResult().size());
                            for (QueryDocumentSnapshot document: task.getResult()){
                                buscarAlumno(document.getId());
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d("msg-test", "AlumnoEventoActivity: error al buscar evento");
                        }
                    }
                });
    }

    private void buscarAlumno(String alumnoId){
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
                                apoyos.add(alumno);
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

    private void mostrarConfirmacionDialog(String nombre){
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setTitle("Eliminar evento");
        alertDialog.setMessage("¿Está seguro que desea eliminar el evento \""+nombre+"\"? Este cambio será permanente.");
        alertDialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarEvento();
            }
        });
        alertDialog.setNeutralButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }
    private void eliminarEvento() {
        //TODO eliminar evento de apoyos de alumnos
        //eliminar evento de actividad
        //eliminar evento de eventos
    }

}
