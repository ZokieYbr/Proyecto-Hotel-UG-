package com.vane.hotel.controlador;

public class OcupacionMesDTO {
    private String mes;
    private Double ocupacion_real;
    private Double ocupacion_predicha;

    public OcupacionMesDTO() {
    }


    public OcupacionMesDTO(String mes, Double ocupacion_real, Double ocupacion_predicha) {
        this.mes = mes;
        this.ocupacion_real = ocupacion_real;
        this.ocupacion_predicha = ocupacion_predicha;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getOcupacion_real() {
        return ocupacion_real;
    }

    public void setOcupacion_real(Double ocupacion_real) {
        this.ocupacion_real = ocupacion_real;
    }

    public Double getOcupacion_predicha() {
        return ocupacion_predicha;
    }

    public void setOcupacion_predicha(Double ocupacion_predicha) {
        this.ocupacion_predicha = ocupacion_predicha;
    }
}