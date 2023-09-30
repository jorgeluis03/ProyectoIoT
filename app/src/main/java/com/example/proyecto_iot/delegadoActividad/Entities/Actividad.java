package com.example.proyecto_iot.delegadoActividad.Entities;

import com.example.proyecto_iot.alumno.Objetos.Evento;

import java.util.List;

public class Actividad {
    private String nombre;
    private String delegado;

    private String fecha;
    private String hora;

    private List<Evento> eventos;

    public Actividad(String nombre, String fecha, String hora){
        this.nombre = nombre;
        this.delegado = delegado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDelegado() {
        return delegado;
    }

    public void setDelegado(String delegado) {
        this.delegado = delegado;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
