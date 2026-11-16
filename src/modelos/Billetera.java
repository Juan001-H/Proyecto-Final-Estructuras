package modelos;

import estructuras.ListaEnlazada;

public class Billetera {
    private String codigo;
    private String tipo; // Ahorros, Corriente, etc.
    private double saldo;
    private boolean activa;
    private ListaEnlazada<Transaccion> historialTransacciones;

    public Billetera(String codigo, String tipo, double saldoInicial) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.saldo = saldoInicial;
        this.activa = true;
        this.historialTransacciones = new ListaEnlazada<>();
    }

    public void depositar(double monto) {
        if (monto > 0 && activa) {
            this.saldo += monto;
        }
    }

    public boolean retirar(double monto) {
        if (monto > 0 && activa && this.saldo >= monto) {
            this.saldo -= monto;
            return true;
        }
        return false;
    }

    public void agregarTransaccion(Transaccion tx) {
        historialTransacciones.agregar(tx);
    }
    // Método para obtener el saldo formateado como texto con 2 decimales (Ej: "500.00")
public String getSaldoFormateado() {
    return String.format(java.util.Locale.US, "%.2f", this.saldo);
}

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public String getTipo() { return tipo; }
    public double getSaldo() { return saldo; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public ListaEnlazada<Transaccion> getHistorialTransacciones() { return historialTransacciones; }
}
