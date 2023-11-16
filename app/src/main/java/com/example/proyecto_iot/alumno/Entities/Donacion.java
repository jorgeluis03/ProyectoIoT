package com.example.proyecto_iot.alumno.Entities;

import java.io.Serializable;

public class Donacion implements Serializable {
    private String hora;
    private String fecha;
    private String rol;
    private String fotoQR;
    private String monto;
    private String nombre;
    private String estado;

    public Donacion(String fecha,String hora, String rol, String fotoQR, String monto, String nombre, String estado){
        this.hora = hora;
        this.fecha = fecha;
        this.rol = rol;
        this.fotoQR = fotoQR;
        this.monto = monto;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Donacion(){

    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFotoQR() {
        return fotoQR;
    }

    public void setFotoQR(String fotoQR) {
        this.fotoQR = fotoQR;
    }
}
