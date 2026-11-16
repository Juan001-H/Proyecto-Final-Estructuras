package servicios;

import modelos.Transaccion;

public class DetectorFraude {

    // Límite de monto considerado sospechoso o atípico para una sola transacción estándar
    private static final double LIMITE_MONTO_SOSPECHOSO = 5000.00;

    // Evaluar si una transacción presenta riesgos de comportamiento inusual
    public boolean evaluarTransaccion(Transaccion transaccion) {
        if (transaccion == null) return false;

        // Regla 1: Montos inusualmente altos respecto al uso normal
        if (transaccion.getMonto() > LIMITE_MONTO_SOSPECHOSO) {
            System.out.println("[ALERTA ANTIFRAUDE] ¡Transacción sospechosa detectada! ID: "
                + transaccion.getId() + " excede el límite seguro con $" + String.format("%.2f", transaccion.getMonto()));
            return true; // Es sospechosa / requiere auditoría
        }

        return false; // Operación normal
    }
}