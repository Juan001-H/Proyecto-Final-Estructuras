# Proyecto Final - Estructuras de Datos

Billetera virtual (fintech) hecha en Java con interfaz gráfica en Swing. Todas las estructuras de datos son propias, sin usar las colecciones de `java.util`.

**Integrantes:** Tomas Suaza y Juan Esteban Herrera.

## Qué hace el programa

La ventana principal tiene seis pestañas:

- **Clientes & Billeteras:** registrar usuarios y crearles billeteras con saldo inicial.
- **Motor Transaccional:** recargas, retiros y transferencias entre billeteras. Cada operación suma puntos al usuario y sube su nivel (Bronce, Plata, Oro o Platino).
- **Tareas Programadas:** tareas que se atienden según su prioridad.
- **Antifraude & Seguridad:** marca como sospechosas las transacciones de monto muy alto y permite revertir la última operación.
- **Alertas y Notificaciones:** historial de todo lo que pasa en el sistema.
- **Analítica & Grafos:** ranking de usuarios por puntos, monto total movilizado, usuario y billetera más activos, y el grafo de quién le transfiere a quién.

## Estructuras de datos y dónde se usan

| Estructura | Uso en el proyecto |
|---|---|
| `ListaEnlazada` | Lista de usuarios, billeteras de cada usuario, historial de transacciones y notificaciones |
| `TablaHash` | Búsqueda rápida de usuarios por id y de billeteras por código |
| `Stack` (pila) | Reversiones: la última transacción es la primera que se deshace |
| `Cola` / `ColaPrioridad` | Programador de tareas, que atiende primero la de mayor prioridad |
| `ArbolUsuarios` (ABB) | Ranking de usuarios ordenado por puntos |
| `GrafoTransferencias` / `GrafoDirigido` | Relaciones de transferencias entre usuarios |

## Estructura del proyecto

```
src/
├── App.java          # punto de entrada, abre la ventana
├── estructuras/      # estructuras de datos propias
├── modelos/          # Usuario, Billetera, Transaccion
├── servicios/        # lógica: gestores, motor transaccional, antifraude, analizador
└── vista/            # VentanaPrincipal (interfaz gráfica)
lib/flatlaf-3.8.jar   # librería del tema oscuro de la interfaz
```

## Cómo ejecutarlo

En VS Code: abrir `src/App.java` y darle **Run**.

Desde la terminal, en la carpeta del proyecto:

**Mac / Linux**
```bash
javac -encoding UTF-8 -cp lib/flatlaf-3.8.jar -d bin $(find src -name "*.java")
java -cp "bin:lib/flatlaf-3.8.jar" App
```

**Windows (PowerShell)**
```powershell
javac -encoding UTF-8 -cp lib/flatlaf-3.8.jar -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "bin;lib/flatlaf-3.8.jar" App
```
