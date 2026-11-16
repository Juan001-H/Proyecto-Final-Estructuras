package servicios;

import estructuras.ArbolUsuarios;
import estructuras.GrafoTransferencias;
import estructuras.ListaEnlazada;
import modelos.Billetera;
import modelos.Transaccion;
import modelos.Usuario;

public class Analizador {

    // Genera un ranking de usuarios ordenados por puntos usando el Árbol Binario de Búsqueda
    public ListaEnlazada<Usuario> obtenerRankingUsuarios(ListaEnlazada<Usuario> usuarios) {
        ArbolUsuarios arbol = new ArbolUsuarios();
        for (int i = 0; i < usuarios.getSize(); i++) {
            Usuario u = usuarios.obtener(i);
            arbol.insertar(u, u.getPuntosAcumulados());
        }

        ListaEnlazada<Usuario> ordenadosAsc = new ListaEnlazada<>();
        arbol.obtenerUsuariosOrdenados(ordenadosAsc);

        ListaEnlazada<Usuario> rankingDesc = new ListaEnlazada<>();
        for (int i = ordenadosAsc.getSize() - 1; i >= 0; i--) {
            rankingDesc.agregar(ordenadosAsc.obtener(i));
        }
        return rankingDesc;
    }

    // Calcula el monto total movilizado entre todas las billeteras
    public double calcularMontoTotalMovilizado(ListaEnlazada<Usuario> usuarios) {
        double total = 0.0;
        for (int i = 0; i < usuarios.getSize(); i++) {
            Usuario u = usuarios.obtener(i);
            ListaEnlazada<Billetera> billeteras = u.getBilleteras();
            for (int j = 0; j < billeteras.getSize(); j++) {
                Billetera b = billeteras.obtener(j);
                ListaEnlazada<Transaccion> historial = b.getHistorialTransacciones();
                for (int k = 0; k < historial.getSize(); k++) {
                    Transaccion tx = historial.obtener(k);
                    if (!"TRANSFERENCIA_RECIBIDA".equals(tx.getTipo())) {
                        total += tx.getMonto();
                    }
                }
            }
        }
        return total;
    }

    // Identifica la billetera con mayor cantidad de transacciones
    public Billetera obtenerBilleteraMasActiva(ListaEnlazada<Usuario> usuarios) {
        Billetera masActiva = null;
        int maxMovimientos = -1;

        for (int i = 0; i < usuarios.getSize(); i++) {
            Usuario u = usuarios.obtener(i);
            ListaEnlazada<Billetera> billeteras = u.getBilleteras();
            for (int j = 0; j < billeteras.getSize(); j++) {
                Billetera b = billeteras.obtener(j);
                int movs = b.getHistorialTransacciones().getSize();
                if (movs > maxMovimientos) {
                    maxMovimientos = movs;
                    masActiva = b;
                }
            }
        }
        return masActiva;
    }

    // Identifica el usuario con mayor actividad transaccional
    public Usuario obtenerUsuarioMasActivo(ListaEnlazada<Usuario> usuarios) {
        Usuario masActivo = null;
        int maxOperaciones = -1;

        for (int i = 0; i < usuarios.getSize(); i++) {
            Usuario u = usuarios.obtener(i);
            int totalOpsUsuario = 0;
            ListaEnlazada<Billetera> billeteras = u.getBilleteras();
            for (int j = 0; j < billeteras.getSize(); j++) {
                totalOpsUsuario += billeteras.obtener(j).getHistorialTransacciones().getSize();
            }
            if (totalOpsUsuario > maxOperaciones) {
                maxOperaciones = totalOpsUsuario;
                masActivo = u;
            }
        }
        return masActivo;
    }

    // Genera un reporte textual consolidado
    public String generarReporteGeneral(ListaEnlazada<Usuario> usuarios, GrafoTransferencias grafo) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE ANALÍTICO DE MOVIMIENTOS ===\n");
        sb.append(String.format("Monto total movilizado: $%.2f%n", calcularMontoTotalMovilizado(usuarios)));

        Usuario topUsuario = obtenerUsuarioMasActivo(usuarios);
        if (topUsuario != null) {
            sb.append("Usuario más activo: ").append(topUsuario.getNombre())
            .append(" (ID: ").append(topUsuario.getId()).append(")\n");
        }

        Billetera topBilletera = obtenerBilleteraMasActiva(usuarios);
        if (topBilletera != null) {
            sb.append("Billetera con mayor uso: ").append(topBilletera.getCodigo())
            .append(" (Movimientos: ").append(topBilletera.getHistorialTransacciones().getSize()).append(")\n");
        }

        sb.append("\n--- Ranking de Fidelización (Árbol de Puntos) ---\n");
        ListaEnlazada<Usuario> ranking = obtenerRankingUsuarios(usuarios);
        for (int i = 0; i < ranking.getSize(); i++) {
            Usuario u = ranking.obtener(i);
            sb.append(String.format("%d. %s | Puntos: %d | Nivel: %s%n",
                    (i + 1), u.getNombre(), u.getPuntosAcumulados(), u.getNivel()));
        }

        sb.append("\n--- Rutas de Transferencias (Grafo) ---\n");
        for (int i = 0; i < usuarios.getSize(); i++) {
            Usuario u = usuarios.obtener(i);
            ListaEnlazada<String> destinos = grafo.obtenerDestinosDe(u.getId());
            if (destinos != null && destinos.getSize() > 0) {
                sb.append("Usuario ").append(u.getId()).append(" transfirió a -> ");
                for (int j = 0; j < destinos.getSize(); j++) {
                    sb.append(destinos.obtener(j));
                    if (j < destinos.getSize() - 1) sb.append(", ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}