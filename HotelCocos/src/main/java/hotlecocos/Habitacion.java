/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package hotlecocos;

import java.io.Serializable;

public class Habitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int numero;
    private String tipo;
    private double precio;
    private String estado;

    public Habitacion(
            int numero,
            String tipo,
            double precio,
            String estado) {

        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitación " + numero +
                " - " + tipo +
                " - S/ " + precio;
    }
}
