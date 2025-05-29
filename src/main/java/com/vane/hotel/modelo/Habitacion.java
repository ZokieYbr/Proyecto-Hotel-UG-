package com.vane.hotel.modelo;

public class Habitacion {
    int id;
    int numero;
    String tipo;
    double precio_noche;
    String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getprecio_noche() {
        return precio_noche;
    }

    public void setprecio_noche(double precio_noche) {
        this.precio_noche = precio_noche;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Habitacion(int id, int numero, String tipo, double precio_noche, String estado) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precio_noche = precio_noche;
        this.estado = estado;
    }




}
