package modelos;

public class Transaccion {
    private String id;
    private String tipo; // RECARGA, RETIRO, TRANSFERENCIA
    private double monto;
    private String origen;
    private String destino;
    private int puntosGenerados;

    public Transaccion(String id, String tipo, double monto, String origen, String destino) {
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.origen = origen;
        this.destino = destino;
        this.puntosGenerados = 0;
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public int getPuntosGenerados() { return puntosGenerados; }
    public void setPuntosGenerados(int puntosGenerados) { this.puntosGenerados = puntosGenerados; }
}
