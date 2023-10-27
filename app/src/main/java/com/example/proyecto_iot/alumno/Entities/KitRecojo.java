package com.example.proyecto_iot.alumno.Entities;

import com.google.firebase.firestore.GeoPoint;

import java.security.Timestamp;

public class KitRecojo {
    private GeoPoint lugar;
    private String estado;
    private Timestamp fechaHora;
    public KitRecojo() {
        // Constructor vacío requerido para Firestore
    }

    public KitRecojo(GeoPoint lugar, String estado, Timestamp fechaHora) {
        this.lugar = lugar;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    // Getter y Setter para cada campo
    public GeoPoint getLugar() {
        return lugar;
    }

    public void setLugar(GeoPoint lugar) {
        this.lugar = lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

}