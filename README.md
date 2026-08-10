# SmartTask

Aplicación de consola en Java para la gestión de tareas personales, desarrollada como proyecto final del módulo 4 (Fundamentos de Programación en Java).

## Descripción general

SmartTask permite administrar tareas diarias desde la consola: agregarlas, listarlas (todas, activas o completadas), marcarlas como completadas, eliminarlas y editarlas. El proyecto aplica programación orientada a objetos completa (encapsulamiento, herencia, polimorfismo, interfaces y clases abstractas), está documentado con JavaDoc, cuenta con pruebas unitarias en JUnit 5, y está gestionado con Maven.

## Requisitos

- JDK 21 o superior
- Maven 3.8 o superior (o usar el Maven embebido de IntelliJ IDEA)

## Instalación y ejecución

1. Clonar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA (se reconoce automáticamente como proyecto Maven gracias al `pom.xml`).
3. Compilar y generar el ejecutable:
   ```
   mvn clean package
   ```
4. Ejecutar desde una terminal (no funciona con doble clic, porque es un programa de consola que necesita una terminal interactiva para leer el teclado):
   ```
   cd target
   java -jar smarttask-1.0.0.jar
   ```

## Ejecutar las pruebas

```
mvn test
```

## Generar la documentación JavaDoc

Desde IntelliJ IDEA: `Tools > Generate JavaDoc...`

## Estructura del proyecto

```
SmartTask/
├── src/main/java/cl/jenniferperez/smarttask/
│   ├── contrato/
│   │   └── Accionable.java          (interfaz)
│   ├── modelo/
│   │   ├── Tarea.java               (clase abstracta)
│   │   ├── TareaNormal.java         (hereda de Tarea)
│   │   └── TareaUrgente.java        (hereda de Tarea)
│   ├── servicio/
│   │   └── GestorTareas.java        (lógica de negocio)
│   ├── vista/
│   │   └── MenuConsola.java         (interfaz de consola)
│   └── Main.java                    (punto de entrada)
├── src/test/java/cl/jenniferperez/smarttask/
│   └── (pruebas JUnit 5, misma estructura de paquetes)
├── pom.xml
└── README.md
```

La separación en paquetes (`contrato`, `modelo`, `servicio`, `vista`) responde al principio de responsabilidad única: cada capa tiene un propósito concreto y no se mezcla con las demás. El modelo no sabe nada de consola, el servicio no sabe nada de consola ni de cómo se construyen las tareas, y la vista no contiene lógica de negocio, solo coordina entrada y salida.

## Aplicación de conceptos de POO

| Concepto | Dónde se aplica |
|---|---|
| Clases e instancias | `Tarea`, `TareaNormal`, `TareaUrgente`, `GestorTareas`, `MenuConsola` |
| Encapsulamiento | Todos los atributos de `Tarea` y sus clases hijas son `private`, con getters/setters que validan antes de modificar el estado |
| Interfaz | `Accionable` define el contrato común (`completar()`, `estaCompletada()`, `obtenerDetalle()`) |
| Clase abstracta | `Tarea` centraliza el estado y comportamiento común; define `obtenerTipo()` y `obtenerMensajeEspecial()` como métodos abstractos que cada clase hija debe implementar |
| Herencia | `TareaNormal extends Tarea` y `TareaUrgente extends Tarea` |
| Polimorfismo | `GestorTareas` maneja una única `List<Tarea>` sin importar el tipo concreto; al llamar `obtenerDetalle()` sobre cualquier tarea, Java ejecuta automáticamente la versión correcta según el tipo real del objeto |

## Justificación y reflexión de las decisiones de diseño

En esta sección explico el razonamiento detrás de las decisiones que fui tomando durante el desarrollo, incluyendo varios cambios que hice sobre versiones anteriores del diseño al darme cuenta de que no tenían sentido real de uso.

### 1. Por qué decidí que la prioridad fuera de 1 a 3 y no de 1 a 5

Mi primera versión usaba una escala de 1 a 5, tomada de material de referencia del curso. Al revisarla con más calma, me di cuenta de que esa granularidad no tenía mucho sentido: para tareas del día a día no necesito distinguir cinco niveles de urgencia, con tres niveles (baja, media, alta) me alcanza para ordenar y priorizar sin complicarle la decisión a quien usa la aplicación. Reducir la escala también me simplificó la validación y los mensajes de error.

### 2. Por qué agregué `fechaCreacion` y `fechaVencimiento` en la clase `Tarea`

En una versión anterior, `TareaNormal` tenía un campo `categoria` (texto libre) y `TareaUrgente` tenía un campo `requiereRecordatorio` (booleano). Al revisarlos, noté que ninguno de los dos aportaba información realmente útil: `categoria` no se usaba para nada más que mostrarse en pantalla, y `requiereRecordatorio` no tenía sentido porque, por definición, toda tarea urgente requiere recordatorio; no es algo que debiera preguntarle al usuario.

Repensé el diseño desde una pregunta más realista: si el sistema le va a recordar tareas a una persona, necesita saber desde cuándo existe la tarea y hasta cuándo es válida. Por eso subí ambas fechas a la clase abstracta `Tarea`, ya que son datos que cualquier tarea necesita, sin importar su tipo:

- `fechaCreacion`: se asigna sola al crear el objeto (`LocalDate.now()`), el usuario no la ingresa. No tendría sentido dejar que alguien inventara una fecha de creación falsa.
- `fechaVencimiento`: la define el usuario, y valido que no sea anterior a hoy (no tiene sentido crear una tarea que ya venció).

### 3. Por qué le di a `TareaNormal` el campo `minutosAntes` y a `TareaUrgente` el campo `frecuencia`

Una vez que tuve la fecha de vencimiento como ancla temporal real, me pregunté qué diferencia de comportamiento tenía sentido entre una tarea normal y una urgente, más allá del nombre. Llegué a esto:

- **`TareaNormal.minutosAntes`**: un aviso único, X minutos antes del vencimiento. Me parece el comportamiento esperado para una tarea común: un solo recordatorio basta.
- **`TareaUrgente.frecuencia`**: un recordatorio que se repite cada cierta cantidad de minutos hasta que la tarea se complete. Esto sí refleja lo que entiendo por "urgente": no basta un solo aviso, necesito que insista porque olvidarla sale más caro.

Este fue el punto donde sentí que el polimorfismo dejó de ser solo un requisito que tenía que cumplir y pasó a tener sentido real: cada clase hija responde distinto a la misma pregunta ("¿cómo se avisa de esta tarea?"), y el resto del programa (`GestorTareas`, `MenuConsola`) no necesita saber cuál es cuál para funcionar bien.

### 4. Por qué decidí validar la fecha con `LocalDate` en vez de dejarla como texto libre

En una versión intermedia, pedía la fecha de vencimiento como un `String` sin exigir ningún formato, lo que dejaba que la persona escribiera cualquier cosa ("mañana", "15 agosto", un formato distinto cada vez), generando datos inconsistentes que además no me servían para calcular nada.

Decidí usar el tipo `LocalDate` de Java, con un formato fijo (`dd/MM/yyyy`) validado con `DateTimeFormatter`. Si el usuario escribe una fecha en un formato inválido, el programa se lo indica y se la vuelve a pedir, en vez de aceptar cualquier texto. Esto me significó más código (un método `leerFecha()` con manejo de la excepción `DateTimeParseException`), pero es lo que realmente me permite validar que "la fecha de vencimiento no puede ser anterior a hoy", algo que con texto libre no habría podido verificar de forma confiable.

### 5. Por qué terminé permitiendo editar la tarea completa, y no solo la fecha

Mi primera versión de la función de editar solo dejaba cambiar la fecha de vencimiento, porque asumí que la única razón para editar una tarea sería extender un plazo. Al pensarlo mejor, me di cuenta de que ese supuesto estaba incompleto: uno se puede equivocar al escribir el nombre, cambiar de opinión sobre la prioridad, o querer ajustar la frecuencia de sus recordatorios, no solo la fecha.

Por eso la opción "Editar tarea" del menú abre un sub-menú donde se puede modificar cualquier campo relevante: nombre, prioridad, fecha de vencimiento, y el campo específico según el tipo de tarea (minutos antes o frecuencia). Este cambio también resolvió algo que tenía pendiente: los métodos `setNombre()`, `setPrioridad()` y `setFechaVencimiento()` existían por encapsulamiento, pero no se usaban en ningún lado del programa. Ahora sí tienen un propósito real.

### 6. Por qué `MenuConsola` recibe un `Scanner` y un `PrintStream` en el constructor

Le di a `MenuConsola` dos constructores: uno simple que usa la entrada y salida estándar (`System.in`, `System.out`) para el uso normal del programa, y otro que me permite inyectar un `Scanner` y un `PrintStream` distintos. Este segundo constructor lo agregué exclusivamente para poder hacer pruebas automatizadas: en los tests simulo la entrada del usuario con un `Scanner` construido desde un `String`, y capturo la salida con un `ByteArrayOutputStream`, sin depender del teclado real ni de la consola. Sin este diseño no habría podido probar automáticamente el flujo del menú.

### 7. Por qué no cierro el `Scanner` explícitamente al final de `iniciar()`

Cerrar un `Scanner` que envuelve `System.in` cierra la entrada estándar de todo el proceso de Java, no solo ese objeto. Como mi programa solo necesita leer del teclado mientras corre y termina naturalmente cuando el usuario elige "Salir", no hay necesidad de cerrarlo a mano: el sistema operativo libera el recurso cuando el proceso termina. Además, cerrarlo explícitamente me habría roto las pruebas unitarias, porque ahí instancio varios `Scanner` distintos a lo largo de la suite de tests.

## Limitaciones conocidas

- El nombre de una tarea solo se valida contra que no esté vacío; no se valida que el texto ingresado tenga sentido semántico (por ejemplo, una cadena de caracteres aleatoria pasa la validación). Se consideró implementar una validación heurística (proporción de vocales, longitud mínima) pero se descartó por estar fuera del alcance de la evaluación.
- El almacenamiento de las tareas es en memoria: al cerrar el programa, se pierde toda la información. No es un requisito del proyecto, pero es una limitación relevante a mencionar si el proyecto evoluciona hacia persistencia en archivo o base de datos.

## Pruebas unitarias

Se implementaron pruebas con JUnit 5 para el modelo (`TareaNormalTest`, `TareaUrgenteTest`), el servicio (`GestorTareasTest`) y la vista (`MenuConsolaTest`), cubriendo casos de éxito, validaciones de datos inválidos, y el flujo completo del menú simulando entradas de teclado.
