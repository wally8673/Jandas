# 1) Objetivo y Alcance
El objetivo será crear e implementar una librería que permite manipular datos de forma tabular.
El alcance seria implementar nuevas funcionalidades y optimizaciones para mejorar la experiencia de usuario, también agregando compatibilidad con otras librerías.
	
# 2) Descripción De alto nivel del sistema

La librería Jandas es una herramienta diseñada para manipular y analizar datos estructurados en forma de tablas (2D) dentro del ecosistema Java, inspirada en funcionalidades básicas de librerías como pandas (Python) o DataFrames (R), pero con un enfoque minimalista y sin dependencias externas.

Estructura Central:

Representa datos como tablas compuestas por filas y columnas, donde cada columna tiene un tipo de dato definido (numérico, booleano, cadena o valor faltante NA).

Soporta etiquetas personalizadas para filas/columnas (números o strings).

Operaciones Clave:
Carga/Guardado: Importar y exportar datos desde/hacía archivos CSV (con delimitadores configurables).

Manipulación Básica: Acceso indexado, inserción/eliminación de filas/columnas y modificación de celdas.

Operaciones Avanzadas:

Filtrado con condiciones lógicas ("edad > 30 AND activo = true").
Ordenamiento por columnas (ascendente/descendente).
Concatenación de tablas compatibles.
Manejo de valores faltantes (NA) mediante imputación.

Extensibilidad:
Diseño modular para añadir futuras funcionalidades (ej: nuevos tipos de datos, operaciones estadísticas).

# 3) Requerimientos funcionalidades más relevantes.

Sistema de implementación de estructura de datos.
	RF 1.1: TAD de dataframe de 2 dimensiones con n filas y m columnas.
RF 1.2: TAD para una fila.
RF 1.3: TAD para una columna.
RF 1.4: Implementar etiquetas(labels) para poder indexar filas y columnas.
 	RF 1.5 Carga de archivos csv.
	RF 1.6 Lectura del archivo csv.
	RF 1.7 Modificación del archivo csv.
	RF 1.8 Creación de datasets.

Operación sobre la estructura de datos(métodos que se nos ocurran)
RF 2.1: Método head(visualizar n primeros datos).
RF 2.2: Método para visualizar n fila o m columnas.
RF 2.3: Modificación de celda nxm.
RF 2.4: Realización de copia profunda.
RF 2.5: Filtrado de datos.
RF 2.6: Acceso a los datos por indexación.
RF 2.7: Método de tipo de dato.
RF 2.8: Eliminación de fila o columna.
RF 2.9: Método de ordenamiento(ascendente o descendente).
RF 2.10: Método para unir tablas.

Sistema de visualización de datos en consola.
RF 3.1: Cantidad de elementos que se visualizan por consola.
RF 3.2: Mensajes de error.

# 4) Requerimientos no funcionales.

Tiempo de respuesta para cada operación.(en ms)
Optimización y Escalabilidad. 
RF 4.1: Implementacion de algoritmos de busqueda mas eficientes
RF 4.2: Capacidad de crear nuevos métodos a partir de las clases preexistentes
Estabilidad
RF 4.3: Capacidad de generar Excepciones y rellenar con NA en caso de datos faltantes.
Compatibilidad y Tecnología 
RF 4.4: Ser usado en java 8 o superior
