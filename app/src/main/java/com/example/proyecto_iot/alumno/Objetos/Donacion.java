package com.example.proyecto_iot.alumno.Objetos;

public class Donacion {
    private String texto;
    private String hora;
    private String donacion;
    public Donacion(String texto, String hora, String donacion){
        this.texto = texto;
        this.hora = hora;
        this.donacion = donacion;
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
}
