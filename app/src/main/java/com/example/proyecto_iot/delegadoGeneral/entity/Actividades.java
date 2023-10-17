package com.example.proyecto_iot.delegadoGeneral.entity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "actividades")
public class Actividades implements Serializable {
    @PrimaryKey(autoGenerate = true)

    private Integer id;
    private String nombre;
    private String estado;


    public Actividades(int id,String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
