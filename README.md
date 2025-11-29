# Turnero Médico

**Materia:** Laboratorio 1 - Programación 3
**Carrera:** Ingeniería en Inteligencia Artificial – Universidad de Palermo

---

## Descripción

Aplicación de escritorio desarrollada en **Java Swing** para la gestión de turnos médicos. Permite administrar médicos, pacientes, consultorios, obras sociales y turnos, con funcionalidades de reportes y cálculo de recaudación.

---

## Funcionalidades

- **Gestión de Médicos:** Alta, baja y modificación de médicos con valor de consulta y obra social asociada
- **Gestión de Pacientes:** Alta, baja y modificación de pacientes con DNI y obra social
- **Gestión de Consultorios:** Administración de consultorios con nombre y ubicación
- **Gestión de Obras Sociales:** Configuración de obras sociales con porcentaje de descuento
- **Gestión de Turnos:** Creación de turnos con validaciones (evita duplicados por médico/hora y consultorio/hora)
- **Reportes:** Consulta de turnos por médico y rango de fechas
- **Recaudación:** Cálculo automático de recaudación con descuentos según obra social
- **Calendario:** Vista mensual de turnos programados
- **Historial:** Consulta de turnos por paciente

---

## Arquitectura

El proyecto implementa una arquitectura en capas:

```
src/
├── modelo/          # Clases de dominio (Medico, Paciente, Turno, etc.)
├── dao/             # Data Access Objects (interfaces)
│   └── impl/h2/     # Implementaciones para H2 Database
├── servicio/        # Lógica de negocio
├── interfaz/        # Capa de presentación (Swing)
│   └── tabla/       # Componentes de tablas y paneles
├── conexiondb/      # Gestión de conexiones a BD
└── validaciones/    # Utilidades de validación
```

---

## Tecnologías Utilizadas

- **Java SE 21**
- **Swing** - Interfaz gráfica
- **JDBC** - Acceso a base de datos
- **H2 Database 2.3.232** - Base de datos embebida

---

## Patrones de Diseño Implementados

- **DAO (Data Access Object)** - Separación entre lógica y acceso a datos
- **Template Method** - En clase `OperacionBD` para operaciones de BD
- **Service Pattern** - Capa de servicios para lógica de negocio
- **MVC** - Separación en Modelo, Vista y Controlador

---

## Modelo de Datos

```
obras_sociales (id, nombre, porcentaje_descuento)
       │
       ├──< medicos (id, nombre, valor_consulta, obra_social_id)
       │         │
       │         └──< turnos (id, medico_id, paciente_id, consultorio_id, fecha_hora)
       │                  │
       └──< pacientes (id, nombre, dni, obra_social_id)
                         │
              consultorios (id, nombre, ubicacion)
```

**Restricciones:**
- `UNIQUE(medico_id, fecha_hora)` - Un médico no puede tener dos turnos a la misma hora
- `UNIQUE(consultorio_id, fecha_hora)` - Un consultorio no puede tener dos turnos a la misma hora

---

## Instrucciones para Ejecutar en Eclipse IDE

### Requisitos Previos
- Eclipse IDE (2023 o superior recomendado)
- JDK 21 instalado y configurado

### Pasos de Importación

1. **Abrir Eclipse IDE**

2. **Importar el proyecto:**
   - `File` → `Import...`
   - Seleccionar `General` → `Existing Projects into Workspace`
   - Click en `Next`
   - En `Select root directory`, hacer click en `Browse...` y seleccionar la carpeta del proyecto
   - Asegurarse de que el proyecto `TurneroMedicoRecuperatorio` esté marcado
   - Click en `Finish`

3. **Verificar configuración del JDK:**
   - Click derecho sobre el proyecto → `Properties`
   - Ir a `Java Build Path` → pestaña `Libraries`
   - Verificar que `JRE System Library` apunte a JavaSE-21
   - Si no está correcto: `Add Library...` → `JRE System Library` → Seleccionar Java 21

4. **Verificar la librería H2:**
   - En `Java Build Path` → `Libraries`, verificar que `h2-2.3.232.jar` esté presente
   - Si no aparece: `Add JARs...` → Navegar a `lib/h2-2.3.232.jar`

### Iniciar el Servidor H2 (Requerido antes de ejecutar)

1. **Abrir una terminal/CMD** en la carpeta del proyecto

2. **Ejecutar el servidor H2:**
   ```bash
   java -jar h2/bin/h2-2.3.232.jar
   ```

3. El servidor iniciará en `localhost:9092`. Dejar esta terminal abierta.

### Ejecutar la Aplicación

1. **Localizar la clase Main:**
   - En el Package Explorer, navegar a `src` → `interfaz` → `Main.java`

2. **Ejecutar:**
   - Click derecho sobre `Main.java`
   - Seleccionar `Run As` → `Java Application`

3. **La aplicación se abrirá** mostrando una ventana con pestañas para cada módulo

### Solución de Problemas Comunes

| Problema | Solución |
|----------|----------|
| Error de conexión a BD | Verificar que el servidor H2 esté corriendo en puerto 9092 |
| Clase Main no encontrada | Verificar que `src` esté marcado como Source Folder |
| Error de compilación | Verificar versión de Java (debe ser 21) |
| H2 JAR no encontrado | Agregar manualmente desde `lib/h2-2.3.232.jar` |

---

## Nota para el Final

> **Recordá identificar donde podés ahorrar código y pensar la UI en términos de componentes para el final.**

### Puntos a tener en cuenta:

1. **Reutilización de código:**
   - Identificar patrones repetitivos en los formularios (Form)
   - Analizar los GridPanel y TableModel para posibles abstracciones
   - Revisar métodos similares en los DAOs que podrían generalizarse

2. **Componentización de la UI:**
   - Los paneles de tabla (`*GridPanel`) siguen estructuras similares
   - Los formularios (`*Form`) comparten lógica de validación y guardado
   - Considerar componentes reutilizables para campos comunes (combos de selección, validadores)

3. **Áreas de mejora identificadas:**
   - Formularios: Extraer clase base `BaseForm<T>` con métodos `guardar()`, `limpiar()`, `validar()`
   - Tablas: Crear `GenericTableModel<T>` con configuración de columnas
   - DAOs: La clase `OperacionBD` ya implementa Template Method, revisar su uso

---

## Estructura de Archivos

```
TurneroMedicoRecuperatorio/
├── src/                    # Código fuente
├── lib/                    # Librerías (H2)
├── h2/                     # Base de datos H2
│   ├── base_de_datos/      # Archivos de BD
│   └── bin/                # Ejecutable H2
├── bin/                    # Clases compiladas
├── .classpath              # Configuración Eclipse
├── .project                # Configuración Eclipse
└── README.md               # Este archivo
```

---

## Autor

Trabajo práctico desarrollado para el final de Laboratorio 1 - Programación 3
