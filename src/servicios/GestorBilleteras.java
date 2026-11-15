package servicios;

import estructuras.TablaHash;
import modelos.Billetera;
import modelos.Usuario;

public class GestorBilleteras {
    private TablaHash<String, Billetera> billeterasPorCodigo;
    private TablaHash<String, Usuario> propietarioPorBilletera; // Para saber rápidamente de quién es cada billetera

    public GestorBilleteras(int capacidadTabla) {
        this.billeterasPorCodigo = new TablaHash<>(capacidadTabla);
        this.propietarioPorBilletera = new TablaHash<>(capacidadTabla);
    }

    // Crear y vincular una billetera a un usuario (Cumple requisito 4.1)
    public boolean crearBilletera(Usuario usuario, String codigo, String tipo, double saldoInicial) {
        if (usuario == null || billeterasPorCodigo.buscar(codigo) != null) {
            return false; // Usuario no válido o código de billetera ya existente
        }
        Billetera nueva = new Billetera(codigo, tipo, saldoInicial);
        usuario.agregarBilletera(nueva);
        billeterasPorCodigo.insertar(codigo, nueva);
        propietarioPorBilletera.insertar(codigo, usuario);
        return true;
    }

    // Localizar billetera en tiempo O(1)
    public Billetera buscarBilletera(String codigo) {
        return billeterasPorCodigo.buscar(codigo);
    }

    // Saber qué usuario es dueño de una billetera en tiempo O(1)
    public Usuario obtenerPropietario(String codigoBilletera) {
        return propietarioPorBilletera.buscar(codigoBilletera);
    }
}