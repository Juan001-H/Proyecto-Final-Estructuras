package servicios;

import estructuras.ListaEnlazada;

public class GestorNotificaciones {

    public static class Notificacion {
        private final String id;
        private final String mensaje;
        private final String fecha;

        public Notificacion(String id, String mensaje) {
            this.id = id;
            this.mensaje = mensaje;
            this.fecha = java.time.LocalDateTime.now().toString();
        }

        public String getId() {
            return id;
        }

        public String getMensaje() {
            return mensaje;
        }

        public String getFecha() {
            return fecha;
        }
    }

    private final ListaEnlazada<Notificacion> historialNotificaciones;

    public GestorNotificaciones() {
        this.historialNotificaciones = new ListaEnlazada<>();
    }

    public void agregarNotificacion(String id, String mensaje) {
        Notificacion nueva = new Notificacion(id, mensaje);
        historialNotificaciones.agregar(nueva);
    }

    public ListaEnlazada<Notificacion> getHistorialNotificaciones() {
        return historialNotificaciones;
    }
}