package servicios;

import estructuras.ListaEnlazada;
import estructuras.TablaHash;
import modelos.Usuario;

public class GestorUsuarios {
    private TablaHash<String, Usuario> usuariosPorId;
    private ListaEnlazada<Usuario> listaUsuarios;

    public GestorUsuarios(int capacidad) {
        this.usuariosPorId = new TablaHash<>(capacidad);
        this.listaUsuarios = new ListaEnlazada<>();
    }

    // Registrar usuario con validación estricta de duplicados y datos nulos
    public boolean registrarUsuario(String idUsuario, String nombre) {
        if (idUsuario == null || idUsuario.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
            System.out.println("[ERROR SEGURIDAD] El ID y el nombre no pueden estar vacíos.");
            return false;
        }

        // Cláusula de seguridad: Evitar registrar dos usuarios con el mismo ID
        if (usuariosPorId.buscar(idUsuario) != null) {
            System.out.println("[ERROR SEGURIDAD] El usuario con ID " + idUsuario + " ya se encuentra registrado.");
            return false;
        }

        Usuario nuevoUsuario = new Usuario(idUsuario, nombre);
        usuariosPorId.insertar(idUsuario, nuevoUsuario);
        listaUsuarios.agregar(nuevoUsuario);
        return true;
    }

    public Usuario buscarUsuario(String idUsuario) {
        if (idUsuario == null) return null;
        return usuariosPorId.buscar(idUsuario);
    }

    public boolean existeUsuario(String idUsuario) {
        return buscarUsuario(idUsuario) != null;
    }

    public ListaEnlazada<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}