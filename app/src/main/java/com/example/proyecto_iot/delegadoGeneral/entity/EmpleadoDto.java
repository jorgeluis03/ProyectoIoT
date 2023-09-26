package com.example.proyecto_iot.delegadoGeneral.entity;

import java.util.List;

public class EmpleadoDto {
    private List<Empleado> lista;
    private String estado;

    public List<Empleado> getLista() {
        return lista;
    }

    public void setLista(List<Empleado> lista) {
        this.lista = lista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
