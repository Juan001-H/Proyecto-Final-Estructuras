package servicios;

import estructuras.TablaHash;
import estructuras.ListaEnlazada;
import modelos.Usuario;

public class GestorUsuarios {
    private TablaHash<String, Usuario> usuariosPorId;
    private ListaEnlazada<Usuario> listaUsuarios;

    public GestorUsuarios(int capacidadTabla) {
        this.usuariosPorId = new TablaHash<>(capacidadTabla);
        this.listaUsuarios = new ListaEnlazada<>();
    }

    // Registrar un nuevo usuario (Cumple requisito 1)
    public boolean registrarUsuario(String idUsuario, String nombre) {
        if (usuariosPorId.buscar(idUsuario) != null) {
            return false; // El usuario ya existe
        }
        Usuario nuevoUsuario = new Usuario(idUsuario, nombre);
        usuariosPorId.insertar(idUsuario, nuevoUsuario);
        listaUsuarios.agregar(nuevoUsuario);
        return true;
    }

    // Buscar usuario por ID usando Tabla Hash O(1)
    public Usuario buscarUsuario(String idUsuario) {
        return usuariosPorId.buscar(idUsuario);
    }

    // Eliminar usuario
    public boolean eliminarUsuario(String idUsuario) {
        Usuario usuario = usuariosPorId.buscar(idUsuario);
        if (usuario != null) {
            usuariosPorId.eliminar(idUsuario);
            listaUsuarios.eliminar(usuario);
            return true;
        }
        return false;
    }

    public ListaEnlazada<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}