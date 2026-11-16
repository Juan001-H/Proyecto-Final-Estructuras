package modelos;

import estructuras.ListaEnlazada;

public class Usuario {
    private String id;
    private String nombre;
    private ListaEnlazada<Billetera> billeteras;
    private int puntosAcumulados;
    private String nivel; // Bronce, Plata, Oro, Platino

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.billeteras = new ListaEnlazada<>();
        this.puntosAcumulados = 0;
        this.nivel = "Bronce";
    }

    public void agregarBilletera(Billetera billetera) {
        billeteras.agregar(billetera);
    }

    public void sumarPuntos(int puntos) {
        if (puntos > 0) {
            this.puntosAcumulados += puntos;
            actualizarNivel();
        }
    }

    public void descontarPuntos(int puntos) {
        if (puntos > 0) {
            this.puntosAcumulados = Math.max(0, this.puntosAcumulados - puntos);
            actualizarNivel();
        }
    }

    private void actualizarNivel() {
        if (puntosAcumulados > 5000) {
            this.nivel = "Platino";
        } else if (puntosAcumulados > 1000) {
            this.nivel = "Oro";
        } else if (puntosAcumulados > 500) {
            this.nivel = "Plata";
        } else {
            this.nivel = "Bronce";
        }
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public ListaEnlazada<Billetera> getBilleteras() { return billeteras; }
    
    public int getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(int puntosAcumulados) {
        this.puntosAcumulados = Math.max(0, puntosAcumulados);
        actualizarNivel();
    }

    public String getNivel() { return nivel; }
}