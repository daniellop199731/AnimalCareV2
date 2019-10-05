package com.example.animalcarev2;

public class Cria {
    public String nombre;
    private String codigo;
    private String tipo;
    private String raza;
    private String sexo;
    private String fecha_nacimiento;
    private String codigoP;
    private String codigoM;

    public Cria(String nombre, String codigo, String codigoP, String codigoM, String tipo, String raza, String sexo, String fecha_nacimiento) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.codigoP = codigoP;
        this.codigoM = codigoM;
        this.tipo = tipo;
        this.raza = raza;
        this.sexo = sexo;
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoP() {
        return codigoP;
    }

    public void setCodigoP(String codigoP) {
        this.codigoP = codigoP;
    }

    public String getCodigoM() {
        return codigoM;
    }

    public void setCodigoM(String codigoM) {
        this.codigoM = codigoM;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
}
