package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentDgActividadesBinding;
import com.example.proyecto_iot.delegadoGeneral.CrearActividadActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Dg_actividadesFragment extends Fragment {
    private boolean actividadesCargadas = false;
    ActivityResultLauncher<Intent> lunchEditar;
    private ListaActividadesAdapter adapter;
    FragmentDgActividadesBinding binding;
    private List<Actividades> listaAct; // Inicializa la lista
    FirebaseFirestore db;
    ListenerRegistration listenerRegistration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgActividadesBinding.inflate(inflater,container,false);
        // Cambiar el contenido del Toolbar
        ((Dg_Activity) requireActivity()).setToolbarContent("ActiviConnect");

        lunchEditar = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent resultData = result.getData();
                lunchEditar(resultData);
            }
        });
        adapter = new ListaActividadesAdapter(lunchEditar);

        cargarActividades();

        binding.floatingButtonCrearActividadDg.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CrearActividadActivity.class);
            launcher.launch(intent);
        });


        return binding.getRoot();
    }

    //
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent resultData = result.getData();
            if (resultData != null) {
                Actividades actividad = (Actividades) resultData.getSerializableExtra("nombreActividad");
                Alumno usuarioDelegado = (Alumno) resultData.getSerializableExtra("delegado");
                actividad.setDelegadoActividad(usuarioDelegado);


                db = FirebaseFirestore.getInstance();
                db.collection("actividades")
                        .add(actividad)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                actividad.setId(documentReference.getId());
                                listaAct.add(actividad);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Actividad agregada", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        }
    });

    public void cargarActividades(){
        db = FirebaseFirestore.getInstance();
        db.collection("actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        listaAct=new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Actividades actividades = document.toObject(Actividades.class);
                            actividades.setId(document.getId());
                            listaAct.add(actividades);
                        }

                        adapter.setContext(getContext());
                        adapter.setListaActividades(listaAct);
                        binding.recycleViewActividadesDg.setAdapter(adapter);
                        binding.recycleViewActividadesDg.setLayoutManager(new LinearLayoutManager(getContext()));

                        actividadesCargadas = true; // Marca las actividades como cargadas
                    }else {
                        Toast.makeText(getContext(),"pasó algo",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void lunchEditar(Intent resultData){
        if (resultData != null) {
            Actividades actividad = (Actividades) resultData.getSerializableExtra("nombreActividad");
            Alumno usuarioDelegado = (Alumno) resultData.getSerializableExtra("delegado");
            actividad.setDelegadoActividad(usuarioDelegado);
            Log.d("msg-test", "luncher acti: " + actividad.getNombre()+" id: "+actividad.getId());
            Log.d("msg-test", "luncher dele: " + actividad.getDelegadoActividad().getNombre());

            db = FirebaseFirestore.getInstance();
            db.collection("actividades")
                    .document(actividad.getId())
                    .update("nombre",actividad.getNombre(),
                            "delegadoActividad",usuarioDelegado)
                    .addOnSuccessListener(unused -> {
                        // Busca el objeto correspondiente en la lista y actualízalo
                        int position = -1;
                        for (int i = 0; i < listaAct.size(); i++) {
                            if (listaAct.get(i).getId().equals(actividad.getId())) {
                                position = i;
                                break;
                            }
                        }

                        if (position != -1) {
                            listaAct.set(position, actividad);
                            adapter.notifyItemChanged(position);
                        }

                        Toast.makeText(getContext(),"Actividad actualizada",Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(),"algo pasó",Toast.LENGTH_SHORT).show();
                    });

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenerRegistration!=null){
            listenerRegistration.remove();
        }

    }
}