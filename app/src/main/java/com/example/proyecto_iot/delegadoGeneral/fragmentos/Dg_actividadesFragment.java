package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyecto_iot.databinding.FragmentDgActividadesBinding;
import com.example.proyecto_iot.delegadoGeneral.CrearActividadActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaActividadesAdapter;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaEmpleadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.entity.Empleado;
import com.example.proyecto_iot.delegadoGeneral.entity.EmpleadoDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Usuario;
import com.example.proyecto_iot.delegadoGeneral.retrofitServices.EmpleadoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dg_actividadesFragment extends Fragment {

    FragmentDgActividadesBinding binding;
    EmpleadoService empleadoService;
    private List<Actividades> cacheDataAct =  new ArrayList<>();; // Almacena los datos en caché
    private List<Actividades> listaAct = llenarListaUsuarios();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgActividadesBinding.inflate(inflater,container,false);
        // Cambiar el contenido del Toolbar
        ((Dg_Activity) requireActivity()).setToolbarContent("ActiviConnect");


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
            Intent resulrData = result.getData();
            if(resulrData!=null){
                String nombreActividad = resulrData.getStringExtra("nombreActividad");
                Log.d("msg-test",nombreActividad);
                listaAct.add(new Actividades(nombreActividad,"abierto",null));
            }

        }
    });


    //Metodos para el recycleView
    @Override
    public void onStart() {
        super.onStart();
        cargarLista();
    }

    public void cargarLista(){
        ListaActividadesAdapter adapter = new ListaActividadesAdapter();
        adapter.setContext(getContext());
        adapter.setListaActividades(listaAct);

        binding.recycleViewActividadesDg.setAdapter(adapter);
        binding.recycleViewActividadesDg.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public List<Actividades> llenarListaUsuarios(){
        List<Actividades> listaActiv = new ArrayList<>();
        listaActiv.add(new Actividades("FUTSAL VARONES","abierto",null ));
        listaActiv.add(new Actividades("BASQUET VARONES","en curso",new Usuario("Jorge","Dominguez","a202006436@pucp.edu.pe") ));
        listaActiv.add(new Actividades("GYMKANA TELECOM","abierto",null ));
        listaActiv.add(new Actividades("SABADO REFRESCANTE","abierto",null ));
        listaActiv.add(new Actividades("VOLEY VARONES & MUJERES","en curso",new Usuario("Gustavo","Peña","a20105236@pucp.edu.pe") ));
        listaActiv.add(new Actividades("BASQUET MUJERES","en curso",new Usuario("Niurka","Sanchez","a201925636@pucp.edu.pe") ));
        listaActiv.add(new Actividades("BAILETON TELITO","abierto",null ));
        listaActiv.add(new Actividades("FUTSAL MUJERES","en curso",new Usuario("Milagros","Ramirez","a20189856@pucp.edu.pe") ));

        return listaActiv;
    }


    /*@Override
    public void onStart() {
        super.onStart();
        cargarlista();
    }*/

    /*public void obtenerData(){
        empleadoService = new Retrofit.Builder()
                .baseUrl("http://192.168.18.44:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmpleadoService.class);

    }*/
    /*public void cargarlista(){

        if(!cacheData.isEmpty()){
            ListaEmpleadosAdapter adapter = new ListaEmpleadosAdapter();
            adapter.setContext(getContext());
            adapter.setListaEmpleados(cacheData);

            binding.recycleViewActividadesDg.setAdapter(adapter);
            binding.recycleViewActividadesDg.setLayoutManager(new LinearLayoutManager(getContext()));
        }else {
            empleadoService.obtenerLista().enqueue(new Callback<EmpleadoDto>() {
                @Override
                public void onResponse(Call<EmpleadoDto> call, Response<EmpleadoDto> response) {
                    if (response.isSuccessful()){
                        EmpleadoDto empleadoDto = response.body();
                        Log.d("response",empleadoDto.getEstado());
                        List<Empleado> lista = empleadoDto.getLista();

                        // Limitar la lista a 7 elementos si es mayor que 7
                        List<Empleado> cacheData7Elementos = lista.subList(0, Math.min(lista.size(), 7));

                        ListaEmpleadosAdapter adapter = new ListaEmpleadosAdapter();
                        adapter.setContext(getContext());
                        adapter.setListaEmpleados(cacheData7Elementos);

                        binding.recycleViewActividadesDg.setAdapter(adapter);
                        binding.recycleViewActividadesDg.setLayoutManager(new LinearLayoutManager(getContext()));


                    }else {
                        Log.d("response","Repuesta vacia");
                    }
                }

                @Override
                public void onFailure(Call<EmpleadoDto> call, Throwable t) {
                    Log.d("error","paso algo!");
                    Log.d("error",t.getMessage());

                }
            });
        }


    }*/


}