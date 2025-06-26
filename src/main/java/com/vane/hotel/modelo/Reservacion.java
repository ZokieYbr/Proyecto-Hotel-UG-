package com.vane.hotel.modelo;

import java.util.Date;

public class Reservacion {
    int id;
    int cliente_id;
    int habitacion_id;
    Date fecha_entrada;
    Date fecha_salida;
    String estado;

    public Reservacion(int id, int cliente_id, int habitacion_id, Date fecha_entrada, Date fecha_salida, String estado) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.habitacion_id = habitacion_id;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.estado = estado;
    }
    public int getId() {
        return id; }

    public void setId(int id) {
        this.id = id; }

    public int getCliente_id() {
        return cliente_id; }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id; }

    public int getHabitacion_id() {
        return habitacion_id; }

    public void setHabitacion_id(int habitacion_id) {
        this.habitacion_id = habitacion_id; }
    public Date getFecha_entrada() {
        return fecha_entrada; }

    public void setFecha_entrada(Date fecha_entrada) {
        this.fecha_entrada = fecha_entrada; }
    public Date getFecha_salida() {
        return fecha_salida; }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida; }
    public String getEstado() {
        return estado; }

    public void setEstado(String estado) {
        this.estado = estado; }
}