package modelos;

import estructuras.ListaEnlazada;

public class Usuario {
    private String id;
    private String nombre;
    private ListaEnlazada<Billetera> billeteras;
    private int puntosAcumulados;
    private String nivel; // Bronze, Silver, Gold, Platinum

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.billeteras = new ListaEnlazada<>();
        this.puntosAcumulados = 0;
        this.nivel = "Bronze";
    }

    public void agregarBilletera(Billetera billetera) {
        billeteras.agregar(billetera);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public ListaEnlazada<Billetera> getBilleteras() { return billeteras; }
    
    public int getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(int puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
        actualizarNivel();
    }

    public String getNivel() { return nivel; }

    private void actualizarNivel() {
        if (puntosAcumulados >= 1000) {
            this.nivel = "Platinum";
        } else if (puntosAcumulados >= 500) {
            this.nivel = "Gold";
        } else if (puntosAcumulados >= 200) {
            this.nivel = "Silver";
        } else {
            this.nivel = "Bronze";
        }
    }
}

