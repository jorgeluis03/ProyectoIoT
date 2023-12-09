package com.example.proyecto_iot.delegadoActividad.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.delegadoActividad.DaEditEventoActivity;
import com.example.proyecto_iot.delegadoActividad.Entities.ApoyoDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaEventosActividadesAdapter extends RecyclerView.Adapter<ListaEventosActividadesAdapter.EventoAViewHolder> {

    private List<Evento> eventoAList;
    ApoyoDto apoyo;
    ArrayList<ApoyoDto> apoyos = new ArrayList<>();
    public ArrayList<Actividades> actividadList = new ArrayList<>();
    public ArrayList<Actividades> actividadesList = new ArrayList<>();
    private Actividades currentActividad;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ListaApoyosAdapter adapter = new ListaApoyosAdapter();
    ProgressBar progressBar;
    TextView sinApoyo;

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
                progressBar = bottomSheetView.findViewById(R.id.progressBarDialog);
                sinApoyo = bottomSheetView.findViewById(R.id.textView15);
                progressBar.setVisibility(View.VISIBLE);
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
                intent.putExtra("evento", evento);
                view.getContext().startActivity(intent);
            });
        }
    }

    private void cargarAdapter(Evento evento) {
        String name = "evento"+evento.getFechaHoraCreacion().toString();
        Log.d("msg-test", evento.getFechaHoraCreacion().toString());
        List<Task<?>> tasks = new ArrayList<>();
        db.collection("eventos")
                .document(name)
                .collection("apoyos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("msg-test", "busqueda apoyos ok: "+task.getResult().size());
                        for (QueryDocumentSnapshot document: task.getResult()){
                            apoyo = new ApoyoDto();
                            apoyo.setCategoria(document.getString("categoria"));
                            apoyo.setEventoId(name);
                            tasks.add(buscarAlumno(document.getId(), apoyo));
                        }
                        Log.d("msg-test", "busqueda apoyos ok1: "+apoyos.size());

                        Tasks.whenAllComplete(tasks)
                                .addOnCompleteListener(allTasks -> {
                                    progressBar.setVisibility(View.GONE);
                                    if (apoyos.isEmpty()){
                                        sinApoyo.setVisibility(View.VISIBLE);
                                    }else {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    } else {
                        Log.d("msg-test", "AlumnoEventoActivity: error al buscar evento");
                    }
                });
    }

    private Task<Void> buscarAlumno(String alumnoId, ApoyoDto apoyo){
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        db.collection("alumnos")
                .document(alumnoId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Alumno alumno = task.getResult().toObject(Alumno.class);
                        Log.d("msg-test", "apoyO encontrado: "+alumno.getNombre());
                        if (alumno.getEstado().equals("activo")){
                            apoyo.setAlumno(alumno);
                            apoyos.add(apoyo);
                        }
                    }
                    else{
                        Log.d("msg-test", "ListaApoyos error buscando apoyo: "+alumnoId);
                    }
                    taskCompletionSource.setResult(null);
                });
        return taskCompletionSource.getTask();
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
    public void eliminarEvento(Evento evento, View itemView) {
        db = FirebaseFirestore.getInstance();
        //eliminando evento de 'apoyos' del alumno
        db.collection("alumnos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                        String alumnoId = document.getId();
                        db.collection("alumnos")
                                .document(alumnoId)
                                .collection("eventos")
                                .document("evento"+evento.getFechaHoraCreacion().toString())
                                .delete()
                                .addOnSuccessListener(aVoid -> {})
                                .addOnFailureListener(e -> {});
                    }
                });
        //eliminando evento de actividades
        db.collection("actividades").document(evento.getActividadId())
                .collection("eventos").document("evento"+evento.getFechaHoraCreacion().toString())
                .delete()
                .addOnSuccessListener(unused -> {Log.d("msg-test", "eliminado de actividades");})
                .addOnFailureListener(e -> {Log.d("msg-test", "no se pudo eliminar de actividades");});
        //eliminando fotos de eventos
        db.collection("eventos").document("evento"+evento.getFechaHoraCreacion()).collection("fotos")
            .get()
                .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                } else {
                    Log.d("msg-test", "Error al obtener documentos de la subcolección 'fotos': "+ task.getException());
                }
        });
        db.collection("eventos").document("evento"+evento.getFechaHoraCreacion()).collection("fotos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            document.getReference().delete();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("msg-test", "Error al eliminar la subcolección 'fotos': "+ e);
                });

        //eliminando fotos de evento en storage
        storage.getReference().child("evento"+evento.getFechaHoraCreacion()).listAll()
            .addOnSuccessListener(listResult  -> {
                for (StorageReference item : listResult.getItems()) {
                    item.delete().addOnSuccessListener(aVoid -> {
                        // Archivo eliminado con éxito
                        Log.d("msg-test", "Archivo eliminado con éxito: " + item.getName());
                    }).addOnFailureListener(exception -> {
                        // Error al intentar eliminar el archivo
                        Log.d("msg-test", "Error al eliminar el archivo: " + item.getName() + exception);
                    });
                }
            });
        //eliminando foto main de evento en storage
        storage.getReference().child("eventos/evento"+evento.getFechaHoraCreacion()+".jpg")
            .delete()
            .addOnSuccessListener(unused -> {
                Log.d("msg-test", "Foto eliminada con éxito: " + evento.getTitulo());
            });
        //eliminando apoyos de eventos
        db.collection("eventos").document("evento"+evento.getFechaHoraCreacion()).collection("apoyos")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //TODO DA: notificacion de que se eliminó el evento
                            document.getReference().delete();
                        }
                        // Después de eliminar todos los documentos, elimina la subcolección
                        db.collection("eventos").document("evento"+evento.getFechaHoraCreacion()).collection("apoyos")
                                .document("dummy") // Cualquier documento ficticio
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // La subcolección "fotos" ha sido eliminada
                                    Log.d("msg-test", "Subcolección 'fotos' eliminada con éxito.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("msg-test", "Error al eliminar la subcolección 'fotos': "+ e);
                                });
                    } else {
                        Log.d("msg-test", "Error al obtener documentos de la subcolección 'fotos': "+ task.getException());
                    }
                });
        //eliminando evento de eventos
        db.collection("eventos").document("evento"+evento.getFechaHoraCreacion().toString())
                .delete()
                .addOnSuccessListener(unused -> {
                    eventoAList.remove(evento);
                    notifyDataSetChanged();
                    Snackbar.make(itemView, "Se eliminó exitosamente el evento "+evento.getTitulo()+" de la actividad "+evento.getActividad()+".", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(itemView, "No se pudo el evento "+evento.getTitulo()+" de la actividad "+evento.getActividad()+". Intente más tarde.", Snackbar.LENGTH_SHORT).show();
                    // TODO DA*: volver a escribir el evento en 'eventos' de los alumnos
                    // TODO DA*: volver a escribir el evento en 'eventos' de la actividad
                });
    }
    public ArrayList<Actividades> obtenerActividadesDesdeMemoria() {
        try (FileInputStream fileInputStream = getContext().openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);
            return alumno.getActividadesId();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Actividades buscarActividad(String id) {
        AsyncTask<String, Void, Actividades> task = new AsyncTask<String, Void, Actividades>() {
            @Override
            protected Actividades doInBackground(String... strings) {
                Actividades ac = null; // Inicializa a null para manejar casos donde no se encuentra la actividad

                try {
                    Task<DocumentSnapshot> task = db.collection("actividades").document(strings[0]).get();
                    DocumentSnapshot documentSnapshot = Tasks.await(task);

                    if (documentSnapshot.exists()) {
                        Actividades a = documentSnapshot.toObject(Actividades.class);
                        Log.d("msg-test", "actividad encontrada: " + a.getNombre());

                        if (a.getEstado().equals("abierto")) {
                            ac = a;
                        }
                    }
                } catch (Exception e) {
                    Log.d("msg-test", "DaGestionFragment error buscando act: " + e.getMessage());
                }

                return ac;
            }
        };

        try {
            return task.execute(id).get();
        } catch (Exception e) {
            Log.d("msg-test", "Error al ejecutar AsyncTask: " + e.getMessage());
            return null;
        }
    }

}
