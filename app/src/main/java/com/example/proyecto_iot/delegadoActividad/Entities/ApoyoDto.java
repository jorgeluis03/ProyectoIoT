package com.example.proyecto_iot.delegadoActividad.Entities;

import com.example.proyecto_iot.alumno.Entities.Alumno;

import java.io.Serializable;

public class ApoyoDto implements Serializable {
    private Alumno alumno;
    private String categoria;
    private String eventoId;
    private String eventoName;

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEventoId() {
        return eventoId;
    }

    public void setEventoId(String eventoId) {
        this.eventoId = eventoId;
    }

    public String getEventoName() {
        return eventoName;
    }

    public void setEventoName(String eventoName) {
        this.eventoName = eventoName;
    }
}
