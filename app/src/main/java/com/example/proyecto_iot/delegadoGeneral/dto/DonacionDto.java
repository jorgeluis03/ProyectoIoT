package com.example.proyecto_iot.delegadoGeneral.dto;

public class DonacionDto {
    private String horaFecha;
    private String monto;
    private String nombreDonante;
    private String idDocumento;
    private String codigoDonante;



    public DonacionDto(String idDocumento,String codigoDonante,String hora, String monto, String nombreDonante) {
        this.idDocumento=idDocumento;
        this.codigoDonante = codigoDonante;
        this.horaFecha = hora;
        this.monto = monto;
        this.nombreDonante = nombreDonante;
    }

    public String getCodigoDonante() {
        return codigoDonante;
    }

    public void setCodigoDonante(String codigoDonante) {
        this.codigoDonante = codigoDonante;
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
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
