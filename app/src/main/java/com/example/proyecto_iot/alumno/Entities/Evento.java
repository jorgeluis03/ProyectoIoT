package com.example.proyecto_iot.alumno.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Evento implements Serializable {
    private String titulo;
    private String descripcion;
    private String actividad;
    private byte[] foto;
    private String fecha;
    private String hora;
    private Lugar lugar;
    private LocalDateTime fechaHoraCreacion;

    private List<Foto> fotosSubidas;
    private boolean apoyo;

    public Evento(String titulo, String descripcion, String actividad, String fecha, String hora, Lugar lugar, boolean apoyo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
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

    public LocalDateTime getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public List<Foto> getFotosSubidas() {
        return fotosSubidas;
    }

    public void setFotosSubidas(List<Foto> fotosSubidas) {
        this.fotosSubidas = fotosSubidas;
    }

    public boolean isApoyo() {
        return apoyo;
    }

    public void setApoyo(boolean apoyo) {
        this.apoyo = apoyo;
    }
}
