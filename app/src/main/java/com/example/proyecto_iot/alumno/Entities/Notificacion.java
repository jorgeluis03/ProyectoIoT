package com.example.proyecto_iot.alumno.Entities;

public class Notificacion {
    private String texto;
    private String hora;

    public Notificacion(String texto, String hora){
        this.texto = texto;
        this.hora = hora;
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
}
