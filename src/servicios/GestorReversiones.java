package servicios;

import estructuras.ListaEnlazada;
import estructuras.Stack;
import modelos.Billetera;
import modelos.Transaccion;
import modelos.Usuario;

public class GestorReversiones {

    private Stack<Transaccion> pilaReversiones;

    public GestorReversiones() {
        this.pilaReversiones = new Stack<>();
    }

    public void registrarTransaccionParaReversion(Transaccion transaccion) {
        if (transaccion != null) {
            pilaReversiones.push(transaccion);
        }
    }

    public boolean hayReversionesPendientes() {
        return !pilaReversiones.isEmpty();
    }

    public Transaccion revertirUltimaTransaccion() {
        if (pilaReversiones.isEmpty()) {
            return null;
        }
        return pilaReversiones.pop();
    }

    // Reversión completa: actualiza saldos, registra en historial y descuenta puntos acumulados
    public boolean revertirUltimaOperacionCompleta(GestorUsuarios gestorUsuarios, GestorBilleteras gestorBilleteras, GestorNotificaciones notificaciones) {
        if (pilaReversiones.isEmpty()) {
            System.out.println("[AVISO] No hay operaciones pendientes para revertir.");
            return false;
        }

        Transaccion tx = pilaReversiones.pop();
        double monto = tx.getMonto();
        String tipo = tx.getTipo();

        if ("DEPOSITO".equals(tipo)) {
            Billetera destino = gestorBilleteras.buscarBilletera(tx.getDestino());
            if (destino == null || destino.getSaldo() < monto) {
                System.out.println("[ERROR REVERSIÓN] Saldo insuficiente en " + tx.getDestino() + " para revertir el depósito.");
                return false;
            }
            destino.retirar(monto);
            destino.agregarTransaccion(new Transaccion("REV-" + tx.getId(), "REVERSION_DEPOSITO", monto, tx.getDestino(), "EXTERNO"));
            descontarPuntosPorCodigoBilletera(gestorUsuarios, tx.getDestino(), (int) (monto / 100) * 1);

        } else if ("RETIRO".equals(tipo)) {
            Billetera origen = gestorBilleteras.buscarBilletera(tx.getOrigen());
            if (origen == null) return false;
            origen.depositar(monto);
            origen.agregarTransaccion(new Transaccion("REV-" + tx.getId(), "REVERSION_RETIRO", monto, "EXTERNO", tx.getOrigen()));
            descontarPuntosPorCodigoBilletera(gestorUsuarios, tx.getOrigen(), (int) (monto / 100) * 2);

        } else if ("TRANSFERENCIA_ENVIADA".equals(tipo)) {
            Billetera origen = gestorBilleteras.buscarBilletera(tx.getOrigen());
            Billetera destino = gestorBilleteras.buscarBilletera(tx.getDestino());
            if (origen == null || destino == null || destino.getSaldo() < monto) {
                System.out.println("[ERROR REVERSIÓN] No es posible revertir la transferencia (fondos insuficientes en destino).");
                return false;
            }
            destino.retirar(monto);
            origen.depositar(monto);

            destino.agregarTransaccion(new Transaccion("REV-" + tx.getId(), "REVERSION_TRANSFERENCIA_OUT", monto, destino.getCodigo(), origen.getCodigo()));
            origen.agregarTransaccion(new Transaccion("REV-" + tx.getId(), "REVERSION_TRANSFERENCIA_IN", monto, destino.getCodigo(), origen.getCodigo()));
            descontarPuntosPorCodigoBilletera(gestorUsuarios, tx.getOrigen(), (int) (monto / 100) * 3);
        }

        if (notificaciones != null) {
            notificaciones.agregarNotificacion("REV-" + tx.getId(),
                    String.format("Operación %s revertida por $%.2f. Saldos y puntos actualizados.", tx.getId(), monto));
        }

        System.out.printf("[ÉXITO] Operación %s revertida ($%.2f) con recálculo de saldos y puntos.%n", tx.getId(), monto);
        return true;
    }

    private void descontarPuntosPorCodigoBilletera(GestorUsuarios gestorUsuarios, String codigoBilletera, int puntosADescontar) {
        if (gestorUsuarios == null || puntosADescontar <= 0) return;
        ListaEnlazada<Usuario> usuarios = gestorUsuarios.getListaUsuarios();
        for (int i = 0; i < usuarios.getSize(); i++) {
            Usuario u = usuarios.obtener(i);
            ListaEnlazada<Billetera> billeteras = u.getBilleteras();
            for (int j = 0; j < billeteras.getSize(); j++) {
                if (billeteras.obtener(j).getCodigo().equals(codigoBilletera)) {
                    u.descontarPuntos(puntosADescontar);
                    return;
                }
            }
        }
    }
}