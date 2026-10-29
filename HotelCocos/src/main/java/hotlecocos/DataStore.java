/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package hotlecocos;

import java.io.*;
import java.util.ArrayList;

public class DataStore {

    private static final String ARCHIVO =
            "cocos.dat";

    public static ArrayList<Cliente> clientes =
            new ArrayList<>();

    public static ArrayList<Habitacion> habitaciones =
            new ArrayList<>();

    public static ArrayList<Reserva> reservas =
            new ArrayList<>();

    public static ArrayList<Actividad> actividades =
            new ArrayList<>();


    // =====================================================
    // CARGAR DATOS
    // =====================================================

    public static void cargar() {

        File archivo =
                new File(ARCHIVO);

        if (!archivo.exists()) {

            crearDatosIniciales();

            guardar();

            return;
        }

        try {

            ObjectInputStream entrada =
                    new ObjectInputStream(
                            new FileInputStream(ARCHIVO)
                    );

            clientes =
                    (ArrayList<Cliente>)
                            entrada.readObject();

            habitaciones =
                    (ArrayList<Habitacion>)
                            entrada.readObject();

            reservas =
                    (ArrayList<Reserva>)
                            entrada.readObject();

            actividades =
                    (ArrayList<Actividad>)
                            entrada.readObject();

            entrada.close();

        } catch (Exception e) {

            System.out.println(
                    "Error cargando datos: "
                            + e.getMessage()
            );

            crearDatosIniciales();
        }
    }


    // =====================================================
    // GUARDAR DATOS
    // =====================================================

    public static void guardar() {

        try {

            ObjectOutputStream salida =
                    new ObjectOutputStream(
                            new FileOutputStream(ARCHIVO)
                    );

            salida.writeObject(clientes);
            salida.writeObject(habitaciones);
            salida.writeObject(reservas);
            salida.writeObject(actividades);

            salida.close();

        } catch (IOException e) {

            System.out.println(
                    "Error guardando datos: "
                            + e.getMessage()
            );
        }
    }


    // =====================================================
    // DATOS INICIALES
    // =====================================================

    private static void crearDatosIniciales() {

        clientes.clear();
        habitaciones.clear();
        reservas.clear();
        actividades.clear();


        // CLIENTES

        clientes.add(
                new Cliente(
                        1,
                        "Juan Pérez",
                        "12345678",
                        "999111222",
                        "juan@gmail.com"
                )
        );

        clientes.add(
                new Cliente(
                        2,
                        "María López",
                        "87654321",
                        "999222333",
                        "maria@gmail.com"
                )
        );

        clientes.add(
                new Cliente(
                        3,
                        "Carlos Gómez",
                        "45678912",
                        "999333444",
                        "carlos@gmail.com"
                )
        );


        // HABITACIONES

        habitaciones.add(
                new Habitacion(
                        101,
                        "Simple",
                        120,
                        "Disponible"
                )
        );

        habitaciones.add(
                new Habitacion(
                        102,
                        "Simple",
                        120,
                        "Disponible"
                )
        );

        habitaciones.add(
                new Habitacion(
                        201,
                        "Doble",
                        180,
                        "Ocupada"
                )
        );

        habitaciones.add(
                new Habitacion(
                        202,
                        "Doble",
                        180,
                        "Disponible"
                )
        );

        habitaciones.add(
                new Habitacion(
                        301,
                        "Suite",
                        300,
                        "Ocupada"
                )
        );


        // ACTIVIDADES

        actividades.add(
                new Actividad(
                        "Sistema iniciado"
                )
        );

        actividades.add(
                new Actividad(
                        "Cliente Juan Pérez registrado"
                )
        );

        actividades.add(
                new Actividad(
                        "Habitación 201 ocupada"
                )
        );
    }


    // =====================================================
    // AGREGAR ACTIVIDAD
    // =====================================================

    public static void agregarActividad(
            String descripcion) {

        actividades.add(
                0,
                new Actividad(descripcion)
        );

        // Mantener solamente las últimas 20
        if (actividades.size() > 20) {

            actividades.remove(
                    actividades.size() - 1
            );
        }

        guardar();
    }
}

