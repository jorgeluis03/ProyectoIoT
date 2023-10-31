package com.example.proyecto_iot.alumno.Entities;

import java.sql.Timestamp;

public class Foto {
    private String alumnoID;
    private String descripcion;
    private Timestamp fechaHoraSubida;
    private String fotoUrl;

    public String getAlumnoID() {
        return alumnoID;
    }

    public void setAlumnoID(String alumnoID) {
        this.alumnoID = alumnoID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFechaHoraSubida() {
        return fechaHoraSubida;
    }

    public void setFechaHoraSubida(Timestamp fechaHoraSubida) {
        this.fechaHoraSubida = fechaHoraSubida;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
