package com.example.proyecto_iot.alumno.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Evento implements Serializable {
    private String titulo;
    private String descripcion;
    private String actividad;
    private String fotoUrl;
    private String fecha;
    private String hora;
    private Lugar lugar;
    private String horaFechaCreacion;
    private List<Foto> fotosSubidas;
    private String estado;

    public Evento(String titulo, String descripcion, String actividad, String fecha, String hora, Lugar lugar, String estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
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

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public String getHoraFechaCreacion() {
        return horaFechaCreacion;
    }
    public void setHoraFechaCreacion(String horaFechaCreacion) {
        this.horaFechaCreacion = horaFechaCreacion;
    }

    public List<Foto> getFotosSubidas() {
        return fotosSubidas;
    }

    public void setFotosSubidas(List<Foto> fotosSubidas) {
        this.fotosSubidas = fotosSubidas;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
