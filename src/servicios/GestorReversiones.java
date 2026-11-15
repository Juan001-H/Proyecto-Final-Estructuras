package servicios;

import estructuras.Stack;
import modelos.Transaccion;

public class GestorReversiones {
    private Stack<Transaccion> pilaReversiones;

    public GestorReversiones() {
        this.pilaReversiones = new Stack<>();
    }

    // Registrar una transacción realizada para poder revertirla después
    public void registrarTransaccionParaReversion(Transaccion transaccion) {
        pilaReversiones.push(transaccion);
    }

    // Revertir la última transacción ejecutada
    public Transaccion revertirUltimaTransaccion() {
        if (!pilaReversiones.isEmpty()) {
            return pilaReversiones.pop();
        }
        return null; // No hay transacciones para revertir
    }

    // Consultar cuál es la última transacción pendiente de posible reversión sin sacarla
    public Transaccion verUltimaTransaccion() {
        if (!pilaReversiones.isEmpty()) {
            return pilaReversiones.peek();
        }
        return null;
    }

    public boolean hayReversionesPendientes() {
        return !pilaReversiones.isEmpty();
    }
}