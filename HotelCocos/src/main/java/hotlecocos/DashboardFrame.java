/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotlecocos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class DashboardFrame extends JFrame {

    private JPanel contenido;

    public DashboardFrame() {
        setTitle("COCOS HOTEL - Dashboard");
        setSize(1250, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.add(crearMenu(), BorderLayout.WEST);

        contenido = new JPanel(new BorderLayout());
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        mostrarDashboard();

        principal.add(contenido, BorderLayout.CENTER);
        add(principal);
    }

    // =====================================================
    // MENÚ
    // =====================================================
    private JPanel crearMenu() {
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(190, 0));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(248, 248, 248));

        JLabel logo = new JLabel("<html><center>🌴<br><b>C O C O S</b><br><small>H O T E L</small></center></html>");
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(Box.createVerticalStrut(20));
        menu.add(logo);
        menu.add(Box.createVerticalStrut(30));

        JButton dashboard = crearBoton("⌂    Dashboard");
        dashboard.addActionListener(e -> mostrarDashboard());

        JButton clientes = crearBoton("♙    Clientes");
        clientes.addActionListener(e -> mostrarClientes());

        JButton habitaciones = crearBoton("▣    Habitaciones");
        habitaciones.addActionListener(e -> mostrarHabitaciones());

        JButton reservas = crearBoton("▤    Reservas");
        reservas.addActionListener(e -> mostrarReservas());

        JButton reportes = crearBoton("▥    Reportes");
        reportes.addActionListener(e -> mostrarReportes());

        menu.add(dashboard);
        menu.add(clientes);
        menu.add(habitaciones);
        menu.add(reservas);
        menu.add(reportes);

        menu.add(Box.createVerticalGlue());

        JButton cerrar = crearBoton("↪    Cerrar sesión");
        cerrar.addActionListener(e -> cerrarSesion());

        menu.add(cerrar);
        menu.add(Box.createVerticalStrut(20));

        return menu;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setMaximumSize(new Dimension(180, 40));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setBackground(new Color(248, 248, 248));
        return boton;
    }

    // =====================================================
    // DASHBOARD (DATOS REALES DE MYSQL)
    // =====================================================
    private void mostrarDashboard() {
        contenido.removeAll();
        JPanel panel = new JPanel(new BorderLayout(15, 15));

        JLabel titulo = new JLabel("Dashboard");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(titulo, BorderLayout.WEST);

        JLabel usuarioLbl = new JLabel("Admin  ▼");
        encabezado.add(usuarioLbl, BorderLayout.EAST);
        panel.add(encabezado, BorderLayout.NORTH);

        int totalHabitaciones = 0;
        int ocupadas = 0;
        int disponibles = 0;
        int totalClientes = 0;
        int totalReservas = 0;
        double ingresos = 0;

        try (Connection conn = Conexion.conectar()) {
            if (conn != null) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*), SUM(CASE WHEN estado='Ocupada' THEN 1 ELSE 0 END) FROM habitacion");
                if (rs.next()) {
                    totalHabitaciones = rs.getInt(1);
                    ocupadas = rs.getInt(2);
                    disponibles = totalHabitaciones - ocupadas;
                }

                rs = st.executeQuery("SELECT COUNT(*) FROM cliente");
                if (rs.next()) {
                    totalClientes = rs.getInt(1);
                }

                rs = st.executeQuery("SELECT COUNT(*), SUM(CASE WHEN estado='Confirmada' OR estado='Completada' THEN total ELSE 0 END) FROM reserva");
                if (rs.next()) {
                    totalReservas = rs.getInt(1);
                    ingresos = rs.getDouble(2);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 15, 0));
        tarjetas.add(crearTarjeta("Habitaciones", String.valueOf(totalHabitaciones), "habitaciones"));
        tarjetas.add(crearTarjeta("Ocupadas", String.valueOf(ocupadas), "habitaciones"));
        tarjetas.add(crearTarjeta("Disponibles", String.valueOf(disponibles), "habitaciones"));
        tarjetas.add(crearTarjeta("Clientes", String.valueOf(totalClientes), "registrados"));

        JPanel centro = new JPanel(new GridLayout(2, 2, 15, 15));

        centro.add(crearPanelDashboard("Ocupación", "Habitaciones ocupadas: " + ocupadas + "\nDisponibles: " + disponibles));
        centro.add(crearPanelDashboard("Reservas", "Reservas registradas: " + totalReservas));
        centro.add(crearPanelDashboard("Ingresos", String.format("Ingresos acumulados: S/ %.2f", ingresos)));

        StringBuilder actividadesSb = new StringBuilder();
        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT descripcion FROM actividad ORDER BY id DESC LIMIT 5")) {
            while (rs.next()) {
                actividadesSb.append("• ").append(rs.getString("descripcion")).append("\n");
            }
        } catch (SQLException e) {
            actividadesSb.append("• Sin actividad reciente.");
        }

        centro.add(crearPanelDashboard("Actividades recientes", actividadesSb.toString()));

        panel.add(tarjetas, BorderLayout.CENTER);
        panel.add(centro, BorderLayout.SOUTH);

        contenido.add(panel);
        contenido.revalidate();
        contenido.repaint();
    }

    private JPanel crearTarjeta(String titulo, String numero, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel numeroLabel = new JLabel(numero);
        numeroLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel descripcionLabel = new JLabel(descripcion);
        descripcionLabel.setForeground(Color.GRAY);

        panel.add(tituloLabel, BorderLayout.NORTH);
        panel.add(numeroLabel, BorderLayout.CENTER);
        panel.add(descripcionLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelDashboard(String titulo, String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JTextArea contenidoTexto = new JTextArea(texto);
        contenidoTexto.setEditable(false);
        contenidoTexto.setBackground(panel.getBackground());
        contenidoTexto.setFont(new Font("SansSerif", Font.PLAIN, 14));

        panel.add(tituloLabel, BorderLayout.NORTH);
        panel.add(contenidoTexto, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================
    // CLIENTES (CONEXIÓN MYSQL Y FILTROS EN TIEMPO REAL)
    // =====================================================
    private void mostrarClientes() {
        contenido.removeAll();
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Clientes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton agregar = new JButton("+ Nuevo cliente");
        agregar.addActionListener(e -> agregarCliente());

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(titulo, BorderLayout.WEST);
        superior.add(agregar, BorderLayout.EAST);

        String[] columnas = {"ID", "Nombre", "Documento", "Teléfono"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre, dni, telefono FROM cliente")) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("telefono")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabla);

        JButton eliminar = new JButton("Eliminar cliente");
        eliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente.");
                return;
            }

            int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            try (Connection conn = Conexion.conectar();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM cliente WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();

                registrarActividad(conn, "Cliente eliminado ID: " + id);
                JOptionPane.showMessageDialog(this, "Cliente eliminado con éxito.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se puede eliminar el cliente porque tiene reservas asociadas.");
            }

            mostrarClientes();
        });

        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inferior.add(eliminar);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);

        contenido.add(panel);
        contenido.revalidate();
        contenido.repaint();
    }

    private void agregarCliente() {
        JTextField txtNombre = new JTextField();
        JTextField txtDocumento = new JTextField();
        JTextField txtTelefono = new JTextField();

        // FILTRO 1: DNI (Solo números y máximo 8 dígitos)
        ((javax.swing.text.AbstractDocument) txtDocumento.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                if (string.matches("\\d*") && (fb.getDocument().getLength() + string.length()) <= 8) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            public void replace(FilterBypass fb, int offset, int length, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                if (string.matches("\\d*") && (fb.getDocument().getLength() - length + string.length()) <= 8) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });

        // FILTRO 2: Teléfono (Inicia con 9 y máximo 9 dígitos)
        ((javax.swing.text.AbstractDocument) txtTelefono.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String textoFuturo = textoActual.substring(0, offset) + string + textoActual.substring(offset);
                
                if (string.matches("\\d*") && textoFuturo.matches("^9\\d{0,8}$")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            public void replace(FilterBypass fb, int offset, int length, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String textoFuturo = textoActual.substring(0, offset) + string + textoActual.substring(offset + length);
                
                if (string.matches("\\d*") && textoFuturo.matches("^9\\d{0,8}$")) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });

        // FILTRO 3: Nombre (Solo letras y espacios)
        ((javax.swing.text.AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                if (string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            public void replace(FilterBypass fb, int offset, int length, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string == null) return;
                if (string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });

        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Documento (DNI - 8 dígitos):"));
        formulario.add(txtDocumento);
        formulario.add(new JLabel("Teléfono (9 dígitos, inicia con 9):"));
        formulario.add(txtTelefono);

        int resultado = JOptionPane.showConfirmDialog(
                this, formulario, "Nuevo cliente", JOptionPane.OK_CANCEL_OPTION
        );

        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            String documento = txtDocumento.getText().trim();
            String telefono = txtTelefono.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (documento.length() < 8) {
                JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!telefono.matches("^9\\d{8}$")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe empezar con 9 y tener exactamente 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar en la base de datos si ya existe el DNI o el Teléfono antes de insertar
            try (Connection conn = Conexion.conectar()) {
                
                // 1. Verificar si el DNI ya existe
                PreparedStatement psCheckDni = conn.prepareStatement("SELECT COUNT(*) FROM cliente WHERE dni = ?");
                psCheckDni.setString(1, documento);
                ResultSet rsDni = psCheckDni.executeQuery();
                if (rsDni.next() && rsDni.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "El número de DNI ya se encuentra registrado en el sistema.", "DNI Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 2. Verificar si el Teléfono ya existe
                PreparedStatement psCheckTel = conn.prepareStatement("SELECT COUNT(*) FROM cliente WHERE telefono = ?");
                psCheckTel.setString(1, telefono);
                ResultSet rsTel = psCheckTel.executeQuery();
                if (rsTel.next() && rsTel.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "El número de teléfono ya se encuentra registrado para otro cliente.", "Teléfono Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 3. Si pasa las validaciones de duplicados, insertar en MySQL
                PreparedStatement psInsert = conn.prepareStatement("INSERT INTO cliente (nombre, dni, telefono) VALUES (?, ?, ?)");
                psInsert.setString(1, nombre);
                psInsert.setString(2, documento);
                psInsert.setString(3, telefono);
                psInsert.executeUpdate();

                registrarActividad(conn, "Nuevo cliente registrado: " + nombre);
                JOptionPane.showMessageDialog(this, "Cliente guardado en MySQL con éxito.");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar el cliente en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            mostrarClientes();
        }
    }

    // =====================================================
    // HABITACIONES (CONEXIÓN MYSQL)
    // =====================================================
    private void mostrarHabitaciones() {
        contenido.removeAll();
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Habitaciones");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        String[] columnas = {"ID", "Número", "Tipo", "Precio", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, numero, tipo, precio, estado FROM habitacion")) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("numero"),
                        rs.getString("tipo"),
                        "S/ " + rs.getDouble("precio"),
                        rs.getString("estado")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabla);

        JButton cambiar = new JButton("Cambiar estado");
        cambiar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una habitación.");
                return;
            }

            int idHabitacion = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
            String numeroHabitacion = tabla.getValueAt(fila, 1).toString();

            String[] estados = {"Disponible", "Ocupada", "Mantenimiento"};
            String estado = (String) JOptionPane.showInputDialog(
                    this, "Selecciona el nuevo estado:", "Estado",
                    JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]
            );

            if (estado == null) return;

            try (Connection conn = Conexion.conectar();
                 PreparedStatement ps = conn.prepareStatement("UPDATE habitacion SET estado = ? WHERE id = ?")) {
                ps.setString(1, estado);
                ps.setInt(2, idHabitacion);
                ps.executeUpdate();

                registrarActividad(conn, "Habitación " + numeroHabitacion + " cambió a " + estado);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            mostrarHabitaciones();
        });

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(titulo, BorderLayout.WEST);
        superior.add(cambiar, BorderLayout.EAST);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        contenido.add(panel);
        contenido.revalidate();
        contenido.repaint();
    }

    // =====================================================
    // RESERVAS (CONEXIÓN MYSQL)
    // =====================================================
    private void mostrarReservas() {
        contenido.removeAll();
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Reservas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton nueva = new JButton("+ Nueva reserva");
        nueva.addActionListener(e -> crearReserva());

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(titulo, BorderLayout.WEST);
        superior.add(nueva, BorderLayout.EAST);

        String[] columnas = {"ID", "Cliente", "Habitación", "Check-in", "Check-out", "Total", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        String sqlQuery = "SELECT r.id, c.nombre AS cliente, h.numero AS habitacion, r.fecha_inicio, r.fecha_fin, r.total, r.estado " +
                          "FROM reserva r JOIN cliente c ON r.cliente_id = c.id JOIN habitacion h ON r.habitacion_id = h.id";

        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlQuery)) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getString("habitacion"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        "S/ " + rs.getDouble("total"),
                        rs.getString("estado")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabla);

        JButton cancelar = new JButton("Cancelar reserva");
        cancelar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una reserva.");
                return;
            }

            int idReserva = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            try (Connection conn = Conexion.conectar();
                 PreparedStatement ps = conn.prepareStatement("UPDATE reserva SET estado = 'Cancelada' WHERE id = ?")) {
                ps.setInt(1, idReserva);
                ps.executeUpdate();

                registrarActividad(conn, "Reserva ID " + idReserva + " cancelada");
                JOptionPane.showMessageDialog(this, "Reserva cancelada con éxito.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            mostrarReservas();
        });

        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inferior.add(cancelar);

        panel.add(superior, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);

        contenido.add(panel);
        contenido.revalidate();
        contenido.repaint();
    }

    private void crearReserva() {
        JComboBox<String> clienteCombo = new JComboBox<>();
        JComboBox<String> habitacionCombo = new JComboBox<>();

        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre FROM cliente")) {
            while (rs.next()) {
                clienteCombo.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (clienteCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Primero debes registrar un cliente en la base de datos.");
            return;
        }

        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, numero, tipo, precio FROM habitacion WHERE estado = 'Disponible'")) {
            while (rs.next()) {
                habitacionCombo.addItem(rs.getInt("id") + " - Hab " + rs.getString("numero") + " (" + rs.getString("tipo") + ") - S/ " + rs.getDouble("precio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (habitacionCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay habitaciones disponibles.");
            return;
        }

        JTextField noches = new JTextField("1");

        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        formulario.add(new JLabel("Cliente:"));
        formulario.add(clienteCombo);
        formulario.add(new JLabel("Habitación:"));
        formulario.add(habitacionCombo);
        formulario.add(new JLabel("Número de noches:"));
        formulario.add(noches);

        int resultado = JOptionPane.showConfirmDialog(
                this, formulario, "Nueva reserva", JOptionPane.OK_CANCEL_OPTION
        );

        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            int numeroNoches = Integer.parseInt(noches.getText().trim());
            if (numeroNoches <= 0) throw new NumberFormatException();

            String clienteSeleccionado = (String) clienteCombo.getSelectedItem();
            int clienteId = Integer.parseInt(clienteSeleccionado.split(" - ")[0]);

            String habitacionSeleccionada = (String) habitacionCombo.getSelectedItem();
            int habitacionId = Integer.parseInt(habitacionSeleccionada.split(" - ")[0]);

            double precioHabitacion = 0;
            try (Connection conn = Conexion.conectar();
                 PreparedStatement ps = conn.prepareStatement("SELECT precio FROM habitacion WHERE id = ?")) {
                ps.setInt(1, habitacionId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    precioHabitacion = rs.getDouble("precio");
                }
            }

            double total = precioHabitacion * numeroNoches;
            LocalDate checkIn = LocalDate.now();
            LocalDate checkOut = checkIn.plusDays(numeroNoches);

            try (Connection conn = Conexion.conectar()) {
                String sqlReserva = "INSERT INTO reserva (cliente_id, habitacion_id, fecha_inicio, fecha_fin, total, estado) VALUES (?, ?, ?, ?, ?, 'Confirmada')";
                PreparedStatement psReserva = conn.prepareStatement(sqlReserva);
                psReserva.setInt(1, clienteId);
                psReserva.setInt(2, habitacionId);
                psReserva.setDate(3, java.sql.Date.valueOf(checkIn));
                psReserva.setDate(4, java.sql.Date.valueOf(checkOut));
                psReserva.setDouble(5, total);
                psReserva.executeUpdate();

                PreparedStatement psHab = conn.prepareStatement("UPDATE habitacion SET estado = 'Ocupada' WHERE id = ?");
                psHab.setInt(1, habitacionId);
                psHab.executeUpdate();

                registrarActividad(conn, "Nueva reserva registrada para ID Cliente: " + clienteId);
            }

            JOptionPane.showMessageDialog(this, "Reserva creada correctamente. Total: S/ " + String.format("%.2f", total));
            mostrarReservas();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El número de noches no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // REPORTES
    // =====================================================
    private void mostrarReportes() {
        contenido.removeAll();
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Reportes");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        int totalClientes = 0;
        int totalHabitaciones = 0;
        int habitacionesOcupadas = 0;
        int reservasConfirmadas = 0;
        int reservasCanceladas = 0;
        double ingresos = 0;

        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement()) {
             
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM cliente");
            if (rs.next()) totalClientes = rs.getInt(1);

            rs = st.executeQuery("SELECT COUNT(*), SUM(CASE WHEN estado='Ocupada' THEN 1 ELSE 0 END) FROM habitacion");
            if (rs.next()) {
                totalHabitaciones = rs.getInt(1);
                habitacionesOcupadas = rs.getInt(2);
            }

            rs = st.executeQuery("SELECT SUM(CASE WHEN estado='Confirmada' OR estado='Completada' THEN 1 ELSE 0 END), " +
                                 "SUM(CASE WHEN estado='Cancelada' THEN 1 ELSE 0 END), " +
                                 "SUM(CASE WHEN estado='Confirmada' OR estado='Completada' THEN total ELSE 0 END) FROM reserva");
            if (rs.next()) {
                reservasConfirmadas = rs.getInt(1);
                reservasCanceladas = rs.getInt(2);
                ingresos = rs.getDouble(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTextArea reporte = new JTextArea();
        reporte.setEditable(false);
        reporte.setFont(new Font("Monospaced", Font.PLAIN, 15));

        reporte.setText(
                "====================================\n" +
                "        REPORTE COCOS HOTEL\n" +
                "====================================\n\n" +
                "CLIENTES\n" +
                "------------------------------------\n" +
                "Total de clientes: " + totalClientes + "\n\n" +
                "HABITACIONES\n" +
                "------------------------------------\n" +
                "Total: " + totalHabitaciones + "\n" +
                "Ocupadas: " + habitacionesOcupadas + "\n" +
                "Disponibles: " + (totalHabitaciones - habitacionesOcupadas) + "\n\n" +
                "RESERVAS\n" +
                "------------------------------------\n" +
                "Confirmadas: " + reservasConfirmadas + "\n" +
                "Canceladas: " + reservasCanceladas + "\n\n" +
                "INGRESOS\n" +
                "------------------------------------\n" +
                String.format("Total: S/ %.2f", ingresos) + "\n"
        );

        JScrollPane scroll = new JScrollPane(reporte);
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        contenido.add(panel);
        contenido.revalidate();
        contenido.repaint();
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    private void registrarActividad(Connection conn, String descripcion) {
        String sql = "INSERT INTO actividad (descripcion, fecha_hora, usuario) VALUES (?, NOW(), 'Admin')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descripcion);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}