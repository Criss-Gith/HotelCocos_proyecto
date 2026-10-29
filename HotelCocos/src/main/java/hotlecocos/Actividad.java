/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotlecocos;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descripcion;
    private LocalDateTime fecha;

    public Actividad(String descripcion) {

        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );

        return fecha.format(formato);
    }

    @Override
    public String toString() {

        return descripcion +
                " - " +
                getFecha();
    }
}
