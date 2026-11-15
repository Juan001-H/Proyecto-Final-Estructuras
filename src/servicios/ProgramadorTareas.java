package servicios;
import estructuras.ColaPrioridad;

public class ProgramadorTareas {

    // Clase interna para representar una tarea programada
    public static class TareaFintech implements Comparable<TareaFintech> {
        private String idTarea;
        private String descripcion;
        private int prioridad;

        public TareaFintech(String idTarea, String descripcion, int prioridad) {
            this.idTarea = idTarea;
            this.descripcion = descripcion;
            this.prioridad = prioridad;
        }

        public int getPrioridad() {
            return prioridad;
        }

        public String getDescripcion() {
            return descripcion;
        }

        @Override
        public int compareTo(TareaFintech otra) {
            return Integer.compare(this.prioridad, otra.prioridad);
        }
    }

    private ColaPrioridad<TareaFintech> colaTareas;

    public ProgramadorTareas() {
        this.colaTareas = new ColaPrioridad<>();
    }

    // Programar una nueva tarea en el sistema
    public void programarTarea(String id, String descripcion, int prioridad) {
        TareaFintech nuevaTarea = new TareaFintech(id, descripcion, prioridad);
        // Usamos el método estándar de inserción en cola de prioridad (si en tu ColaPrioridad se llama 'insertar' o 'encolar', ajústalo aquí)
        colaTareas.encolar(nuevaTarea); // O el método que corresponda según tu implementación
    }

    // Procesar la siguiente tarea
    public TareaFintech procesarSiguienteTarea() {
        if (!colaTareas.isEmpty()) {
            // Si en tu ColaPrioridad el método para extraer es diferente (ej. eliminarMin, extraer, desencolar), 
            // asegúrate de usar el que programaste en tu clase ColaPrioridad.java
            return colaTareas.desencolar(); // O el método que corresponda según tu implementación
        }
        return null;
    }

    public boolean hayTareasPendientes() {
        return !colaTareas.isEmpty();
    }
}