package servicios;

import estructuras.TablaHash;
import modelos.Billetera;
import modelos.Usuario;

public class GestorBilleteras {
    private TablaHash<String, Billetera> billeterasGlobales;

    public GestorBilleteras(int capacidad) {
        this.billeterasGlobales = new TablaHash<>(capacidad);
    }

    // Crear billetera con validaciones estrictas de seguridad bancaria
    public boolean crearBilletera(Usuario usuario, String codigoBilletera, String tipo, double saldoInicial) {
        if (usuario == null) {
            System.out.println("[ERROR SEGURIDAD] No se puede crear una billetera para un usuario nulo.");
            return false;
        }

        if (codigoBilletera == null || codigoBilletera.trim().isEmpty()) {
            System.out.println("[ERROR SEGURIDAD] El código de la billetera no puede estar vacío.");
            return false;
        }

        if (saldoInicial < 0) {
            System.out.println("[ERROR SEGURIDAD] El saldo inicial no puede ser negativo.");
            return false;
        }

        // Cláusula de seguridad: Evitar códigos de billetera duplicados en el banco
        if (billeterasGlobales.buscar(codigoBilletera) != null) {
            System.out.println("[ERROR SEGURIDAD] La billetera con código " + codigoBilletera + " ya existe.");
            return false;
        }

        Billetera nuevaBilletera = new Billetera(codigoBilletera, tipo, saldoInicial);
        
        // Asociarla al usuario y al registro global
        usuario.agregarBilletera(nuevaBilletera);
        billeterasGlobales.insertar(codigoBilletera, nuevaBilletera);
        return true;
    }

    public Billetera buscarBilletera(String codigo) {
        if (codigo == null) return null;
        return billeterasGlobales.buscar(codigo);
    }

    // Eliminar del registro global todas las billeteras de un usuario (se usa al eliminar el usuario)
    public void eliminarBilleterasDeUsuario(Usuario usuario) {
        if (usuario == null) return;
        for (int i = 0; i < usuario.getBilleteras().getSize(); i++) {
            billeterasGlobales.eliminar(usuario.getBilleteras().obtener(i).getCodigo());
        }
    }
}