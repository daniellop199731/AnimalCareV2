package com.example.animalcarev2;

public class ProduccionDerivado {

    private String codigoAnimal;
    private String derivado;
    private String unidadMedida;
    private int cantidad;
    private String feProduccion;

    public ProduccionDerivado(String codigoAnimal, String derivado, String unidadMedida, int cantidad, String feProduccion) {

            this.codigoAnimal = codigoAnimal;
            this.derivado = derivado;
            this.unidadMedida = unidadMedida;
            this.cantidad = cantidad;
            this.feProduccion = feProduccion;
    }

    public String getCodigoAnimal() {
        return codigoAnimal;
    }

    public String getDerivado() {
        return derivado;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getFeProduccion() {
        return feProduccion;
    }
}
