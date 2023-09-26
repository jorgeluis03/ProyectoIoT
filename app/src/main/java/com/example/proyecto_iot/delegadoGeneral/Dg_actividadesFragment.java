package com.example.proyecto_iot.delegadoGeneral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyecto_iot.databinding.FragmentDgActividadesBinding;
import com.example.proyecto_iot.delegadoGeneral.adapter.ListaEmpleadosAdapter;
import com.example.proyecto_iot.delegadoGeneral.entity.Empleado;
import com.example.proyecto_iot.delegadoGeneral.entity.EmpleadoDto;
import com.example.proyecto_iot.delegadoGeneral.retrofitServices.EmpleadoService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dg_actividadesFragment extends Fragment {
    FragmentDgActividadesBinding binding;
    EmpleadoService empleadoService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgActividadesBinding.inflate(inflater,container,false);

        obtenerData();
        return binding.getRoot();
    }

    public void obtenerData(){
        empleadoService = new Retrofit.Builder()
                .baseUrl("http://10.100.251.61:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmpleadoService.class);


        empleadoService.obtenerLista().enqueue(new Callback<EmpleadoDto>() {
            @Override
            public void onResponse(Call<EmpleadoDto> call, Response<EmpleadoDto> response) {
                if (response.isSuccessful()){
                    EmpleadoDto empleadoDto = response.body();
                    Log.d("response",empleadoDto.getEstado());
                    List<Empleado> lista = empleadoDto.getLista();

                    ListaEmpleadosAdapter adapter = new ListaEmpleadosAdapter();
                    adapter.setContext(getContext());
                    adapter.setListaEmpleados(lista);

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


}