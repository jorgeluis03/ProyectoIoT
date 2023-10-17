package com.example.proyecto_iot.alumno.Entities;

import java.io.Serializable;

public class Lugar implements Serializable {
    private String nombre;
    private double coordenadas;

    public Lugar(String nombre, double coordenadas) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(double coordenadas) {
        this.coordenadas = coordenadas;
    }
}
