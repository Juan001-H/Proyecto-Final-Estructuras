import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import vista.VentanaPrincipal;

public class App {
    public static void main(String[] args) {
        try {
            // Inicializar el motor gráfico FlatLaf Dark Mode de alto nivel
            FlatDarkLaf.setup();

            // Personalizaciones estéticas avanzadas ("con todos los poderes")
            UIManager.put("Button.arc", 12);          // Botones con esquinas redondeadas modernas
            UIManager.put("Component.arc", 10);       // Campos de texto y combos suavizados
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("TabbedPane.selectedBackground", new java.awt.Color(30, 41, 59)); // Estilo de pestañas fintech
            UIManager.put("Table.rowHeight", 28);     // Altura de filas cómoda y legible para tablas
            
        } catch (Exception ex) {
            System.err.println("Falla crítica al inicializar FlatLaf: " + ex.getMessage());
        }

        // Lanzar la interfaz gráfica de manera segura en el hilo EDT de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}