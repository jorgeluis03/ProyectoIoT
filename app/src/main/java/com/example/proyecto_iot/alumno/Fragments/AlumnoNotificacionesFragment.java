package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Notificacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaNotificacionesAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoNotificacionesBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AlumnoNotificacionesFragment extends Fragment {

    FragmentAlumnoNotificacionesBinding binding;
    ArrayList<Notificacion> notificacionList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListaNotificacionesAdapter adapter = new ListaNotificacionesAdapter();
    private String userUid = FirebaseAuth.getInstance().getUid();
    Notificacion notificacion = new Notificacion();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoNotificacionesBinding.inflate(inflater, container, false);

        Bundle bundle = new Bundle();
        bundle.putString("header", "Notificaciones");
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentNotificacionesHeader, AlumnoHeader2Fragment.class, bundle)
                .commit();
        binding.progressBar13.setVisibility(View.VISIBLE);

        cargarNotificaciones();

        adapter.setContext(getContext());
        adapter.setNotificacionList(notificacionList);

        binding.rvNotificaciones.setAdapter(adapter);
        binding.rvNotificaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    private void cargarNotificaciones() {
        List<Task<?>> tasks = new ArrayList<>();
        db.collection("alumnos").document(userUid)
                .collection("notificaciones")
                .orderBy("hora", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null){
                        Log.d("msg-test", "Listen failed in eventos apoyando", error);
                        return;
                    }
                    if (value != null){
                        notificacionList.clear();
                        for (QueryDocumentSnapshot doc: value){
                            notificacion = new Notificacion();
                            tasks.add(inicializarNoty(doc));
                        }
                        Tasks.whenAllComplete(tasks)
                                .addOnCompleteListener(allTasks -> {
                                    binding.progressBar13.setVisibility(View.GONE);
                                    if (notificacionList.isEmpty()){
                                        binding.textView23.setVisibility(View.VISIBLE);
                                        binding.imageView14.setVisibility(View.VISIBLE);
                                    }else {
                                        adapter.notifyDataSetChanged();
                                        binding.textView23.setVisibility(View.GONE);
                                        binding.imageView14.setVisibility(View.GONE);
                                    }
                                });
                    }
                });
    }

    private Task<Void> inicializarNoty(QueryDocumentSnapshot doc) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Notificacion notificacion = doc.toObject(Notificacion.class);
        notificacionList.add(notificacion);
        taskCompletionSource.setResult(null);
        return taskCompletionSource.getTask();
    }

    /* daba error xd
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                TextView textView = view.findViewById(R.id.textView19);
                textView.setText("Notificaciones");
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

     */
}