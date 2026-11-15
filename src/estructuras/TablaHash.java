package estructuras; // O packages estructuras, dependiendo de cómo tengas tus carpetas

public class TablaHash<K, V> {

    private static class Entrada<K, V> {
        K clave;
        V valor;
        Entrada<K, V> siguiente;

        public Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Entrada<K, V>[] tabla;
    private int capacidad;
    private int tamano;

    @SuppressWarnings("unchecked")
    public TablaHash(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new Entrada[capacidad];
        this.tamano = 0;
    }

    private int hash(K clave) {
        return Math.abs(clave.hashCode()) % capacidad;
    }

    // Insertar o actualizar un valor
    public void insertar(K clave, V valor) {
        int indice = hash(clave);
        Entrada<K, V> cabeza = tabla[indice];

        // Verificar si la clave ya existe para actualizarla
        Entrada<K, V> actual = cabeza;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }

        // Si no existe, insertar al inicio de la lista en ese índice
        Entrada<K, V> nuevaEntrada = new Entrada<>(clave, valor);
        nuevaEntrada.siguiente = cabeza;
        tabla[indice] = nuevaEntrada;
        tamano++;
    }

    // Buscar valor por clave O(1) promedio
    public V buscar(K clave) {
        int indice = hash(clave);
        Entrada<K, V> actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null; // No encontrado
    }

    // Eliminar por clave
    public boolean eliminar(K clave) {
        int indice = hash(clave);
        Entrada<K, V> actual = tabla[indice];
        Entrada<K, V> anterior = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public int getTamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }
}