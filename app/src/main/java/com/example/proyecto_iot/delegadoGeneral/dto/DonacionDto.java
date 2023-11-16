package com.example.proyecto_iot.delegadoGeneral.dto;

public class DonacionDto {
    private String horaFecha;
    private String monto;
    private String nombreDonante;


    public DonacionDto(String hora, String monto, String nombreDonante) {
        this.horaFecha = hora;
        this.monto = monto;
        this.nombreDonante = nombreDonante;
    }

    public String getHorahoraFecha() {
        return horaFecha;
    }

    public void setHorahoraFecha(String hora) {
        this.horaFecha = hora;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getNombreDonante() {
        return nombreDonante;
    }

    public void setNombreDonante(String nombreDonante) {
        this.nombreDonante = nombreDonante;
    }
}
