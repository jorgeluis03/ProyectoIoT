package com.example.proyecto_iot.delegadoGeneral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgActividadesBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }

}