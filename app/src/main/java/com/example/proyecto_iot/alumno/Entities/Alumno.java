package com.example.proyecto_iot.alumno.Entities;

import com.example.proyecto_iot.delegadoGeneral.dto.ActividadesDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;

import java.io.Serializable;
import java.util.ArrayList;

public class Alumno implements Serializable {
    private String id;
    private String nombre;
    private String apellidos;
    private String rol; // DelegadoGeneral, DelegadoActividad o Alumno
    private String codigo;
    private String correo;
    private String fotoUrl;
    private String tipo; // Alumno o Egresado
    private String estado; // activo, inactivo (considerando que se puede eliminar cuenta), pendiente, baneado
    private String fcmToken;
    private ArrayList<Actividades> actividadesId;
    public Alumno(){

    }

    public Alumno(String id, String nombre, String apellidos, String rol, String codigo, String correo, String fotoUrl, String tipo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rol = rol;
        this.codigo = codigo;
        this.correo = correo;
        this.fotoUrl = fotoUrl;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;

    }

    public ArrayList<Actividades> getActividadesId() {
        return actividadesId;
    }

    public void setActividadesId(ArrayList<Actividades> actividadesId) {
        this.actividadesId = actividadesId;
    }
    public String fullNameFormal(){
        String[] nombre = this.nombre.split(" ");
        String[] last = this.apellidos.split(" ");
        return last[0]+", "+nombre[0];
    }
    public String fullName(){
        String[] nombre = this.nombre.split(" ");
        String[] last = this.apellidos.split(" ");
        return nombre[0]+" "+last[0];
    }
}
