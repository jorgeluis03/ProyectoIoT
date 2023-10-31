package com.example.proyecto_iot.delegadoGeneral.entity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.proyecto_iot.alumno.Entities.Alumno;

import java.io.Serializable;

public class Actividades implements Serializable {
    private String id;
    private String nombre;
    private String estado;

    private Alumno delegadoActividad;

    public Alumno getDelegadoActividad() {
        return delegadoActividad;
    }

    public void setDelegadoActividad(Alumno delegadoActividad) {
        this.delegadoActividad = delegadoActividad;
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
