package com.example.animalcarev2;

public class Animal {

    public String nombre;
    private String codigo;
    private String tipo;
    private String raza;
    private String sexo;
    private String fecha_nacimiento;
    private String enVida;


    public Animal(String nombre, String codigo, String tipo, String raza, String sexo, String fecha_nacimiento) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.tipo = tipo;
        this.raza = raza;
        this.sexo = sexo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.enVida = "s";
    }

    public Animal(String nombre, String codigo, String tipo, String raza, String sexo, String fecha_nacimiento, String enVida) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.tipo = tipo;
        this.raza = raza;
        this.sexo = sexo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.enVida = enVida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getEnVida(){
        return enVida;
    }
}
