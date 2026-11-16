package estructuras;

public class GrafoTransferencias {
    private TablaHash<String, ListaEnlazada<String>> listaAdyacencia;

    public GrafoTransferencias(int capacidad) {
        this.listaAdyacencia = new TablaHash<>(capacidad);
    }

    // Registrar un usuario como nodo en el grafo
    public void agregarUsuarioNodo(String idUsuario) {
        if (listaAdyacencia.buscar(idUsuario) == null) {
            listaAdyacencia.insertar(idUsuario, new ListaEnlazada<>());
        }
    }

    // Registrar una transferencia (arista dirigida con monto) de un usuario a otro
    public void registrarTransferencia(String idOrigen, String idDestino, double monto) {
        agregarUsuarioNodo(idOrigen);
        agregarUsuarioNodo(idDestino);

        ListaEnlazada<String> destinos = listaAdyacencia.buscar(idOrigen);
        if (destinos != null) {
            String detalleTransferencia = idDestino + " (Monto: $" + String.format("%.2f", monto) + ")";
            destinos.agregar(detalleTransferencia);
        }
    }

    // Consultar las rutas o transferencias hechas por un usuario
    public ListaEnlazada<String> obtenerDestinosDe(String idUsuario) {
        return listaAdyacencia.buscar(idUsuario);
    }
}