package com.example.proyecto_iot.alumno.Entities;

public class Donacion {
    private String texto;
    private String hora;
    private String donacion;
    private String fecha;
    private String rol;
    private String fotoQR;
    private String monto;
    private String nombre;
    public Donacion(String texto, String hora, String donacion, String fecha, String rol, String fotoQR, String monto, String nombre){
        this.texto = texto;
        this.hora = hora;
        this.donacion = donacion;
        this.fecha = fecha;
        this.rol = rol;
        this.fotoQR = fotoQR;
        this.monto = monto;
        this.nombre = nombre;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDonacion() {
        return donacion;
    }

    public void setDonacion(String donacion) {
        this.donacion = donacion;
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
