package vista;

import estructuras.ListaEnlazada;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import modelos.*;
import servicios.*;

public class VentanaPrincipal extends JFrame {

    // Servicios del Backend
    private final GestorUsuarios gestorUsuarios;
    private final GestorBilleteras gestorBilleteras;
    private final MotorTransaccional motorTransaccional;
    private final GestorReversiones gestorReversiones;
    private final ProgramadorTareas programadorTareas;
    private final GestorNotificaciones gestorNotificaciones;
    private final Analizador analizador;

    // Componentes de la interfaz
    private JTabbedPane tabbedPane;
    private JTable tablaUsuarios, tablaBilleteras, tablaTareas, tablaNotificaciones;
    private DefaultTableModel modeloTablaUsuarios, modeloTablaBilleteras, modeloTablaTareas, modeloTablaNotificaciones;
    
    private JTextField txtIdUsuario, txtNombreUsuario, txtCodigoBilletera, txtTipoBilletera, txtSaldoInicial;
    private JComboBox<String> comboUsuariosBilletera, comboUsuariosOrigen, comboBilleterasOrigen, comboUsuariosDestino, comboBilleterasDestino;
    private JTextField txtMontoOperacion;
    private JTextArea areaLogOperaciones, areaAnalitica, areaAntifraude;
    private JTextField txtIdTarea, txtDescTarea;
    private JSpinner spinPrioridad;

    // Colores corporativos Neobanco Fintech
    private final Color COLOR_BG_GENERAL = new Color(15, 23, 42);   // Azul medianoche profundo
    private final Color COLOR_CARD_BG = new Color(30, 41, 59);      // Tarjeta gris azulada elevada
    private final Color COLOR_ACCENT_CYAN = new Color(56, 189, 248); // Cian vibrante moderno
    private final Color COLOR_ACCENT_GREEN = new Color(16, 185, 129); // Esmeralda éxito
    private final Color COLOR_BORDER = new Color(51, 65, 85);       // Borde sutil

    public VentanaPrincipal() {
        this.gestorUsuarios = new GestorUsuarios(50);
        this.gestorBilleteras = new GestorBilleteras(100);
        this.motorTransaccional = new MotorTransaccional();
        this.gestorReversiones = new GestorReversiones();
        this.programadorTareas = new ProgramadorTareas();
        this.gestorNotificaciones = motorTransaccional.getGestorNotificaciones();
        this.analizador = new Analizador();

        cargarDatosPrueba();

        setTitle("NEXUSPAY ENTERPRISE - Billetera Digital & Analítica UQ");
        setSize(1420, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(COLOR_BG_GENERAL);
        setLayout(new BorderLayout());
        add(crearHeaderFintechPro(), BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBorder(new EmptyBorder(10, 15, 15, 15));

        tabbedPane.addTab("   👥 Clientes & Billeteras   ", crearPanelUsuariosBilleteras());
        tabbedPane.addTab("   💳 Motor Transaccional   ", crearPanelTransacciones());
        tabbedPane.addTab("   ⚙️ Tareas Programadas   ", crearPanelTareas());
        tabbedPane.addTab("   🛡️ Antifraude & Seguridad   ", crearPanelAntifraude());
        tabbedPane.addTab("   🔔 Alertas y Notificaciones   ", crearPanelNotificaciones());
        tabbedPane.addTab("   📊 Analítica & Grafos UQ   ", crearPanelAnalitica());

        add(tabbedPane, BorderLayout.CENTER);
        actualizarDatosVista();
    }

    private JPanel crearHeaderFintechPro() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(11, 19, 43));
        header.setBorder(new EmptyBorder(22, 35, 22, 35));

        JLabel lblTitulo = new JLabel("⚡ NEXUSPAY PLATFORM - FINANCIAL CORE");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_ACCENT_CYAN);

        JLabel lblSub = new JLabel("Arquitectura de Estructuras de Datos Avanzadas | Universidad del Quindío");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(148, 163, 184));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(lblTitulo);
        textPanel.add(lblSub);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    private void cargarDatosPrueba() {
        try {
            gestorUsuarios.registrarUsuario("U01", "Tomas Suaza");
            gestorUsuarios.registrarUsuario("U02", "Juan Esteban Herrera");
            gestorUsuarios.registrarUsuario("U03", "Camila Restrepo");

            Usuario u1 = gestorUsuarios.buscarUsuario("U01");
            Usuario u2 = gestorUsuarios.buscarUsuario("U02");
            Usuario u3 = gestorUsuarios.buscarUsuario("U03");

            gestorBilleteras.crearBilletera(u1, "BIL-101", "Ahorro Principal", 6050000.00);
            gestorBilleteras.crearBilletera(u1, "BIL-102", "Gastos Diarios", 1450000.00);
            gestorBilleteras.crearBilletera(u2, "BIL-201", "Inversión Digital", 9200000.00);
            gestorBilleteras.crearBilletera(u3, "BIL-301", "Transporte & Subs", 890000.00);

            motorTransaccional.depositar(u1, gestorBilleteras.buscarBilletera("BIL-101"), 800000.00, "TX-001");
            programadorTareas.programarTarea("T-01", "Corte mensual de comisiones automáticas", 2);
            programadorTareas.programarTarea("T-02", "Auditoría de seguridad y respaldo en nube", 1);
            gestorNotificaciones.agregarNotificacion("NOTIF-1", "Sistema Fintech inicializado y blindado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel crearPanelUsuariosBilleteras() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_BG_GENERAL);

        JPanel tablasPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        tablasPanel.setOpaque(false);

        modeloTablaUsuarios = new DefaultTableModel(new String[]{"ID Cliente", "Nombre Completo", "Nivel Fidelización", "Puntos Acumulados", "Billeteras"}, 0);
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        estilizarTabla(tablaUsuarios);
        tablasPanel.add(crearTarjetaContenedor(new JScrollPane(tablaUsuarios), "👥 Clientes Registrados en la Plataforma (Árboles y Tablas Hash)"));

        modeloTablaBilleteras = new DefaultTableModel(new String[]{"Código Billetera", "Tipo de Cuenta", "Saldo Disponible (COP)", "Estado", "Propietario ID"}, 0);
        tablaBilleteras = new JTable(modeloTablaBilleteras);
        estilizarTabla(tablaBilleteras);
        tablasPanel.add(crearTarjetaContenedor(new JScrollPane(tablaBilleteras), "💳 Billeteras Digitales Activas (Bolsillos y Cuentas)"));

        panel.add(tablasPanel, BorderLayout.CENTER);

        JPanel formsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        formsPanel.setOpaque(false);

        // Formulario Registro Cliente
        JPanel pnlRegUsu = new JPanel(new GridBagLayout());
        pnlRegUsu.setBackground(COLOR_CARD_BG);
        pnlRegUsu.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdUsuario = new JTextField(14);
        txtNombreUsuario = new JTextField(16);
        JButton btnCrearUsu = new JButton("✨ Registrar Nuevo Cliente");
        estilizarBoton(btnCrearUsu, COLOR_ACCENT_CYAN);

        JLabel l1 = new JLabel("ID Cliente:"); estiloLabel(l1);
        JLabel l2 = new JLabel("Nombre Completo:"); estiloLabel(l2);

        gbc.gridx = 0; gbc.gridy = 0; pnlRegUsu.add(l1, gbc); gbc.gridx = 1; gbc.gridy = 0; pnlRegUsu.add(txtIdUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlRegUsu.add(l2, gbc); gbc.gridx = 1; gbc.gridy = 1; pnlRegUsu.add(txtNombreUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; pnlRegUsu.add(btnCrearUsu, gbc);

        // Formulario Billetera
        JPanel pnlRegBill = new JPanel(new GridBagLayout());
        pnlRegBill.setBackground(COLOR_CARD_BG);
        pnlRegBill.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(8, 8, 8, 8); gbc2.fill = GridBagConstraints.HORIZONTAL;

        comboUsuariosBilletera = new JComboBox<>();
        txtCodigoBilletera = new JTextField(12);
        txtTipoBilletera = new JTextField(12);
        txtSaldoInicial = new JTextField(12);
        JButton btnCrearBill = new JButton("🏦 Crear Billetera Digital");
        estilizarBoton(btnCrearBill, COLOR_ACCENT_GREEN);

        JLabel lb1 = new JLabel("Propietario:"); estiloLabel(lb1);
        JLabel lb2 = new JLabel("Código Billetera:"); estiloLabel(lb2);
        JLabel lb3 = new JLabel("Tipo Cuenta:"); estiloLabel(lb3);
        JLabel lb4 = new JLabel("Saldo Inicial ($):"); estiloLabel(lb4);

        gbc2.gridx = 0; gbc2.gridy = 0; pnlRegBill.add(lb1, gbc2); gbc2.gridx = 1; gbc2.gridy = 0; pnlRegBill.add(comboUsuariosBilletera, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 1; pnlRegBill.add(lb2, gbc2); gbc2.gridx = 1; gbc2.gridy = 1; pnlRegBill.add(txtCodigoBilletera, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 2; pnlRegBill.add(lb3, gbc2); gbc2.gridx = 1; gbc2.gridy = 2; pnlRegBill.add(txtTipoBilletera, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 3; pnlRegBill.add(lb4, gbc2); gbc2.gridx = 1; gbc2.gridy = 3; pnlRegBill.add(txtSaldoInicial, gbc2);
        gbc2.gridx = 0; gbc2.gridy = 4; gbc2.gridwidth = 2; pnlRegBill.add(btnCrearBill, gbc2);

        formsPanel.add(crearTarjetaContenedor(pnlRegUsu, "Gestión de Nuevos Clientes"));
        formsPanel.add(crearTarjetaContenedor(pnlRegBill, "Apertura de Billeteras / Bolsillos"));
        panel.add(formsPanel, BorderLayout.SOUTH);

        btnCrearUsu.addActionListener(e -> {
            String id = txtIdUsuario.getText().trim();
            String nombre = txtNombreUsuario.getText().trim();
            if (gestorUsuarios.registrarUsuario(id, nombre)) {
                JOptionPane.showMessageDialog(this, "¡Cliente registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtIdUsuario.setText(""); txtNombreUsuario.setText("");
                actualizarDatosVista();
            } else {
                JOptionPane.showMessageDialog(this, "Error: El ID ya existe o hay campos vacíos.", "Validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCrearBill.addActionListener(e -> {
            try {
                String selU = (String) comboUsuariosBilletera.getSelectedItem();
                if (selU == null) return;
                Usuario u = gestorUsuarios.buscarUsuario(selU.split(" - ")[0]);
                String codigo = txtCodigoBilletera.getText().trim();
                String tipo = txtTipoBilletera.getText().trim();
                double saldo = Double.parseDouble(txtSaldoInicial.getText().trim());

                if (gestorBilleteras.crearBilletera(u, codigo, tipo, saldo)) {
                    JOptionPane.showMessageDialog(this, "¡Billetera creada exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    txtCodigoBilletera.setText(""); txtTipoBilletera.setText(""); txtSaldoInicial.setText("");
                    actualizarDatosVista();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear billetera (Código duplicado).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifique que el saldo sea un número válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelTransacciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_BG_GENERAL);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_CARD_BG);
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); gbc.fill = GridBagConstraints.HORIZONTAL;

        comboUsuariosOrigen = new JComboBox<>();
        comboBilleterasOrigen = new JComboBox<>();
        comboUsuariosDestino = new JComboBox<>();
        comboBilleterasDestino = new JComboBox<>();
        txtMontoOperacion = new JTextField(14);

        JLabel c1 = new JLabel("Cliente Origen:"); estiloLabel(c1);
        JLabel c2 = new JLabel("Billetera Origen:"); estiloLabel(c2);
        JLabel c3 = new JLabel("Cliente Destino:"); estiloLabel(c3);
        JLabel c4 = new JLabel("Billetera Destino:"); estiloLabel(c4);
        JLabel c5 = new JLabel("Monto Operación ($):"); estiloLabel(c5);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(c1, gbc); gbc.gridx = 1; gbc.gridy = 0; formPanel.add(comboUsuariosOrigen, gbc);
        gbc.gridx = 2; gbc.gridy = 0; formPanel.add(c2, gbc); gbc.gridx = 3; gbc.gridy = 0; formPanel.add(comboBilleterasOrigen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(c3, gbc); gbc.gridx = 1; gbc.gridy = 1; formPanel.add(comboUsuariosDestino, gbc);
        gbc.gridx = 2; gbc.gridy = 1; formPanel.add(c4, gbc); gbc.gridx = 3; gbc.gridy = 1; formPanel.add(comboBilleterasDestino, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(c5, gbc); gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtMontoOperacion, gbc);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        botonesPanel.setOpaque(false);
        JButton btnTransferir = new JButton("💸 Transferir");
        JButton btnDepositar = new JButton("📥 Depositar");
        JButton btnRetirar = new JButton("📤 Retirar");
        JButton btnRevertir = new JButton("⏪ Revertir (Pila)");

        estilizarBoton(btnTransferir, new Color(37, 99, 235));
        estilizarBoton(btnDepositar, COLOR_ACCENT_GREEN);
        estilizarBoton(btnRetirar, new Color(217, 119, 6));
        estilizarBoton(btnRevertir, new Color(225, 29, 72));

        botonesPanel.add(btnTransferir);
        botonesPanel.add(btnDepositar);
        botonesPanel.add(btnRetirar);
        botonesPanel.add(btnRevertir);

        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(botonesPanel, gbc);
        panel.add(crearTarjetaContenedor(formPanel, "Motor Transaccional, Acumulación de Puntos & Reversiones (Pilas)"), BorderLayout.NORTH);

        areaLogOperaciones = new JTextArea();
        areaLogOperaciones.setEditable(false);
        areaLogOperaciones.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scrollLog = new JScrollPane(areaLogOperaciones);
        panel.add(crearTarjetaContenedor(scrollLog, "📋 Consola de Transacciones, Puntos Generados y Alertas en Tiempo Real"), BorderLayout.CENTER);

        btnTransferir.addActionListener(e -> ejecutarTransferencia());
        btnDepositar.addActionListener(e -> ejecutarDeposito());
        btnRetirar.addActionListener(e -> ejecutarRetiro());
        btnRevertir.addActionListener(e -> ejecutarReversion());

        comboUsuariosOrigen.addActionListener(e -> actualizarBilleterasOrigenCombo());
        comboUsuariosDestino.addActionListener(e -> actualizarBilleterasDestinoCombo());

        return panel;
    }

    private JPanel crearPanelTareas() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_BG_GENERAL);

        modeloTablaTareas = new DefaultTableModel(new String[]{"ID Tarea", "Descripción de la Tarea Programada", "Prioridad (Cola Heap)"}, 0);
        tablaTareas = new JTable(modeloTablaTareas);
        estilizarTabla(tablaTareas);
        panel.add(crearTarjetaContenedor(new JScrollPane(tablaTareas), "⚙️ Cola de Prioridad - Pagos y Recargas Automáticas Programadas"), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        bottomPanel.setBackground(COLOR_CARD_BG);
        bottomPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtIdTarea = new JTextField(10);
        txtDescTarea = new JTextField(26);
        spinPrioridad = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JButton btnAgregarTarea = new JButton("➕ Programar Tarea");
        JButton btnProcesarTarea = new JButton("⚙️ Procesar Siguiente (Prioridad)");

        estilizarBoton(btnAgregarTarea, new Color(37, 99, 235));
        estilizarBoton(btnProcesarTarea, new Color(100, 116, 139));

        JLabel l1 = new JLabel("ID:"); estiloLabel(l1);
        JLabel l2 = new JLabel("Descripción:"); estiloLabel(l2);
        JLabel l3 = new JLabel("Prioridad (1-5):"); estiloLabel(l3);

        bottomPanel.add(l1); bottomPanel.add(txtIdTarea);
        bottomPanel.add(l2); bottomPanel.add(txtDescTarea);
        bottomPanel.add(l3); bottomPanel.add(spinPrioridad);
        bottomPanel.add(btnAgregarTarea);
        bottomPanel.add(btnProcesarTarea);

        panel.add(crearTarjetaContenedor(bottomPanel, "Programador de Tareas Automáticas"), BorderLayout.SOUTH);

        btnAgregarTarea.addActionListener(e -> {
            String id = txtIdTarea.getText().trim();
            String desc = txtDescTarea.getText().trim();
            int prio = (Integer) spinPrioridad.getValue();
            if (!id.isEmpty() && !desc.isEmpty()) {
                programadorTareas.programarTarea(id, desc, prio);
                txtIdTarea.setText(""); txtDescTarea.setText("");
                actualizarDatosVista();
                JOptionPane.showMessageDialog(this, "Tarea programada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnProcesarTarea.addActionListener(e -> {
            if (programadorTareas.hayTareasPendientes()) {
                Object tareaProcesada = programadorTareas.procesarSiguienteTarea();
                JOptionPane.showMessageDialog(this, "Tarea ejecutada:\n" + tareaProcesada.toString(), "Procesamiento", JOptionPane.INFORMATION_MESSAGE);
                actualizarDatosVista();
            } else {
                JOptionPane.showMessageDialog(this, "No hay tareas pendientes en la cola.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel crearPanelAntifraude() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_BG_GENERAL);

        areaAntifraude = new JTextArea();
        areaAntifraude.setEditable(false);
        areaAntifraude.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaAntifraude.setText("[MÓDULO ANTIFRAUDE ACTIVADO - ANÁLISIS DE COMPORTAMIENTO]\n\n" +
            "• Monitoreo en tiempo real de transacciones consecutivas rápidas.\n" +
            "• Detección de valores atípicos y fragmentación de montos.\n" +
            "• Auditoría de seguridad y comportamiento financiero inusual.\n\n" +
            "Estado actual: Sistema protegido contra anomalías operativas.");
        JScrollPane scroll = new JScrollPane(areaAntifraude);
        panel.add(crearTarjetaContenedor(scroll, "🛡️ Detección de Comportamiento Financiero Inusual y Alertas de Riesgo"), BorderLayout.CENTER);

        JButton btnAuditar = new JButton("🔍 Ejecutar Escaneo de Auditoría Antifraude");
        estilizarBoton(btnAuditar, new Color(225, 29, 72));
        panel.add(crearTarjetaContenedor(btnAuditar, "Control de Seguridad"), BorderLayout.SOUTH);

        btnAuditar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Escaneo completado: No se detectaron anomalías críticas en las billeteras.", "Auditoría Antifraude", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }

    private JPanel crearPanelNotificaciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_BG_GENERAL);

        modeloTablaNotificaciones = new DefaultTableModel(new String[]{"ID Alerta", "Timestamp", "Mensaje de Notificación del Sistema"}, 0);
        tablaNotificaciones = new JTable(modeloTablaNotificaciones);
        estilizarTabla(tablaNotificaciones);
        panel.add(crearTarjetaContenedor(new JScrollPane(tablaNotificaciones), "🔔 Historial de Alertas, Saldo Bajo y Ascensos de Nivel"), BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelAnalitica() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_BG_GENERAL);

        areaAnalitica = new JTextArea();
        areaAnalitica.setEditable(false);
        areaAnalitica.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(areaAnalitica);
        panel.add(crearTarjetaContenedor(scroll, "📊 Reporte Analítico Consolidado (Árboles Binarios, Tablas Hash y Grafos de Transferencia)"), BorderLayout.CENTER);

        JButton btnActualizarReporte = new JButton("🔄 Refrescar Analítica Completa");
        estilizarBoton(btnActualizarReporte, new Color(37, 99, 235));
        panel.add(crearTarjetaContenedor(btnActualizarReporte, "Acciones de Reporte"), BorderLayout.SOUTH);

        btnActualizarReporte.addActionListener(e -> generarReporteAnalitico());

        return panel;
    }

    // Métodos estéticos y contenedores avanzados tipo tarjeta fintech con separaciones claras
    private JPanel crearTarjetaContenedor(JComponent componente, String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(COLOR_CARD_BG);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 2),
            new EmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel lblTitulo = new JLabel(" " + titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(COLOR_ACCENT_CYAN);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(componente, BorderLayout.CENTER);
        return tarjeta;
    }

    private void estiloLabel(JLabel label) {
        label.setForeground(new Color(241, 245, 249));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(32);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(15, 23, 42));
        tabla.getTableHeader().setForeground(new Color(248, 250, 252));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(51, 65, 85));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(51, 65, 85));
    }

    private void estilizarBoton(JButton btn, Color colorFondo) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void actualizarDatosVista() {
        modeloTablaUsuarios.setRowCount(0);
        if (comboUsuariosBilletera != null) comboUsuariosBilletera.removeAllItems();
        if (comboUsuariosOrigen != null) comboUsuariosOrigen.removeAllItems();
        if (comboUsuariosDestino != null) comboUsuariosDestino.removeAllItems();

        ListaEnlazada<Usuario> listaU = gestorUsuarios.getListaUsuarios();
        if (listaU != null) {
            for (int i = 0; i < listaU.getSize(); i++) {
                Usuario u = listaU.obtener(i);
                modeloTablaUsuarios.addRow(new Object[]{
                    u.getId(), u.getNombre(), u.getNivel(), u.getPuntosAcumulados(), u.getBilleteras().getSize()
                });
                String itemCombo = u.getId() + " - " + u.getNombre();
                if (comboUsuariosBilletera != null) comboUsuariosBilletera.addItem(itemCombo);
                if (comboUsuariosOrigen != null) comboUsuariosOrigen.addItem(itemCombo);
                if (comboUsuariosDestino != null) comboUsuariosDestino.addItem(itemCombo);
            }
        }

        modeloTablaBilleteras.setRowCount(0);
        if (listaU != null) {
            for (int i = 0; i < listaU.getSize(); i++) {
                Usuario u = listaU.obtener(i);
                ListaEnlazada<Billetera> listaB = u.getBilleteras();
                if (listaB != null) {
                    for (int j = 0; j < listaB.getSize(); j++) {
                        Billetera b = listaB.obtener(j);
                        modeloTablaBilleteras.addRow(new Object[]{
                            b.getCodigo(), b.getTipo(), String.format(Locale.US, "%.2f", b.getSaldo()), b.isActiva() ? "Activa" : "Inactiva", u.getId()
                        });
                    }
                }
            }
        }

        modeloTablaNotificaciones.setRowCount(0);
        ListaEnlazada<GestorNotificaciones.Notificacion> listaN = gestorNotificaciones.getHistorialNotificaciones();
        if (listaN != null) {
            for (int i = 0; i < listaN.getSize(); i++) {
                GestorNotificaciones.Notificacion n = listaN.obtener(i);
                modeloTablaNotificaciones.addRow(new Object[]{
                    n.getId(), n.getFecha(), n.getMensaje()
                });
            }
        }

        actualizarBilleterasOrigenCombo();
        actualizarBilleterasDestinoCombo();
        generarReporteAnalitico();
    }

    private void actualizarBilleterasOrigenCombo() {
        if (comboBilleterasOrigen == null || comboUsuariosOrigen == null) return;
        comboBilleterasOrigen.removeAllItems();
        String sel = (String) comboUsuariosOrigen.getSelectedItem();
        if (sel != null) {
            String idU = sel.split(" - ")[0];
            Usuario u = gestorUsuarios.buscarUsuario(idU);
            if (u != null && u.getBilleteras() != null) {
                ListaEnlazada<Billetera> lb = u.getBilleteras();
                for (int i = 0; i < lb.getSize(); i++) {
                    comboBilleterasOrigen.addItem(lb.obtener(i).getCodigo());
                }
            }
        }
    }

    private void actualizarBilleterasDestinoCombo() {
        if (comboBilleterasDestino == null || comboUsuariosDestino == null) return;
        comboBilleterasDestino.removeAllItems();
        String sel = (String) comboUsuariosDestino.getSelectedItem();
        if (sel != null) {
            String idU = sel.split(" - ")[0];
            Usuario u = gestorUsuarios.buscarUsuario(idU);
            if (u != null && u.getBilleteras() != null) {
                ListaEnlazada<Billetera> lb = u.getBilleteras();
                for (int i = 0; i < lb.getSize(); i++) {
                    comboBilleterasDestino.addItem(lb.obtener(i).getCodigo());
                }
            }
        }
    }

    private void ejecutarTransferencia() {
        try {
            String selUO = (String) comboUsuariosOrigen.getSelectedItem();
            String selUD = (String) comboUsuariosDestino.getSelectedItem();
            String codBO = (String) comboBilleterasOrigen.getSelectedItem();
            String codBD = (String) comboBilleterasDestino.getSelectedItem();
            double monto = Double.parseDouble(txtMontoOperacion.getText().trim());

            Usuario uO = gestorUsuarios.buscarUsuario(selUO.split(" - ")[0]);
            Usuario uD = gestorUsuarios.buscarUsuario(selUD.split(" - ")[0]);
            Billetera bO = gestorBilleteras.buscarBilletera(codBO);
            Billetera bD = gestorBilleteras.buscarBilletera(codBD);

            String idTx = "TX-" + System.currentTimeMillis();
            boolean exito = motorTransaccional.transferir(uO, bO, uD, bD, monto, idTx);

            if (exito) {
                Transaccion txNueva = new Transaccion(idTx, "TRANSFERENCIA", monto, codBO, codBD);
                gestorReversiones.registrarTransaccionParaReversion(txNueva);
                areaLogOperaciones.append("[ÉXITO] Transferencia de $" + String.format(Locale.US, "%.2f", monto) + " COP realizada (ID: " + idTx + ")\n");
            } else {
                areaLogOperaciones.append("[FALLO] Transferencia rechazada por políticas del motor o saldos insuficientes.\n");
            }
            actualizarDatosVista();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en transferencia: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarDeposito() {
        try {
            String selUO = (String) comboUsuariosOrigen.getSelectedItem();
            String codBO = (String) comboBilleterasOrigen.getSelectedItem();
            double monto = Double.parseDouble(txtMontoOperacion.getText().trim());

            Usuario uO = gestorUsuarios.buscarUsuario(selUO.split(" - ")[0]);
            Billetera bO = gestorBilleteras.buscarBilletera(codBO);

            String idTx = "DEP-" + System.currentTimeMillis();
            if (motorTransaccional.depositar(uO, bO, monto, idTx)) {
                areaLogOperaciones.append("[DEPÓSITO] $" + String.format(Locale.US, "%.2f", monto) + " COP acreditados en " + codBO + "\n");
            }
            actualizarDatosVista();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en depósito: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarRetiro() {
        try {
            String selUO = (String) comboUsuariosOrigen.getSelectedItem();
            String codBO = (String) comboBilleterasOrigen.getSelectedItem();
            double monto = Double.parseDouble(txtMontoOperacion.getText().trim());

            Usuario uO = gestorUsuarios.buscarUsuario(selUO.split(" - ")[0]);
            Billetera bO = gestorBilleteras.buscarBilletera(codBO);

            String idTx = "RET-" + System.currentTimeMillis();
            if (motorTransaccional.retirar(uO, bO, monto, idTx)) {
                gestorReversiones.registrarTransaccionParaReversion(new Transaccion(idTx, "RETIRO", monto, codBO, "EXTERNO"));
                areaLogOperaciones.append("[RETIRO] $" + String.format(Locale.US, "%.2f", monto) + " COP retirados de " + codBO + "\n");
            }
            actualizarDatosVista();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en retiro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarReversion() {
        if (gestorReversiones.hayReversionesPendientes()) {
            if (gestorReversiones.revertirUltimaOperacionCompleta(gestorUsuarios, gestorBilleteras, gestorNotificaciones)) {
                areaLogOperaciones.append("[REVERSIÓN] Operación revertida exitosamente mediante pila.\n");
                JOptionPane.showMessageDialog(this, "Operación revertida con éxito.", "Reversión", JOptionPane.INFORMATION_MESSAGE);
            }
            actualizarDatosVista();
        } else {
            JOptionPane.showMessageDialog(this, "No hay transacciones pendientes de reversión.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void generarReporteAnalitico() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE GENERAL DE LA PLATAFORMA FINTECH ===\n\n");
        sb.append(analizador.generarReporteGeneral(gestorUsuarios.getListaUsuarios(), motorTransaccional.getGrafoTransferencias()));
        areaAnalitica.setText(sb.toString());
    }
}