/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotlecocos;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField correo;
    private JPasswordField password;

    public LoginFrame() {

        setTitle("HOTEL COCOS - Iniciar sesión");

        setSize(900, 550);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);


        JPanel principal =
                new JPanel(
                        new GridLayout(1, 2)
                );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        45, 60, 45, 60
                )
        );


        // =================================================
        // IZQUIERDA
        // =================================================

        JPanel izquierda =
                new JPanel();

        izquierda.setLayout(
                new BoxLayout(
                        izquierda,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel logo =
                new JLabel("🌴");

        logo.setFont(
                new Font(
                        "Serif",
                        Font.PLAIN,
                        70
                )
        );

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel nombre =
                new JLabel("C O C O S");

        nombre.setFont(
                new Font(
                        "Serif",
                        Font.PLAIN,
                        30
                )
        );

        nombre.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel hotel =
                new JLabel("— H O T E L —");

        hotel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel frase =
                new JLabel(
                        "Tu descanso, nuestra prioridad."
                );

        frase.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        izquierda.add(
                Box.createVerticalGlue()
        );

        izquierda.add(logo);

        izquierda.add(nombre);

        izquierda.add(hotel);

        izquierda.add(
                Box.createVerticalStrut(25)
        );

        izquierda.add(frase);

        izquierda.add(
                Box.createVerticalGlue()
        );


        // =================================================
        // DERECHA
        // =================================================

        JPanel derecha =
                new JPanel();

        derecha.setLayout(
                new BoxLayout(
                        derecha,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel bienvenida =
                new JLabel(
                        "Bienvenido de nuevo"
                );

        bienvenida.setFont(
                new Font(
                        "Serif",
                        Font.BOLD,
                        27
                )
        );


        JLabel texto =
                new JLabel(
                        "Ingresa tus credenciales para continuar"
                );


        JLabel correoLabel =
                new JLabel(
                        "Correo electrónico"
                );


        correo =
                new JTextField(
                        "admin@cocos.com"
                );

        correo.setMaximumSize(
                new Dimension(350, 40)
        );


        JLabel passwordLabel =
                new JLabel(
                        "Contraseña"
                );


        password =
                new JPasswordField(
                        "123456"
                );

        password.setMaximumSize(
                new Dimension(350, 40)
        );


        JCheckBox recordar =
                new JCheckBox(
                        "Recordarme"
                );


        JButton iniciar =
                new JButton(
                        "Iniciar sesión"
                );

        iniciar.setMaximumSize(
                new Dimension(350, 45)
        );


        iniciar.addActionListener(
                e -> iniciarSesion()
        );


        derecha.add(
                Box.createVerticalGlue()
        );

        derecha.add(bienvenida);

        derecha.add(
                Box.createVerticalStrut(8)
        );

        derecha.add(texto);

        derecha.add(
                Box.createVerticalStrut(25)
        );

        derecha.add(correoLabel);

        derecha.add(correo);

        derecha.add(
                Box.createVerticalStrut(15)
        );

        derecha.add(passwordLabel);

        derecha.add(password);

        derecha.add(recordar);

        derecha.add(
                Box.createVerticalStrut(15)
        );

        derecha.add(iniciar);

        derecha.add(
                Box.createVerticalGlue()
        );


        principal.add(izquierda);

        principal.add(derecha);

        add(principal);
    }


    // =====================================================
    // LOGIN
    // =====================================================

    private void iniciarSesion() {

        String email =
                correo.getText().trim();

        String pass =
                new String(
                        password.getPassword()
                );


        if (
                email.equals("admin@cocos.com")
                &&
                pass.equals("123456")
        ) {

            DataStore.agregarActividad(
                    "Admin inició sesión"
            );

            dispose();

            new DashboardFrame()
                    .setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

