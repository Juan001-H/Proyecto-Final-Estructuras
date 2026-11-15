package estructuras;

public class GrafoDirigido<T> {

    // Nodo interno para manejar las aristas (conexiones de transferencia)
    private static class Arista<T> {
        T destino;
        double peso; // Monto acumulado de las transferencias
        Arista<T> siguiente;

        public Arista(T destino, double peso) {
            this.destino = destino;
            this.peso = peso;
            this.siguiente = null;
        }
    }

    // Nodo interno para los vértices (Usuarios o Billeteras)
    private static class VerticeGrafo<T> {
        T dato;
        Arista<T> aristasAdyacentes;
        VerticeGrafo<T> siguiente;

        public VerticeGrafo(T dato) {
            this.dato = dato;
            this.aristasAdyacentes = null;
            this.siguiente = null;
        }
    }

    private VerticeGrafo<T> cabezaVertices;
    private int numVertices;

    public GrafoDirigido() {
        this.cabezaVertices = null;
        this.numVertices = 0;
    }

    public boolean isEmpty() {
        return cabezaVertices == null;
    }

    public int getNumVertices() {
        return numVertices;
    }

    // Agregar un vértice (Usuario/Billetera) si no existe
    public void agregarVertice(T dato) {
        if (!existeVertice(dato)) {
            VerticeGrafo<T> nuevoVertice = new VerticeGrafo<>(dato);
            nuevoVertice.siguiente = cabezaVertices;
            cabezaVertices = nuevoVertice;
            numVertices++;
        }
    }

    public boolean existeVertice(T dato) {
        VerticeGrafo<T> actual = cabezaVertices;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    // Agregar o actualizar una arista (Transferencia de dinero con valor)
    public void agregarArista(T origen, T destino, double monto) {
        // Asegurar que ambos vértices existan en el grafo
        agregarVertice(origen);
        agregarVertice(destino);

        VerticeGrafo<T> vOrigen = obtenerVerticeNodo(origen);
        if (vOrigen != null) {
            Arista<T> actualArista = vOrigen.aristasAdyacentes;
            // Verificar si la conexión ya existe para acumular el monto transferido
            while (actualArista != null) {
                if (actualArista.destino.equals(destino)) {
                    actualArista.peso += monto;
                    return;
                }
                actualArista = actualArista.siguiente;
            }

            // Si no existe la arista, crear una nueva al inicio de la lista de adyacencia
            Arista<T> nuevaArista = new Arista<>(destino, monto);
            nuevaArista.siguiente = vOrigen.aristasAdyacentes;
            vOrigen.aristasAdyacentes = nuevaArista;
        }
    }

    private VerticeGrafo<T> obtenerVerticeNodo(T dato) {
        VerticeGrafo<T> actual = cabezaVertices;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    // Método para consultar las conexiones salientes de un nodo
    public String obtenerConexiones(T dato) {
        VerticeGrafo<T> v = obtenerVerticeNodo(dato);
        if (v == null) return "[]";

        StringBuilder sb = new StringBuilder("[");
        Arista<T> arista = v.aristasAdyacentes;
        while (arista != null) {
            sb.append("{Destino: ").append(arista.destino).append(", MontoTotal: $").append(arista.peso).append("}");
            if (arista.siguiente != null) sb.append(", ");
            arista = arista.siguiente;
        }
        return sb.append("]").toString();
    }

    public void limpiar() {
        cabezaVertices = null;
        numVertices = 0;
    }
}