package com.example.proyecto_iot.delegadoGeneral.retrofitServices;

import com.example.proyecto_iot.delegadoGeneral.entity.EmpleadoDto;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EmpleadoService {
    @GET("/")
    Call<EmpleadoDto> obtenerLista();

}
