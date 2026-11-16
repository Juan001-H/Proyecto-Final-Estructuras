import estructuras.ListaEnlazada;
import modelos.Billetera;
import modelos.Transaccion;
import modelos.Usuario;
import servicios.Analizador;
import servicios.GestorBilleteras;
import servicios.GestorNotificaciones.Notificacion;
import servicios.GestorReversiones;
import servicios.GestorUsuarios;
import servicios.MotorTransaccional;
import servicios.ProgramadorTareas;

public class App {
    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("   PRUEBA INTEGRAL - PLATAFORMA DE BILLETERAS");
        System.out.println("=====================================================\n");

        // 1. Inicializar servicios
        GestorUsuarios gestorUsuarios = new GestorUsuarios(20);
        GestorBilleteras gestorBilleteras = new GestorBilleteras(50);
        MotorTransaccional motor = new MotorTransaccional();
        GestorReversiones gestorReversiones = new GestorReversiones();
        ProgramadorTareas programadorTareas = new ProgramadorTareas();
        Analizador analizador = new Analizador();

        // 2. Registrar usuarios
        System.out.println("--- 1. REGISTRO DE USUARIOS ---");
        gestorUsuarios.registrarUsuario("U01", "Tomas Suaza");
        gestorUsuarios.registrarUsuario("U02", "Juan Esteban");
        gestorUsuarios.registrarUsuario("U03", "Camila Restrepo");
        // Prueba de duplicado (debe bloquearse)
        gestorUsuarios.registrarUsuario("U01", "Tomas Duplicado");

        Usuario u1 = gestorUsuarios.buscarUsuario("U01");
        Usuario u2 = gestorUsuarios.buscarUsuario("U02");
        Usuario u3 = gestorUsuarios.buscarUsuario("U03");

        // 3. Crear billeteras
        System.out.println("\n--- 2. CREACIÓN DE BILLETERAS ---");
        gestorBilleteras.crearBilletera(u1, "BIL-101", "Ahorros", 2000.00);
        gestorBilleteras.crearBilletera(u1, "BIL-102", "Gastos diarios", 500.00);
        gestorBilleteras.crearBilletera(u2, "BIL-201", "Ahorros", 1500.00);
        gestorBilleteras.crearBilletera(u3, "BIL-301", "Inversión", 8000.00);

        Billetera b101 = gestorBilleteras.buscarBilletera("BIL-101");
        Billetera b201 = gestorBilleteras.buscarBilletera("BIL-201");
        Billetera b301 = gestorBilleteras.buscarBilletera("BIL-301");

        // 4. Operaciones financieras (Depósitos, Retiros, Transferencias y Antifraude)
        System.out.println("\n--- 3. OPERACIONES Y SISTEMA DE PUNTOS ---");
        // Depósito (1 pt por cada 100 -> +10 pts)
        motor.depositar(u1, b101, 1000.00, "TX-001");

        // Transferencia normal (3 pts por cada 100 -> +45 pts)
        if (motor.transferir(u1, b101, u2, b201, 1500.00, "TX-002")) {
            gestorReversiones.registrarTransaccionParaReversion(
                new Transaccion("TX-002", "TRANSFERENCIA_ENVIADA", 1500.00, "BIL-101", "BIL-201")
            );
        }

        // Transferencia alta (> $5000.00 -> Dispara alerta antifraude y sube nivel)
        if (motor.transferir(u3, b301, u1, b101, 6000.00, "TX-003")) {
            gestorReversiones.registrarTransaccionParaReversion(
                new Transaccion("TX-003", "TRANSFERENCIA_ENVIADA", 6000.00, "BIL-301", "BIL-101")
            );
        }

        // Retiro (2 pts por cada 100 -> +6 pts)
        if (motor.retirar(u2, b201, 300.00, "TX-004")) {
            gestorReversiones.registrarTransaccionParaReversion(
                new Transaccion("TX-004", "RETIRO", 300.00, "BIL-201", "CAJERO/EXTERNO")
            );
        }

        // 5. Prueba de Reversión (Pila LIFO)
        System.out.println("\n--- 4. REVERSIÓN DE ÚLTIMA OPERACIÓN (PILA) ---");
        System.out.println("Saldo BIL-201 antes de revertir: $" + b201.getSaldoFormateado() + " | Puntos Juan: " + u2.getPuntosAcumulados());
        gestorReversiones.revertirUltimaOperacionCompleta(gestorUsuarios, gestorBilleteras, motor.getGestorNotificaciones());
        System.out.println("Saldo BIL-201 después de revertir: $" + b201.getSaldoFormateado() + " | Puntos Juan: " + u2.getPuntosAcumulados());

        // 6. Tareas programadas (Cola de Prioridad)
        System.out.println("\n--- 5. TAREAS PROGRAMADAS (COLA DE PRIORIDAD) ---");
        programadorTareas.programarTarea("T-01", "Pago factura internet", 2);
        programadorTareas.programarTarea("T-02", "Cuota crédito urgente", 1);
        programadorTareas.programarTarea("T-03", "Ahorro mensual", 3);

        while (programadorTareas.hayTareasPendientes()) {
            System.out.println("Ejecutando tarea -> " + programadorTareas.procesarSiguienteTarea());
        }

        // 7. Notificaciones y Alertas generadas
        System.out.println("\n--- 6. NOTIFICACIONES Y ALERTAS ---");
        ListaEnlazada<Notificacion> alertas = motor.getGestorNotificaciones().getHistorialNotificaciones();
        for (int i = 0; i < alertas.getSize(); i++) {
            Notificacion n = alertas.obtener(i);
            System.out.println("[" + n.getFecha() + "] " + n.getMensaje());
        }

        // 8. Reporte Analítico (Árbol Binario de Puntos y Grafo de Transferencias)
        System.out.println("\n" + analizador.generarReporteGeneral(gestorUsuarios.getListaUsuarios(), motor.getGrafoTransferencias()));
    }
}