package com.example.proyecto_iot.alumno.Entities;

public class Donacion {
    private String texto;
    private String hora;
    private String donacion;
    private String fecha;
    public Donacion(String texto, String hora, String donacion, String fecha){
        this.texto = texto;
        this.hora = hora;
        this.donacion = donacion;
        this.fecha = fecha;
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
}
