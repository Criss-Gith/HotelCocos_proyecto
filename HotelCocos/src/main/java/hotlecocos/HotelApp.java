/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package hotlecocos;
import javax.swing.SwingUtilities;

public class HotelApp {

    public static void main(String[] args) {

        // Cargar los datos guardados
        DataStore.cargar();

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
