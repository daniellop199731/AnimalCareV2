package com.example.animalcarev2;

public class MuerteAnimal {

    private String codigo;
    private String fecha_muerte;
    private String descripcion;

    public MuerteAnimal(String codigo, String fecha_muerte, String descripcion) {
        this.codigo = codigo;
        this.fecha_muerte = fecha_muerte;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getFecha_muerte() {
        return fecha_muerte;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
