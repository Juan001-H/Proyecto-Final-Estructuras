package estructuras;

import modelos.Usuario;

public class ArbolUsuarios {
    
    private static class NodoArbol {
        Usuario usuario;
        int puntos;
        NodoArbol izquierdo;
        NodoArbol derecho;

        public NodoArbol(Usuario usuario, int puntos) {
            this.usuario = usuario;
            this.puntos = puntos;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    private NodoArbol raiz;

    public ArbolUsuarios() {
        this.raiz = null;
    }

    // Insertar un usuario en el árbol basado en sus puntos
    public void insertar(Usuario usuario, int puntos) {
        raiz = insertarRecursivo(raiz, usuario, puntos);
    }

    private NodoArbol insertarRecursivo(NodoArbol actual, Usuario usuario, int puntos) {
        if (actual == null) {
            return new NodoArbol(usuario, puntos);
        }

        if (puntos < actual.puntos) {
            actual.izquierdo = insertarRecursivo(actual.izquierdo, usuario, puntos);
        } else {
            actual.derecho = insertarRecursivo(actual.derecho, usuario, puntos);
        }

        return actual;
    }

    // Recorrido InOrden (permite obtener los usuarios ordenados de menor a mayor puntaje)
    public void obtenerUsuariosOrdenados(ListaEnlazada<Usuario> listaResultado) {
        inOrdenRecursivo(raiz, listaResultado);
    }

    private void inOrdenRecursivo(NodoArbol nodo, ListaEnlazada<Usuario> lista) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.izquierdo, lista);
            lista.agregar(nodo.usuario);
            inOrdenRecursivo(nodo.derecho, lista);
        }
    }
}