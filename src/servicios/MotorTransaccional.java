package servicios;

import estructuras.ListaEnlazada;
import estructuras.Stack;
import estructuras.GrafoDirigido;
import modelos.Billetera;
import modelos.Transaccion;
import modelos.Usuario;

public class MotorTransaccional {
    private ListaEnlazada<Transaccion> historialGlobal;
    private Stack<Transaccion> pilaReversiones; // Pila exigida para deshacer operaciones
    private GrafoDirigido<String> grafoTransferencias; // Grafo para red de dinero y fraude
    private int contadorIdTx;

    public MotorTransaccional() {
        this.historialGlobal = new ListaEnlazada<>();
        this.pilaReversiones = new Stack<>();
        this.grafoTransferencias = new GrafoDirigido<>();
        this.contadorIdTx = 1000;
    }

    // 1. Recargar saldo
    public boolean recargar(Billetera billetera, double valor) {
        if (billetera == null || !billetera.isActiva() || valor <= 0) return false;

        billetera.depositar(valor);
        String id = "TX-" + (++contadorIdTx);
        Transaccion tx = new Transaccion(id, "RECARGA", valor, "SISTEMA", billetera.getCodigo());
        
        // Política de puntos: 1 punto por cada 100 unidades (Punto 4.4)
        int puntos = (int) (valor / 100);
        tx.setPuntosGenerados(puntos);

        historialGlobal.agregar(tx);
        pilaReversiones.push(tx); // Se puede revertir
        return true;
    }

    // 2. Retirar saldo
    public boolean retirar(Billetera billetera, double valor) {
        if (billetera == null || !billetera.isActiva() || valor <= 0) return false;

        boolean exito = billetera.retirar(valor);
        if (!exito) return false;

        String id = "TX-" + (++contadorIdTx);
        Transaccion tx = new Transaccion(id, "RETIRO", valor, billetera.getCodigo(), "EFECTIVO");
        
        // Política de puntos: 2 puntos por cada 100 unidades (Punto 4.4)
        int puntos = (int) (valor / 100) * 2;
        tx.setPuntosGenerados(puntos);

        historialGlobal.agregar(tx);
        pilaReversiones.push(tx);
        return true;
    }

    // 3. Transferir dinero (Entre billeteras o a terceros)
    public boolean transferir(Billetera origen, Billetera destino, double valor, Usuario remitente) {
        if (origen == null || destino == null || !origen.isActiva() || !destino.isActiva() || valor <= 0) {
            return false;
        }

        if (origen.getSaldo() < valor) return false; // Saldo insuficiente

        // Ejecutar movimiento
        origen.retirar(valor);
        destino.depositar(valor);

        String id = "TX-" + (++contadorIdTx);
        Transaccion tx = new Transaccion(id, "TRANSFERENCIA", valor, origen.getCodigo(), destino.getCodigo());

        // Política de puntos: 3 puntos por cada 100 unidades (Punto 4.4)
        int puntos = (int) (valor / 100) * 3;
        tx.setPuntosGenerados(puntos);

        // Actualizar puntos del usuario remitente
        if (remitente != null) {
            remitente.setPuntosAcumulados(remitente.getPuntosAcumulados() + puntos);
        }

        // Registrar en estructuras
        historialGlobal.agregar(tx);
        pilaReversiones.push(tx); // Guardar en Pila para reversión
        
        // Registrar arista en el Grafo de transferencias (Punto 5.6)
        grafoTransferencias.agregarArista(origen.getCodigo(), destino.getCodigo(), valor);

        return true;
    }

    // Getters para consultar estructuras desde la interfaz
    public ListaEnlazada<Transaccion> getHistorialGlobal() { return historialGlobal; }
    public Stack<Transaccion> getPilaReversiones() { return pilaReversiones; }
    public GrafoDirigido<String> getGrafoTransferencias() { return grafoTransferencias; }
}
