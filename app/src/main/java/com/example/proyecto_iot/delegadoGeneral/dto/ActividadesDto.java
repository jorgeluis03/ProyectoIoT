package com.example.proyecto_iot.delegadoGeneral.dto;

public class ActividadesDto {
    private String estado;
    private String id;
    private String nombre;

    public ActividadesDto(String id, String nombre,String estado) {
        this.estado = estado;
        this.id = id;
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}
