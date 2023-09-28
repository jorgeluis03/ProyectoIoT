package com.example.proyecto_iot.delegadoGeneral.entity;

public class Actividades {
    private String nombre;
    private String estado;
    private Usuario usuario;

    public Actividades(String nombre, String estado, Usuario usuario) {
        this.nombre = nombre;
        this.estado = estado;
        this.usuario = usuario;
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
