package servicios;

import estructuras.ColaPrioridad;

public class ProgramadorTareas {

    public static class TareaFintech implements Comparable<TareaFintech> {
        private final String idTarea;
        private final String descripcion;
        private final int prioridad;

        public TareaFintech(String idTarea, String descripcion, int prioridad) {
            this.idTarea = idTarea;
            this.descripcion = descripcion;
            this.prioridad = prioridad;
        }

        public String getIdTarea() {
            return idTarea;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public int getPrioridad() {
            return prioridad;
        }

        @Override
        public int compareTo(TareaFintech otra) {
            return Integer.compare(this.prioridad, otra.prioridad);
        }

        @Override
        public String toString() {
            return "[Prioridad " + prioridad + "] " + idTarea + " - " + descripcion;
        }
    }

    private final ColaPrioridad<TareaFintech> colaTareas;

    public ProgramadorTareas() {
        this.colaTareas = new ColaPrioridad<>();
    }

    public void programarTarea(String idTarea, String descripcion, int prioridad) {
        TareaFintech nueva = new TareaFintech(idTarea, descripcion, prioridad);
        colaTareas.encolar(nueva);
    }

    public boolean hayTareasPendientes() {
        return !colaTareas.isEmpty();
    }

    public TareaFintech procesarSiguienteTarea() {
        if (colaTareas.isEmpty()) {
            return null;
        }
        return colaTareas.desencolar();
    }
}