package com.example.proyecto_iot.alumno.Entities;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Notificacion {
    private String texto;
    private Date hora;

    public Notificacion(String texto, String hora){
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getHoraFromNow(){
        String fromNow;
        Date now = Date.from(Instant.now());
        long diff = now.getTime() - this.hora.getTime();
        if (TimeUnit.MILLISECONDS.toMinutes(diff)>=0&&TimeUnit.MILLISECONDS.toMinutes(diff)<60){
            fromNow = "hace "+ TimeUnit.MILLISECONDS.toMinutes(diff)+" minutos";
        }else if (TimeUnit.MILLISECONDS.toHours(diff)>0&&TimeUnit.MILLISECONDS.toHours(diff)<24){
            fromNow = "hace "+ TimeUnit.MILLISECONDS.toHours(diff)+" horas";
        } else if (TimeUnit.MILLISECONDS.toDays(diff)<30){
            fromNow = "hace "+ TimeUnit.MILLISECONDS.toDays(diff)+" días";
        }else {
            fromNow = "hace "+ (int)TimeUnit.MILLISECONDS.toDays(diff)%30 + " meses";
        }
        return fromNow;
    }
}
