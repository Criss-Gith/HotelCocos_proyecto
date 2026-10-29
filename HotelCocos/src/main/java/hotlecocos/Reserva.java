/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package hotlecocos;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String cliente;
    private int habitacion;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double total;
    private String estado;

    public Reserva(
            int id,
            String cliente,
            int habitacion,
            LocalDate checkIn,
            LocalDate checkOut,
            double total,
            String estado) {

        this.id = id;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.total = total;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public int getHabitacion() {
        return habitacion;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
