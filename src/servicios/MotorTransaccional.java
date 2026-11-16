package servicios;

import estructuras.GrafoTransferencias;
import modelos.Billetera;
import modelos.Transaccion;
import modelos.Usuario;

public class MotorTransaccional {

    private DetectorFraude detectorFraude;
    private GestorNotificaciones gestorNotificaciones;
    private GrafoTransferencias grafoTransferencias;

    public MotorTransaccional() {
        this.detectorFraude = new DetectorFraude();
        this.gestorNotificaciones = new GestorNotificaciones();
        this.grafoTransferencias = new GrafoTransferencias(20);
    }

    public MotorTransaccional(DetectorFraude detectorFraude, GestorNotificaciones gestorNotificaciones, GrafoTransferencias grafoTransferencias) {
        this.detectorFraude = detectorFraude;
        this.gestorNotificaciones = gestorNotificaciones;
        this.grafoTransferencias = grafoTransferencias;
    }

    // Sobrecarga compatible con llamadas simples (sin pasar los objetos Usuario)
    public boolean transferir(Billetera origen, Billetera destino, double monto, String idTransaccion) {
        return transferir(null, origen, null, destino, monto, idTransaccion);
    }

    // Transferencia completa con puntos, antifraude, grafo y notificaciones
    public boolean transferir(Usuario usuarioOrigen, Billetera origen, Usuario usuarioDestino, Billetera destino, double monto, String idTransaccion) {
        if (origen == null || destino == null) {
            System.out.println("[ERROR TRANSACCIÓN] Billetera de origen o destino inválida.");
            return false;
        }

        if (origen.getCodigo().equals(destino.getCodigo())) {
            System.out.println("[ERROR TRANSACCIÓN] No se puede transferir a la misma billetera.");
            return false;
        }

        if (monto <= 0) {
            System.out.println("[ERROR TRANSACCIÓN] El monto a transferir debe ser mayor a cero.");
            return false;
        }

        if (!origen.isActiva() || !destino.isActiva()) {
            System.out.println("[ERROR TRANSACCIÓN] Una de las billeteras se encuentra inactiva o bloqueada.");
            return false;
        }

        if (origen.getSaldo() < monto) {
            System.out.println("[ERROR TRANSACCIÓN] Fondos insuficientes en la billetera origen: " + origen.getCodigo());
            gestorNotificaciones.agregarNotificacion("NOTIF-" + idTransaccion, "Transferencia rechazada por fondos insuficientes en " + origen.getCodigo());
            return false;
        }

        Transaccion txSalida = new Transaccion(idTransaccion, "TRANSFERENCIA_ENVIADA", monto, origen.getCodigo(), destino.getCodigo());
        Transaccion txEntrada = new Transaccion(idTransaccion + "-IN", "TRANSFERENCIA_RECIBIDA", monto, origen.getCodigo(), destino.getCodigo());

        // Auditoría antifraude antes/durante la operación
        if (detectorFraude.evaluarTransaccion(txSalida)) {
            gestorNotificaciones.agregarNotificacion("ALERTA-" + idTransaccion,
                    String.format("Alerta de seguridad: transferencia inusual de $%.2f desde %s", monto, origen.getCodigo()));
        }

        // Ejecución financiera
        origen.retirar(monto);
        destino.depositar(monto);

        origen.agregarTransaccion(txSalida);
        destino.agregarTransaccion(txEntrada);

        // Registrar arista en el grafo de transferencias
        String nodoOrigen = (usuarioOrigen != null) ? usuarioOrigen.getId() : origen.getCodigo();
        String nodoDestino = (usuarioDestino != null) ? usuarioDestino.getId() : destino.getCodigo();
        grafoTransferencias.registrarTransferencia(nodoOrigen, nodoDestino, monto);

        // Calcular puntos (3 puntos por cada 100 unidades en transferencias)
        int puntosGanados = (int) (monto / 100) * 3;
        if (usuarioOrigen != null && puntosGanados > 0) {
            String nivelAnterior = usuarioOrigen.getNivel();
            usuarioOrigen.sumarPuntos(puntosGanados);
            if (!nivelAnterior.equals(usuarioOrigen.getNivel())) {
                gestorNotificaciones.agregarNotificacion("NIVEL-" + idTransaccion,
                        "¡Felicidades " + usuarioOrigen.getNombre() + "! Ascendiste a nivel " + usuarioOrigen.getNivel());
            }
        }

        // Alerta de saldo bajo (< $50.00)
        if (origen.getSaldo() < 50.0) {
            gestorNotificaciones.agregarNotificacion("SALDO-" + idTransaccion,
                    String.format("Saldo bajo en billetera %s: $%.2f", origen.getCodigo(), origen.getSaldo()));
        }

        System.out.printf("[ÉXITO] Transferencia de $%.2f realizada de %s a %s (+%d pts)%n",
                monto, origen.getCodigo(), destino.getCodigo(), puntosGanados);
        return true;
    }

    // Sobrecarga compatible para depósito simple
    public boolean depositar(Billetera destino, double monto, String idTransaccion) {
        return depositar(null, destino, monto, idTransaccion);
    }

    // Depósito / Recarga completa con puntos (1 punto por cada 100 unidades)
    public boolean depositar(Usuario usuarioDestino, Billetera destino, double monto, String idTransaccion) {
        if (destino == null || !destino.isActiva() || monto <= 0) {
            System.out.println("[ERROR TRANSACCIÓN] Depósito inválido.");
            return false;
        }

        destino.depositar(monto);
        Transaccion tx = new Transaccion(idTransaccion, "DEPOSITO", monto, "EXTERNO", destino.getCodigo());
        destino.agregarTransaccion(tx);

        int puntosGanados = (int) (monto / 100) * 1;
        if (usuarioDestino != null && puntosGanados > 0) {
            String nivelAnterior = usuarioDestino.getNivel();
            usuarioDestino.sumarPuntos(puntosGanados);
            if (!nivelAnterior.equals(usuarioDestino.getNivel())) {
                gestorNotificaciones.agregarNotificacion("NIVEL-" + idTransaccion,
                        "¡Felicidades " + usuarioDestino.getNombre() + "! Ascendiste a nivel " + usuarioDestino.getNivel());
            }
        }

        System.out.printf("[ÉXITO] Depósito de $%.2f en %s (+%d pts)%n", monto, destino.getCodigo(), puntosGanados);
        return true;
    }

    // Sobrecarga compatible para retiro simple
    public boolean retirar(Billetera origen, double monto, String idTransaccion) {
        return retirar(null, origen, monto, idTransaccion);
    }

    // Retiro completo con puntos (2 puntos por cada 100 unidades) y alertas
    public boolean retirar(Usuario usuarioOrigen, Billetera origen, double monto, String idTransaccion) {
        if (origen == null || !origen.isActiva() || monto <= 0) {
            System.out.println("[ERROR TRANSACCIÓN] Retiro inválido.");
            return false;
        }

        if (origen.getSaldo() < monto) {
            System.out.println("[ERROR TRANSACCIÓN] Saldo insuficiente para el retiro.");
            gestorNotificaciones.agregarNotificacion("NOTIF-" + idTransaccion, "Retiro rechazado por saldo insuficiente en " + origen.getCodigo());
            return false;
        }

        Transaccion tx = new Transaccion(idTransaccion, "RETIRO", monto, origen.getCodigo(), "CAJERO/EXTERNO");
        if (detectorFraude.evaluarTransaccion(tx)) {
            gestorNotificaciones.agregarNotificacion("ALERTA-" + idTransaccion,
                    String.format("Alerta de seguridad: retiro inusual de $%.2f en %s", monto, origen.getCodigo()));
        }

        origen.retirar(monto);
        origen.agregarTransaccion(tx);

        int puntosGanados = (int) (monto / 100) * 2;
        if (usuarioOrigen != null && puntosGanados > 0) {
            String nivelAnterior = usuarioOrigen.getNivel();
            usuarioOrigen.sumarPuntos(puntosGanados);
            if (!nivelAnterior.equals(usuarioOrigen.getNivel())) {
                gestorNotificaciones.agregarNotificacion("NIVEL-" + idTransaccion,
                        "¡Felicidades " + usuarioOrigen.getNombre() + "! Ascendiste a nivel " + usuarioOrigen.getNivel());
            }
        }

        if (origen.getSaldo() < 50.0) {
            gestorNotificaciones.agregarNotificacion("SALDO-" + idTransaccion,
                    String.format("Saldo bajo en billetera %s: $%.2f", origen.getCodigo(), origen.getSaldo()));
        }

        System.out.printf("[ÉXITO] Retiro de $%.2f en %s (+%d pts)%n", monto, origen.getCodigo(), puntosGanados);
        return true;
    }

    public DetectorFraude getDetectorFraude() {
        return detectorFraude;
    }

    public GestorNotificaciones getGestorNotificaciones() {
        return gestorNotificaciones;
    }

    public GrafoTransferencias getGrafoTransferencias() {
        return grafoTransferencias;
    }
}