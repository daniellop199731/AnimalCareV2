package com.example.animalcarev2;

public class Derivado {
    private String nombre;
    private String unidadMedida;

    public Derivado(String nombre, String unidadMedida) {
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}
