package com.example.proyecto_iot.alumno;

import com.google.firebase.firestore.GeoPoint;

public class LugarSeleccionado {
    public GeoPoint coordenadas;
    private String nombre;

    public LugarSeleccionado(GeoPoint coordenadas, String nombre) {
        this.coordenadas = coordenadas;
        this.nombre = nombre;
    }

    public GeoPoint getCoordenadas() {
        return coordenadas;
    }

    public String getNombre() {
        return nombre;
    }
}
