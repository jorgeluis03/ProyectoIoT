package com.example.proyecto_iot.delegadoGeneral.entity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Actividades implements Serializable {
    private String id;
    private String nombre;
    private String estado;
    private String codigoDelegado;

    private Usuario delegadoActividad;

    public Usuario getDelegadoActividad() {
        return delegadoActividad;
    }

    public void setDelegadoActividad(Usuario delegadoActividad) {
        this.delegadoActividad = delegadoActividad;
    }

    public String getCodigoDelegado() {
        return codigoDelegado;
    }

    public void setCodigoDelegado(String codigoDelegado) {
        this.codigoDelegado = codigoDelegado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
