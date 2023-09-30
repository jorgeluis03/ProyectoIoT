package com.example.proyecto_iot.delegadoGeneral.entity;

import android.content.Intent;

public class Actividades {
    private Integer id;
    private String nombre;
    private String estado;
    private Usuario usuario;

    public Actividades(int id,String nombre, String estado, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
