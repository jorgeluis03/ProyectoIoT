package com.example.proyecto_iot.delegadoGeneral.dto;

public class ApoyosActivdadDto {
    private String nombreActividad;
    private int cantidadApoyos;

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public int getCantidadApoyos() {
        return cantidadApoyos;
    }

    public void setCantidadApoyos(int cantidadApoyos) {
        this.cantidadApoyos = cantidadApoyos;
    }
}
